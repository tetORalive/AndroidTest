package xyz.tetatet.vivaquiz.io.network

import kotlinx.coroutines.Deferred
import retrofit2.http.*
import xyz.tetatet.vivaquiz.io.model.fourquare.FoursquareResponse
import xyz.tetatet.vivaquiz.io.model.viva.Product

interface ApiInterface {
    companion object {
        const val CLIENT_ID = "1AIKGLZDDSKP2ATZZRUTI101O3X1W3ULIGVUFNTHP0GRBX3A"
        const val CLIENT_SECRET = "5H02JHFJ2XKHJLDRUSO0AZD5CZ2PEMCF4MS4SY2YUV3QZMFJ"
        const val VERSION = "20180323"
        const val RADIUS = "1500"
        const val LIMIT = "50"
        const val CATEGORY_ID_1 = "4d4b7105d754a06374d81259"
        const val CATEGORY_ID_2 = "4bf58dd8d48988d181941735"
        const val CATEGORY_ID_3 = "4bf58dd8d48988d10f951735"
    }

    /*Title ------------ Viva Wallet ------------ */
    @GET("v1/api/products")
    fun getProductsAsync(): Deferred<MutableList<Product?>?>

//    @GET("testJson.php")
//    fun getProductsAsync(): Deferred<MutableList<Product?>?>

    /*Title ------------ Foursquare ------------ */
    @GET("v2/venues/search")
    fun getFoursquareLocationsAsync(@Query("client_id") id: String?,
                                    @Query("client_secret") secret: String?,
                                    @Query("v") version: String?,
                                    @Query("ll") latlng: String?,
                                    @Query("radius") radius: String?,
                                    @Query("limit") limit: String?,
                                    @Query("categoryId") categoryId: String?): Deferred<FoursquareResponse>


}
