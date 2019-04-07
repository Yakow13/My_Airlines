package com.doubleyu.myairlines

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Airline (
        @SerializedName("code") val code: String,
        @SerializedName("defaultName") val defaultName: String,
        @SerializedName("logoURL") val logoURL: String,
        @SerializedName("name") val name: String,
        @SerializedName("phone") val phone: String,
        @SerializedName("site") val site: String,
        @SerializedName("usName") val usName: String ) : Serializable


