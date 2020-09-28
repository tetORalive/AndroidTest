package xyz.tetatet.vivaquiz.io.repository.repos.foursquare


sealed class FoursquareArgs
open class FoursquarePlacesArgs

class FoursquarePlacesFromLatLngArgs(
    val latlng: String?,
    val foursquareApiId: String?,
    val foursquareApiSecret: String?,
    val category: Int?
) : FoursquarePlacesArgs()

