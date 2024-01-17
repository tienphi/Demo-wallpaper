package com.demo.wallpaper.data.network.model

import com.google.gson.annotations.SerializedName

data class GiphySearchResponse(
    @field:SerializedName("data") val data: List<NetworkGiphyGif>,
    @field:SerializedName("pagination") val pagination: NetworkGiphyPagination,
)
