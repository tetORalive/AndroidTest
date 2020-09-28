package xyz.tetatet.vivaquiz.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.settings_view.*
import kotlinx.android.synthetic.main.search_view.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.android.synthetic.main.view_powered_by_google.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import xyz.tetatet.vivaquiz.R
import xyz.tetatet.vivaquiz.di.application.ActivityModule
import xyz.tetatet.vivaquiz.di.main.DaggerMainComponent
import xyz.tetatet.vivaquiz.di.main.MainModule
import xyz.tetatet.vivaquiz.extensions.hideKeyboard
import xyz.tetatet.vivaquiz.extensions.isKeyboardOpen
import xyz.tetatet.vivaquiz.extensions.setFullWidth
import xyz.tetatet.vivaquiz.io.model.fourquare.FoursquareResponse
import xyz.tetatet.vivaquiz.io.model.fourquare.Venue
import xyz.tetatet.vivaquiz.io.model.viva.Product
import xyz.tetatet.vivaquiz.ui.application.BaseApplication
import xyz.tetatet.vivaquiz.ui.base.BaseActivity
import xyz.tetatet.vivaquiz.ui.main.adapter.CategoriesAdapter
import xyz.tetatet.vivaquiz.ui.main.adapter.GooglePredictionsAdapter
import xyz.tetatet.vivaquiz.ui.main.adapter.ProductsAdapter
import xyz.tetatet.vivaquiz.ui.main.adapter.SwipeToDeleteCallback
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity<MainView, MainPresenter>(), MainView,
    SwipeRefreshLayout.OnRefreshListener {
    /*Constants ------------------------------------------------------------------------*/
    companion object {
        const val TAG = "VivaQuiz:Main"

        @JvmStatic
        fun launch(context: Context) = launch(context, null)

        @JvmStatic
        private fun launch(context: Context, options: ActivityOptionsCompat?) {
            ActivityCompat.startActivity(context, Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }, options?.toBundle())
        }
    }

    override val layout: Int get() = R.layout.activity_main
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private var drawerIcon: Drawable? = null

    private var googlePredictionsAdapter: GooglePredictionsAdapter? = null
    private lateinit var placesClient: PlacesClient
    private var selectedAddress: Place? = null
    private var fulladdress: String? = null
    private var isSelection = false

    override fun initViews() {
        super.initViews()
        initRepos()
        setSupportActionBar(toolbarMain)
        setToolBarTitle(getString(R.string.app_name))
        swipeRefreshLayout.setOnRefreshListener(this@MainActivity)
        setProductsAdapter()
        GlobalScope.async {
            if (databaseController.getProducts().size > 0) {
                productsSuccess(databaseController.getProducts())
            } else {
                presenter?.getProducts()
            }
        }
        initDrawer()
        setThemeListener()
        setCategoriesListener()
        setFoursquareAdapter()
        setGoogleAdapter()
//        applyCategory(preferencesRepository.category)
    }

    private fun setToolBarTitle(title: String) {
        activityTitle.isVisible = true
        activityTitle.text = title
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                drawer.openDrawer(GravityCompat.END)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun injectDependencies() = DaggerMainComponent.builder()
        .applicationComponent((application as BaseApplication).getComponent())
        .mainModule(MainModule())
        .activityModule(ActivityModule(this))
        .build().inject(this)

    private fun initRepos() {
        presenter?.apply {
            isDarkThemeObserver.let {
                preferencesRepository.isDarkThemeLive.observe(this@MainActivity, it)
            }
            categoryNumberObserver.let {
                preferencesRepository.categoryNumberLive.observe(this@MainActivity, it)
            }
            productsResponse
                .apply {
                    data.observe(this@MainActivity, mProductsObserver)
                    networkState.observe(this@MainActivity, mProductsNetworkStateObserver)
                }
            foursquareResponse
                .apply {
                    data.observe(this@MainActivity, mFoursquareObserver)
                    networkState.observe(this@MainActivity, mFoursquareNetworkStateObserver)
                }
        }
    }

    private fun setProductsAdapter() {
        productsAdapter = ProductsAdapter().apply {
            selectionEvent
                .doOnNext { openDetailsActivity(it) }
                .doOnError { }
                .subscribe()
        }
        val swipeHandler =
            object : SwipeToDeleteCallback(this@MainActivity, R.drawable.ic_trash_white) {
                override fun onDelete(position: Int) {
                    productsAdapter.get(position)?.let {
                        GlobalScope.async { databaseController.removeSingleProduct(it) }
                        productsAdapter.removeItem(it)
                    }
                }
            }
        productsRecyclerView?.apply {
            adapter = productsAdapter
            swipeHandler.attachToMyRecyclerView(this)
        }
    }

    override fun showProgressLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun dismissProgressLoading() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun productsSuccess(products: MutableList<Product?>) {
        productsAdapter.set(products)
    }

    override fun onRefresh() {
        presenter?.getProducts()
    }
/* <**************> EXTRA FEATURES IMPLEMENTATIONS <**************> */

    private fun initDrawer() {
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer, toolbarMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        drawerIcon = ContextCompat.getDrawable(this, R.drawable.ic_drawer)
        actionBarDrawerToggle.setHomeAsUpIndicator(drawerIcon)
        actionBarDrawerToggle.syncState()
        actionBarDrawerToggle.toolbarNavigationClickListener = View.OnClickListener {
            messagesController.dismiss()
            drawer.openDrawer(GravityCompat.START)
        }
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(R.id.searchView))
        searchView.setFullWidth(windowManager)
//        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//        navigationView.setFullWidth(windowManager)
        backBtn.setOnClickListener {
            handleKeyboard {
                resetSearchView()
                drawer.closeDrawer(GravityCompat.END)
            }
        }
        cancelBtn?.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
        }
    }

    override fun applyTheme(isDarkTheme: Boolean) {
        when(isDarkTheme){
            true -> darkThemeSwitch.text = getString(R.string.disable_dark_theme)
            else -> darkThemeSwitch.text = getString(R.string.enable_dark_theme)
        }
        darkThemeSwitch.isChecked = isDarkTheme
    }

    private fun setThemeListener() {
        darkThemeSwitch.setOnCheckedChangeListener { _, checked -> preferencesRepository.isDarkTheme = checked }
    }

    override fun applyCategory(category: Int){
        when(category){
            1 -> radioGroup.check(R.id.radioRestaurants)
            2 -> radioGroup.check(R.id.radioEvents)
            3 -> radioGroup.check(R.id.radioPharmacies)
            else -> {}
        }
    }

    private fun setCategoriesListener() {
        radioGroup.setOnCheckedChangeListener { _, checked ->
            when(checked){
                R.id.radioRestaurants -> { preferencesRepository.category = 1}
                R.id.radioEvents -> { preferencesRepository.category = 2}
                R.id.radioPharmacies -> { preferencesRepository.category = 3 }
                else -> {}
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setFoursquareAdapter() {
        categoriesAdapter = CategoriesAdapter().apply {
            categoriesRecyclerView.adapter = this
            eventClick.doOnNext { position ->
//                for (pos in 0..categoriesAdapter.itemCount){
//                    if(pos!=position) categoriesAdapter.notifyItemChanged(pos)
//                }
            }.subscribe()
        }
        searchBar.setOnTouchListener{ _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (event.rawX >= searchBar.right - searchBar.compoundDrawables[2].bounds.width() - searchBar.paddingEnd) {
                    resetSearchView()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun setGoogleAdapter() {
        placesClient = Places.createClient(this)
        googlePredictionsAdapter = GooglePredictionsAdapter().apply {
            googlePredictionsRecycler.adapter = this
            placeSelection
                .doOnNext { selection -> fetchPlace(selection.first, selection.second) }
                .doOnError { }
                .subscribe()

        }
        compositeDisposable.add(
            RxTextView.textChanges(searchBar)
                .map(CharSequence::toString)
                .debounce(150, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { query ->
                    if (!query.isNullOrEmpty() && !isSelection) googleSearch(query)
                    else {
                        googlePredictionsAdapter?.clearData()
                        googleLabel.isVisible = false
                    }
                    isSelection = false
                }
        )
    }

    private fun resetSearchView(){
        categoriesAdapter.clearData()
        googlePredictionsAdapter?.clearData()
        searchBar.setText("")
        googleLabel.isVisible = false
    }

    private fun googleSearch(query: String) {
        val request: FindAutocompletePredictionsRequest =
            FindAutocompletePredictionsRequest.builder()
                .setSessionToken(AutocompleteSessionToken.newInstance())
                .setQuery(query)
                .build()
        placesClient
            .findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                val predictionsList = mutableListOf<AutocompletePrediction?>()
                response.autocompletePredictions.forEach { predictionsList.add(it) }
                googlePredictionsAdapter?.set(predictionsList)
                when {
                    predictionsList.size > 0 -> googleLabel.isVisible = true
                    else -> googleLabel.isVisible = false
                }
            }
            .addOnFailureListener {
                println(it)
            }
    }

    private fun fetchPlace(placeId: String, placeName: String) {
        googlePredictionsAdapter?.clearData()
        googleLabel.isVisible = false
        placesClient
            .fetchPlace(FetchPlaceRequest.builder(placeId, arrayListOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME, Place.Field.LAT_LNG)).build())
            .addOnSuccessListener { fetchPlaceResponse ->
                selectedAddress = fetchPlaceResponse.place
                fulladdress = placeName
                isSelection = true
                searchBar.setText(placeName)
                handleKeyboard {
                    selectedAddress?.latLng?.latitude?.let { lat ->
                        selectedAddress?.latLng?.longitude?.let { lng ->
                            getFoursquarePlaces(lat, lng)
                        }
                    }
                }
            }
    }

    private fun getFoursquarePlaces(lat: Double, lng: Double) {
        presenter?.getFoursquarePlaces("${lat},${lng}", getString(R.string.fouresquare_api_id), getString(R.string.fouresquare_api_secret),preferencesRepository.category)
    }

    override fun foursquareSuccess(foursquare: FoursquareResponse) {
        val allVenues = foursquare.response?.venues
        val categorizedList: MutableList<MutableList<Venue?>?> = mutableListOf()
        foursquare.response?.venues?.distinctBy { it?.categories?.get(0)?.name }
            ?.forEach { uniqueCategory ->
                val categoryList: MutableList<Venue?> = mutableListOf()
                allVenues?.forEach {
                    if (uniqueCategory?.categories?.get(0)?.name == it?.categories?.get(0)?.name) {
                        categoryList.add(
                            it
                        )
                    }
                }
                categorizedList.add(categoryList)
            }

        if (categorizedList.size > 0) {
            categoriesAdapter.apply {
                set(categorizedList.apply {
                    sortBy { it?.get(0)?.categories?.get(0)?.pluralName }
                })
            }
        }
    }

    private fun handleKeyboard(function: () -> Unit = {}) {
        if (isKeyboardOpen()) {
            hideKeyboard()
            Handler().postDelayed({ function() }, 200L)
        } else {
            function()
        }
    }

}