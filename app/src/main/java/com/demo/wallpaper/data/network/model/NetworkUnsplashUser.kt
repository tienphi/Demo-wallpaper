package com.demo.wallpaper.data.network.model

import com.demo.wallpaper.data.model.UnsplashUser
import com.google.gson.annotations.SerializedName

data class NetworkUnsplashUser(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("username") val username: String
)

fun NetworkUnsplashUser.asExternalModel() = UnsplashUser(
    name = name,
    username = username
)
