package  xyz.tetatet.vivaquiz.ui.base

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BasePresenterImpl<T : BaseView> : ViewModel(), BasePresenter<T>, LifecycleObserver {

    private var lifecycle: Lifecycle? = null
    var mvpView: T? = null
    val compositeDisposable: CompositeDisposable get() = CompositeDisposable()

    override fun attachView(mvpView: T) {
        this.mvpView = mvpView
    }

    override fun detachView() {
        detachActivityView()
    }

    override fun registerLifecycle(mvpView: T, lifecycle: Lifecycle) {
        attachView(mvpView)
        lifecycle.addObserver(this)
        this.lifecycle = lifecycle
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detachActivityView() {
        mvpView = null
        if (!compositeDisposable.isDisposed) compositeDisposable.clear()
        lifecycle?.removeObserver(this)
    }

    private fun isViewAttached(): Boolean = mvpView != null

    fun getView(): T? = when {
        isViewAttached() -> mvpView
        else -> {
            Log.d("MVPVIEW", MvpViewNotAttachedException().message ?: "")
            null
        }
    }

    private class MvpViewNotAttachedException internal constructor() :
        RuntimeException("Please call Presenter.attachView(MvpView) before" + " requesting data to the Presenter")
}