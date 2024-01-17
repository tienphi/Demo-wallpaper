package com.demo.wallpaper.data.network.model

import com.demo.wallpaper.data.model.GiphyRendition
import com.google.gson.annotations.SerializedName

data class NetworkGiphyRendition(
    @field:SerializedName("url") val url: String
)

fun NetworkGiphyRendition.asExternalModel() = GiphyRendition(
    url = url
)
