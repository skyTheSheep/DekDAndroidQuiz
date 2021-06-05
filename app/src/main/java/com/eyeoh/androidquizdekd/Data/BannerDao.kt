package com.eyeoh.androidquizdekd.Data

import android.os.Parcel
import android.os.Parcelable

data class BannerDao(
    var id: Int,
    var imageUrl: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString()
    )

    constructor(json: BaseJSONObject) : this(0) {
        id = json.getInt("id", 0)
        imageUrl = json.getString("image_url","") + "?rand=" + id
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BannerDao> {
        override fun createFromParcel(parcel: Parcel): BannerDao {
            return BannerDao(parcel)
        }

        override fun newArray(size: Int): Array<BannerDao?> {
            return arrayOfNulls(size)
        }
    }

}