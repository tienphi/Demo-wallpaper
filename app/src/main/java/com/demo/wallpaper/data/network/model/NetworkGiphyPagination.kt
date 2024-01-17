package com.demo.wallpaper.data.network.model

import com.demo.wallpaper.data.model.GiphyPagination
import com.google.gson.annotations.SerializedName

data class NetworkGiphyPagination(
    @field:SerializedName("offset") val offset: Int,
    @field:SerializedName("count") val count: Int,
    @field:SerializedName("total_count") val totalCount: Int,
)

fun NetworkGiphyPagination.asExternalModel() = GiphyPagination(
    offset = offset,
    count = count,
    totalCount = totalCount
)
