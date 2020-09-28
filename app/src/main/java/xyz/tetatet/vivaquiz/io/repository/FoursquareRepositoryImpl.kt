package xyz.tetatet.vivaquiz.io.repository


import kotlinx.coroutines.Deferred
import xyz.tetatet.vivaquiz.io.model.fourquare.FoursquareResponse
import xyz.tetatet.vivaquiz.io.network.ApiInterface
import xyz.tetatet.vivaquiz.io.network.ApiInterface.Companion.CATEGORY_ID_1
import xyz.tetatet.vivaquiz.io.network.ApiInterface.Companion.CATEGORY_ID_2
import xyz.tetatet.vivaquiz.io.network.ApiInterface.Companion.CATEGORY_ID_3
import xyz.tetatet.vivaquiz.io.network.ApiInterface.Companion.LIMIT
import xyz.tetatet.vivaquiz.io.network.ApiInterface.Companion.RADIUS
import xyz.tetatet.vivaquiz.io.network.ApiInterface.Companion.VERSION
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class FoursquareRepositoryImpl @Inject constructor(private val apiInterface: ApiInterface) : FoursquareRepository {

    fun getCategory(category: Int?):String?{
        when(category){
            1-> return CATEGORY_ID_1
            2-> return CATEGORY_ID_2
            3-> return CATEGORY_ID_3
            else -> return CATEGORY_ID_1
        }
    }

    override fun getFoursquareLocationsAsync(latlng:String?, foursquareApiId:String?, foursquareApiSecret:String?,category: Int?): Deferred<FoursquareResponse> =
            apiInterface.getFoursquareLocationsAsync(foursquareApiId, foursquareApiSecret, VERSION, latlng, RADIUS, LIMIT, getCategory(category))

}

