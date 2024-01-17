package com.demo.wallpaper.data.network.model

import com.demo.wallpaper.data.model.GiphyImage
import com.google.gson.annotations.SerializedName

data class NetworkGiphyImage(
    @field:SerializedName("fixed_width") val fixedHeight: NetworkGiphyRendition,
    @field:SerializedName("original") val original: NetworkGiphyRendition,
)

fun NetworkGiphyImage.asExternalModel() = GiphyImage(
    fixedHeight = fixedHeight.asExternalModel(),
    original = original.asExternalModel()
)
