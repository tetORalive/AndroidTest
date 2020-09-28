package  xyz.tetatet.vivaquiz.io.repository.base.repolivedata

import android.content.Context
import androidx.lifecycle.MutableLiveData
import xyz.tetatet.vivaquiz.io.repository.base.NetworkState

abstract class Repo<D, A> {

    private var mRetry: (() -> Any)? = null
    private val mlData = MutableLiveData<D>()
    private val mlNetworkState = MutableLiveData<NetworkState<D, A>>()

    val mResponse: RepoResponse<D, A> by lazy {
        RepoResponse(mlData, mlNetworkState, { retry() }, { onViewModelCleared() })
    }

    abstract fun request(context: Context, args: A)

    protected abstract fun onViewModelCleared()

    protected open fun notifyStart(args: A) = mlNetworkState.postValue(NetworkState.loading(args))

    protected open fun notifySuccess(result: D?, args: A) {
        mRetry = null
        mlData.postValue(result)
        mlNetworkState.postValue(NetworkState.success(result, args))
    }

    protected open fun notifyFail(context: Context, throwable: Throwable?, args: A) {
        mRetry = { request(context, args) }
        mlNetworkState.postValue(NetworkState.failed(throwable, args))
    }

    private fun retry() {
        val prevRetry = mRetry
        mRetry = null
        prevRetry?.invoke()
    }

}