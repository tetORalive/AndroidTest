package  xyz.tetatet.vivaquiz.io.repository.base.repolivedata

import android.content.Context
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class CoroutineRepo<D, A> : Repo<D, A>(), CoroutineScope {

    private val mJob: Job = Job()
    override val coroutineContext: CoroutineContext = mJob + Dispatchers.IO

    protected abstract suspend fun api(context: Context, args: A): D?

    override fun request(context: Context, args: A) {
        launch {
            withContext(Dispatchers.Main) { notifyStart(args) }
            try {
                val result = api(context, args)
                withContext(Dispatchers.Main) { notifySuccess(result, args) }
            } catch (e: Throwable) {
                withContext(Dispatchers.Main) { notifyFail(context, e, args) }
            }
        }
    }

    override fun onViewModelCleared() = mJob.cancel()
}