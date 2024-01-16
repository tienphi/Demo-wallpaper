package com.demo.wallpaper.data.network.model

import com.google.gson.annotations.SerializedName

data class UnsplashUser(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("username") val username: String
)
