package  xyz.tetatet.vivaquiz.ui.base

import androidx.lifecycle.Lifecycle


interface BasePresenter<in V : BaseView> {

    fun registerLifecycle(mvpView: V, lifecycle: Lifecycle)
    fun attachView(mvpView: V)
    fun detachView()

}