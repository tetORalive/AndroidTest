package  xyz.tetatet.vivaquiz.io.repository.base.repolivedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import xyz.tetatet.vivaquiz.io.repository.base.NetworkState
import xyz.tetatet.vivaquiz.io.repository.base.NetworkStatus

data class RepoResponse<D, A>(
        /*Title -> The LiveData for the UI to observe*/
        val data: MutableLiveData<D>,

        /*Title -> Represents the network response status to show to the user*/
        val networkState: LiveData<NetworkState<D, A>>,

        /*Title -> Retries any failed requests*/
        val retry: () -> Unit,

        /*Title -> This method will be called when this ViewModel is no longer used and will be destroyed*/
        val viewModelCleared: () -> Unit
) {
    val isEmpty: Boolean
        get() = data.value == null

    val inProgress: Boolean
        get() = networkState.value?.status == NetworkStatus.LOADING
}