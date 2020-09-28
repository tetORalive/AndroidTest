package xyz.tetatet.vivaquiz.ui.splash


import android.view.View
import android.view.WindowManager
import xyz.tetatet.vivaquiz.R
import xyz.tetatet.vivaquiz.ui.base.BaseActivity
import xyz.tetatet.vivaquiz.ui.base.BasePresenter
import xyz.tetatet.vivaquiz.ui.base.BaseView


class SplashActivity : BaseActivity<BaseView, BasePresenter<BaseView>>(), BaseView {

    override fun initViews() {
        super.initViews()
        window.apply {
            setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
       openMainActivity()
    }

    override val layout: Int get() = R.layout.activity_splash

    override fun injectDependencies() {}

}