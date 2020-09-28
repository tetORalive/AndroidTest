package xyz.tetatet.vivaquiz.io.network

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class DeferredCallAdapterFactory private constructor() : CallAdapter.Factory() {

    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke() = DeferredCallAdapterFactory()
    }

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {

        if (Deferred::class.java != getRawType(returnType)) return null

        if (returnType !is ParameterizedType)
            throw IllegalStateException("Deferred return type must be parameterized as Deferred<Foo> or Deferred<out Foo>")

        val responseType = getParameterUpperBound(0, returnType)

        return when {
            returnType.actualTypeArguments.isNotEmpty() && returnType.actualTypeArguments[0] == Unit::class.java ->
                UnitCallAdapter<Unit>(responseType)

            returnType.actualTypeArguments.isNotEmpty() && returnType.actualTypeArguments[0] == Void::class.java ->
                VoidCallAdapter<Void>(responseType)

            else -> when (getRawType(responseType)) {
                Response::class.java -> {
                    if (responseType !is ParameterizedType)
                        throw IllegalStateException("Response must be parameterized as Response<Foo> or Response<out Foo>")

                    ResponseCallAdapter<Any>(getParameterUpperBound(0, responseType))
                }
                else -> BodyCallAdapter<Any>(responseType)
            }
        }
    }

    private class BodyCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Deferred<T?>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<T>): Deferred<T?> {
            val deferred = CompletableDeferred<T?>()

            deferred.invokeOnCompletion { if (deferred.isCancelled) call.cancel() }

            call.enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    deferred.completeExceptionally(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    when {
                        response.isSuccessful -> deferred.complete(response.body())
                        else -> deferred.completeExceptionally(HttpException(response))
                    }
                }
            })

            return deferred
        }
    }

    private class UnitCallAdapter<Unit>(private val responseType: Type) : CallAdapter<Unit, Deferred<Any?>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<Unit>): Deferred<Any?> {
            val deferred = CompletableDeferred<Any?>()

            deferred.invokeOnCompletion { if (deferred.isCancelled) call.cancel() }

            call.enqueue(object : Callback<Unit> {
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    deferred.completeExceptionally(t)
                }

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    when {
                        response.isSuccessful -> deferred.complete(null)
                        else -> deferred.completeExceptionally(HttpException(response))
                    }
                }
            })

            return deferred
        }
    }

    private class AnyCallAdapter<Any>(private val responseType: Type) : CallAdapter<Any, Deferred<Any?>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<Any>): Deferred<Any?> {
            val deferred = CompletableDeferred<Any?>()

            deferred.invokeOnCompletion { if (deferred.isCancelled) call.cancel() }

            call.enqueue(object : Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    deferred.completeExceptionally(t)
                }

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    when {
                        response.isSuccessful -> when {
                            response.body() == null -> deferred.complete(null)
                            else -> deferred.complete(response.body())
                        }
                        else -> deferred.completeExceptionally(HttpException(response))
                    }
                }
            })

            return deferred
        }
    }

    private class VoidCallAdapter<Void>(private val responseType: Type) : CallAdapter<Void, Deferred<Any?>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<Void>): Deferred<Any?> {
            val deferred = CompletableDeferred<Any?>()

            deferred.invokeOnCompletion { if (deferred.isCancelled) call.cancel() }

            call.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    deferred.completeExceptionally(t)
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    when {
                        response.isSuccessful -> deferred.complete(null)
                        else -> deferred.completeExceptionally(HttpException(response))
                    }
                }
            })

            return deferred
        }
    }

    private class ResponseCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Deferred<Response<T>>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<T>): Deferred<Response<T>> {
            val deferred = CompletableDeferred<Response<T>>()

            deferred.invokeOnCompletion { if (deferred.isCancelled) call.cancel() }

            call.enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    deferred.completeExceptionally(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    deferred.complete(response)
                }
            })

            return deferred
        }
    }
}