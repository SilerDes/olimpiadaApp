package com.kazbekov.olimpiada.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ServiceVK(
    val name: String,
    val description: String,
    @Json(name = "icon_url")
    val iconUrl: String,
    @Json(name = "service_url")
    val serviceUrl: String
) : Parcelable
