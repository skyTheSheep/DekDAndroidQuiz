package com.eyeoh.androidquizdekd.Data

import android.os.Parcel
import android.os.Parcelable

data class ImageUrlDao(
        var thumb: String = "",
        var cover_image: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!)

    constructor(json: BaseJSONObject, id: Int) : this("") {
        thumb = json.getString("thumb","") + "?rand=" + id
        cover_image = json.getString("cover_image","") + "?rand=" + id
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(thumb)
        parcel.writeString(cover_image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageUrlDao> {
        override fun createFromParcel(parcel: Parcel): ImageUrlDao {
            return ImageUrlDao(parcel)
        }

        override fun newArray(size: Int): Array<ImageUrlDao?> {
            return arrayOfNulls(size)
        }
    }
}