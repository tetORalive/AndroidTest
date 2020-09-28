package xyz.tetatet.vivaquiz.io.exceptions

import android.content.Context
import io.reactivex.exceptions.OnErrorNotImplementedException
import retrofit2.HttpException
import xyz.tetatet.vivaquiz.R
import xyz.tetatet.vivaquiz.io.interceptors.CheckConnectivityInterceptor
import java.net.*
import java.util.regex.Pattern
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException

object ApiExceptionsHandler {

    private val PATTERN_EXCEPTION: Pattern = "([\\w\\.]+)(:.*)?".toPattern()

    fun parseMessage(apiException: Throwable, context: Context): String? {

        return when (apiException) {

            //Comments -> retrofit
            is ErrorResponseException -> apiException.getMessage(context)
            is HttpException -> ErrorResponseException(apiException).getMessage(context)
                    ?: context.getString(R.string.error_service_common_message)

            //Comments -> no network
            is CheckConnectivityInterceptor.NoNetworkException -> context.getString(R.string.warning_offline)

            //Comments -> java.net
            is SocketException,
            is ConnectException,
            is SSLHandshakeException,
            is SocketTimeoutException,
            is ProtocolException,
            is UnknownServiceException,
            is UnknownHostException,
            is SSLException,
            is OnErrorNotImplementedException -> context.getString(R.string.error_service_common_message)

            //Comments -> other
            else -> {
                //Comments -> try to parse common message
                val msg: String? = if (!apiException.localizedMessage.isNullOrBlank())
                    apiException.localizedMessage else apiException.cause?.message
                if (!msg.isNullOrBlank()) PATTERN_EXCEPTION.matcher(msg)
                        .takeIf { it.find() }
                        ?.group(2)
                        ?.takeIf { it.isNotEmpty() }
                        ?.substring(1)
                        ?.trim() ?: msg
                else msg
            }
        }
    }
}