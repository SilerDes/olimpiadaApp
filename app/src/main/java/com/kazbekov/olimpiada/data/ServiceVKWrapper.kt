package com.kazbekov.olimpiada.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServiceVKWrapper(
    val items: List<ServiceVK>
)
