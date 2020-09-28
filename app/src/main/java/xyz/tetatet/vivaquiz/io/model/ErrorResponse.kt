package xyz.tetatet.vivaquiz.io.model

import com.google.gson.annotations.Expose

open class ErrorResponse(
        @Expose val status: String? = null,
        @Expose val error: String? = null,
        @Expose val field: String? = null,
        @Expose val message: String? = null
) {

    val isSuccess: Boolean get() = status == "success"
}