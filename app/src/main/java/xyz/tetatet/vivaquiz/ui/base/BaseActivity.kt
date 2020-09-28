package xyz.tetatet.vivaquiz.ui.base

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import xyz.tetatet.vivaquiz.R
import xyz.tetatet.vivaquiz.controllers.messages.MessagesController
import xyz.tetatet.vivaquiz.io.exceptions.ApiExceptionsHandler
import xyz.tetatet.vivaquiz.controllers.shared.PreferencesRepository
import xyz.tetatet.vivaquiz.db.DatabaseController
import xyz.tetatet.vivaquiz.io.model.viva.Product
import xyz.tetatet.vivaquiz.ui.main.MainActivity
import xyz.tetatet.vivaquiz.ui.product.DetailsActivity
import xyz.tetatet.vivaquiz.ui.views.ProgressDialog
import javax.inject.Inject

abstract class BaseActivity<in V : BaseView, T : BasePresenter<V>>
    : AppCompatActivity(), BaseView, LifecycleOwner, LifecycleObserver {


    /*Constants ----------------------------------------------------------------*/
    companion object;

    /*Fields ----------------------------------------------------------------*/
    var handler: Handler? = null
    val compositeDisposable = CompositeDisposable()
    var presenter: T? = null
    var lifecycleRegistry: LifecycleRegistry? = null
    private val nightModeObserver = Observer<Int> {
        setAppTheme(it)
    }
    private var progressDialog: ProgressDialog? = null

    /*Injections ----------------------------------------------------------------*/

    @Inject
    fun setBasePresenter(presenter: T) {
        this.presenter = presenter
    }
    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    @Inject
    lateinit var databaseController: DatabaseController

    @Inject
    lateinit var messagesController: MessagesController

    /*Abstract/Open Functions ----------------------------------------------------------------*/

    abstract val layout: Int

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun initViews() = Unit

    abstract fun injectDependencies()

    /*Inner Functions ----------------------------------------------------------------*/
    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleRegistry = lifecycleRegistry ?: LifecycleRegistry(this)
        injectDependencies()
        super.onCreate(savedInstanceState)
        preferencesRepository = PreferencesRepository(this)
        nightModeObserver.let {
            preferencesRepository.nightModeLive.observe(this, it )
        }
        setAppTheme(preferencesRepository.nightMode)
        handler = Handler()
        progressDialog = ProgressDialog(this)
        initObservers()
        presenter?.registerLifecycle(this as V, lifecycle)
        if (layout != 0) setContentView(layout)
        lifecycleRegistry?.currentState = Lifecycle.State.CREATED
    }

    public override fun onStart() {
        super.onStart()
        lifecycleRegistry?.currentState = Lifecycle.State.STARTED
    }

    override fun onDestroy() {
        handler?.removeCallbacksAndMessages(null)
        super.onDestroy()
        lifecycleRegistry?.currentState = Lifecycle.State.DESTROYED
    }

    override fun onResume() {
        super.onResume()
        lifecycleRegistry?.currentState = Lifecycle.State.RESUMED
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry ?: LifecycleRegistry(this)

    /*Custom Functions ----------------------------------------------------------------*/
    open fun initObservers() {
        lifecycle.apply {
            addObserver(this@BaseActivity)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun removeObservers() {
        lifecycle.apply {
            removeObserver(this@BaseActivity)
        }
    }

    fun openMainActivity(){
        handler?.postDelayed({
            if (!isFinishing) {
                MainActivity.launch(applicationContext)
                supportFinishAfterTransition()
            }
        }, 2000)
    }

    fun openDetailsActivity(product: Product){
        if (!isFinishing) {
            DetailsActivity.launch(applicationContext,product)
        }
    }

    override fun showLoading() {
        progressDialog?.let {
            if (!it.isShowing) {
                progressDialog = ProgressDialog(this, getString(R.string.progress_loading))
                progressDialog?.apply {
                    setCancelable(false)
                    show()
                }
            }
        }
    }

    override fun dismissLoading() {
        progressDialog?.apply {
            if (isShowing) dismiss()
        }
    }

    private fun setAppTheme(theme: Int) {
        AppCompatDelegate.setDefaultNightMode(theme)
    }

    override fun handleExceptionRes(throwable: Throwable?, coordView: View?, action: Pair<Int, () -> Unit>?) {
        throwable?.run {
            ApiExceptionsHandler.apply {
                parseMessage(throwable, this@BaseActivity)
                    ?.let {
                        messagesController.showSnackBarMessage(
                            it,
                            coordView,
                            action?.let { Pair(getString(it.first), it.second) })
                    }
            }
        }
    }

    override fun handleExceptionRes(throwable: Throwable?, action: Pair<Int, () -> Unit>?) {
        handleExceptionRes(throwable, null, action)
    }

    override fun handleException(throwable: Throwable?, action: Pair<String, () -> Unit>?) {
        throwable?.run {
            ApiExceptionsHandler.apply {
                parseMessage(throwable, this@BaseActivity)
                    ?.let { messagesController.showSnackBarMessage(it, action) }
            }
        }
    }




}