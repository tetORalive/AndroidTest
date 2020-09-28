package xyz.tetatet.vivaquiz.io.repository.repos.foursquare

import android.content.Context
import kotlinx.coroutines.coroutineScope
import xyz.tetatet.vivaquiz.io.model.fourquare.FoursquareResponse
import xyz.tetatet.vivaquiz.io.repository.FoursquareRepository
import xyz.tetatet.vivaquiz.io.repository.base.repolivedata.CoroutineRepo

class FoursquareRepo(private val foursquareRepository: FoursquareRepository) : CoroutineRepo<FoursquareResponse, FoursquarePlacesArgs>() {


    override suspend fun api(context: Context, args: FoursquarePlacesArgs): FoursquareResponse? = coroutineScope {
        when (args) {
            is FoursquarePlacesFromLatLngArgs -> {
                foursquareRepository.getFoursquareLocationsAsync(args.latlng,args.foursquareApiId,args.foursquareApiSecret,args.category).await().let { response ->
                    when (response.meta?.code) {
                        "200" -> response
                        else -> throw IllegalArgumentException(response.meta?.error)
                    }
                }
            }
            else -> throw IllegalArgumentException("Illegal argument for BaseOrdersArgs")
        }

    }
}