package xyz.tetatet.vivaquiz.io.network

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.tetatet.vivaquiz.BuildConfig
import xyz.tetatet.vivaquiz.io.interceptors.CheckConnectivityInterceptor
import java.util.concurrent.TimeUnit

open class ApiClient {

    companion object {
        private var vivaRetrofit: Retrofit? = null
        private var foursquareRetrofit: Retrofit? = null

        @JvmStatic
        private fun getHttpClient(context: Context): OkHttpClient.Builder {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient.Builder()
                .readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .addInterceptor(CheckConnectivityInterceptor(context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager))
                .addInterceptor(loggingInterceptor)
        }

        fun getVivaClient(context: Context): Retrofit {
            if (vivaRetrofit == null) {
                val httpClient = getHttpClient(context).build()
                val builder = GsonBuilder()
                builder.excludeFieldsWithoutExposeAnnotation()
                vivaRetrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.VIVA_API_URL)
                    .addCallAdapterFactory(DeferredCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create(builder.create()))
                    .client(httpClient)
                    .build()
            }
            return vivaRetrofit!!
        }

        fun getFoursquareClient(context: Context): Retrofit {
            if (foursquareRetrofit == null) {
                val httpClient = getHttpClient(context).build()
                val builder = GsonBuilder()
                builder.excludeFieldsWithoutExposeAnnotation()
                foursquareRetrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.FOURSQUARE_API_URL)
                    .addCallAdapterFactory(DeferredCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create(builder.create()))
                    .client(httpClient)
                    .build()
            }
            return foursquareRetrofit!!
        }
    }
}