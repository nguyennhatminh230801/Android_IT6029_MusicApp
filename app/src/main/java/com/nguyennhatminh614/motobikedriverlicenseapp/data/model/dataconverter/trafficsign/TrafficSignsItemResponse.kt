package com.nguyennhatminh614.motobikedriverlicenseapp.data.model.dataconverter.trafficsign

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TrafficSignsItemResponse : Parcelable {
    var id = ""
    var title = ""
    var description = ""

    @SerializedName("image_url")
    @set:PropertyName("image_url")
    @get:PropertyName("image_url")
    var imageUrl = ""

    var category = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TrafficSignsItemResponse

        if (id != other.id) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (imageUrl != other.imageUrl) return false
        if (category != other.category) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + category.hashCode()
        return result
    }

    override fun toString(): String {
        return "TrafficSigns(id='$id', title='$title', description='$description', " +
                "imageUrl='$imageUrl', category='$category')"
    }
}


