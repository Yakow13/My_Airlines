package com.doubleyu.myairlines

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Airline (
        @SerializedName("code") val code: String,
        @SerializedName("defaultName") val defaultName: String,
        @SerializedName("logoURL") val logoURL: String,
        @SerializedName("name") val name: String,
        @SerializedName("phone") val phone: String,
        @SerializedName("site") val site: String,
        @SerializedName("usName") val usName: String ) : Parcelable {

	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(code)
		parcel.writeString(defaultName)
		parcel.writeString(logoURL)
		parcel.writeString(name)
		parcel.writeString(phone)
		parcel.writeString(site)
		parcel.writeString(usName)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Airline> {
		override fun createFromParcel(parcel: Parcel): Airline {
			return Airline(parcel)
		}

		override fun newArray(size: Int): Array<Airline?> {
			return arrayOfNulls(size)
		}
	}
}


