package xyz.tetatet.vivaquiz.io.repository

import kotlinx.coroutines.Deferred
import xyz.tetatet.vivaquiz.io.model.fourquare.FoursquareResponse

interface FoursquareRepository {

    fun getFoursquareLocationsAsync(latlng:String?, foursquareApiId:String?, foursquareApiSecret:String?,category: Int?): Deferred<FoursquareResponse>

}