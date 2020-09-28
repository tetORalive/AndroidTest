package xyz.tetatet.vivaquiz.io.exceptions

import android.content.Context
import com.google.gson.Gson
import retrofit2.HttpException
import xyz.tetatet.vivaquiz.io.model.ErrorResponse

class ErrorResponseException(exception: HttpException) : HttpException(exception.response()) {

    private var statusResponse: String? = null
    private var messageResponse: String? = null

    init {
        try {
            with(Gson().fromJson(response()?.errorBody()?.string(), ErrorResponse::class.java)) {
                statusResponse = status
                messageResponse = error
            }
        } catch (ex: Exception) { }
    }
    fun getMessage(context: Context): String? = messageResponse
}