package xyz.tetatet.vivaquiz.ui.product

import android.content.Context
import android.content.Intent
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_details.*
import xyz.tetatet.vivaquiz.R
import xyz.tetatet.vivaquiz.io.model.viva.Product
import xyz.tetatet.vivaquiz.ui.base.BaseActivity
import xyz.tetatet.vivaquiz.ui.base.BasePresenter
import xyz.tetatet.vivaquiz.ui.base.BaseView


class DetailsActivity : BaseActivity<BaseView, BasePresenter<BaseView>>() {

    /*Constants ------------------------------------------------------------------------*/
    companion object {
        const val TAG = "VivaQuiz:Details"
        const val EXTRA_PRODUCT = "extra_product"

        @JvmStatic
        fun launch(context: Context, product: Product?) = launch(context, product, null)

        @JvmStatic
        private fun launch(context: Context, product: Product?, options: ActivityOptionsCompat?) {
            ActivityCompat.startActivity(
                context,
                Intent(context, DetailsActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra(EXTRA_PRODUCT, product)
                }, options?.toBundle()
            )
        }
    }

    override val layout: Int get() = R.layout.activity_details

    override fun injectDependencies() {}

    override fun initViews() {
        super.initViews()
        val product = intent.getParcelableExtra<Product>(EXTRA_PRODUCT)

        Glide.with(this)
            .load(product?.image)
            .placeholder(R.drawable.ic_placeholder_rect)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .priority(Priority.HIGH)
            .centerCrop()
            .into(productImage)

        product?.name?.apply {
            productTitle.text = this.trim()
            productName.text = this.trim()
        }

        product?.price?.let {
            price.text = it.trim()
        }

        product?.description?.let {
            descriptionTitle.isVisible = true
            description.isVisible = true
            description.text = it.trim()
        }

        backBtn.setOnClickListener { onBackPressed() }
        cancelBtn.setOnClickListener { onBackPressed() }

        initMotionControls()
    }

    private fun initMotionControls() {
        val appBarLayout: AppBarLayout = findViewById(R.id.appbar_layout)
        val motionLayoutAppBar: MotionLayout = findViewById(R.id.motion_layout)
        val motionLayoutContent: MotionLayout = findViewById(R.id.motion_layout_content)
        val listener = AppBarLayout.OnOffsetChangedListener { unused, verticalOffset ->
            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            motionLayoutAppBar.progress = seekPosition
            motionLayoutContent.progress = seekPosition
        }
        appBarLayout.addOnOffsetChangedListener(listener)
    }
}