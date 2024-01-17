package com.demo.wallpaper.data.network.model

import com.demo.wallpaper.data.model.GiphyGif
import com.google.gson.annotations.SerializedName

data class NetworkGiphyGif(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("images") val images: NetworkGiphyImage,
)

fun NetworkGiphyGif.asExternalModel() = GiphyGif(
    id = id,
    images = images.asExternalModel()
)
