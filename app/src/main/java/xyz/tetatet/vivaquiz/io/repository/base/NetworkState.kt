package  xyz.tetatet.vivaquiz.io.repository.base

enum class NetworkStatus {
    LOADING,
    SUCCESS,
    FAILED
}

data class NetworkState<D, A>(
        val status: NetworkStatus,
        val response: D? = null,
        val requestArgs: A? = null,
        val throwable: Throwable? = null
) {

    val inProgress: Boolean get() = status == NetworkStatus.LOADING

    companion object {
        fun <D, A> loading(requestArgs: A) =
                NetworkState<D, A>(NetworkStatus.LOADING, requestArgs = requestArgs)

        fun <D, A> success(response: D?, requestArgs: A): NetworkState<D, A> =
                NetworkState(NetworkStatus.SUCCESS, response = response, requestArgs = requestArgs)

        fun <D, A> failed(throwable: Throwable?, requestArgs: A) =
                NetworkState<D, A>(NetworkStatus.FAILED, requestArgs = requestArgs, throwable = throwable)
    }
}