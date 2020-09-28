package xyz.tetatet.vivaquiz.io.model.viva

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductsResponse(

    @SerializedName("status")
    @Expose val status: String? = null,

) : Parcelable {}

@Entity
@Parcelize
data class Product(

    @PrimaryKey
    @SerializedName("Id")
    @Expose val id: Int? = null,

    @SerializedName("Name")
    @Expose val name: String? = null,

    @SerializedName("Price")
    @Expose val price: String? = null,

    @SerializedName("Thumbnail")
    @Expose val thumbnail: String? = null,

    @SerializedName("Image")
    @Expose val image: String? = null,

    @SerializedName("Description")
    @Expose val description: String? = null,

) : Parcelable {}




