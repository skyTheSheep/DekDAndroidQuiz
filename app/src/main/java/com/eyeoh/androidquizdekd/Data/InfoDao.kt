package com.eyeoh.androidquizdekd.Data

import android.os.Parcel
import android.os.Parcelable

data class InfoDao (
        var id: Int,
        var createdAt: String = "",
        var title: String = "",
        var description: String = "",
        var image_url: ImageUrlDao = ImageUrlDao()
        ) : Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readParcelable(ImageUrlDao::class.java.classLoader)!!
        )

        constructor(json: BaseJSONObject) : this(0) {
                id = json.getInt("id",0)
                createdAt = json.getString("createdAt","")
                title = json.getString("title","")
                description = json.getString("description","")
                image_url = ImageUrlDao(json.getBaseJSONObject("image_url"),json.getInt("id",0))

        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(id)
                parcel.writeString(createdAt)
                parcel.writeString(title)
                parcel.writeString(description)
                parcel.writeParcelable(image_url, flags)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<InfoDao> {
                override fun createFromParcel(parcel: Parcel): InfoDao {
                        return InfoDao(parcel)
                }

                override fun newArray(size: Int): Array<InfoDao?> {
                        return arrayOfNulls(size)
                }
        }
}