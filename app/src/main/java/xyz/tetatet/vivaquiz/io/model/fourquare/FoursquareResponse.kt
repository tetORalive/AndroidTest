package xyz.tetatet.vivaquiz.io.model.fourquare

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class FoursquareResponse(
        @Expose val meta: Meta? = null,
        @Expose val response: Venues? = null
) :  Parcelable {}
@Parcelize
class Meta(
        @SerializedName("errorDetail") @Expose var error: String? = null,
        @Expose val code: String? = null
) : Parcelable
@Parcelize
class Venues(
        @Expose val venues: MutableList<Venue?>? = mutableListOf(),
        var isExpanded: Boolean = false
) : Parcelable
@Parcelize
class Venue(
        @Expose val id: String? = null,
        @Expose val name: String? = null,
        @Expose val location: Location? = null,
        @Expose val categories: MutableList<Category?>? = mutableListOf()
) : Parcelable
@Parcelize
class Location(
        @Expose val distance: Int? = null,
) : Parcelable
@Parcelize
class Category(
        @Expose val name: String? = null,
        @Expose val pluralName: String? = null,
        @Expose val icon: Icon? = null
) : Parcelable
@Parcelize
class Icon(
        @Expose val prefix: String? = null,
        @Expose val suffix: String? = null
) : Parcelable