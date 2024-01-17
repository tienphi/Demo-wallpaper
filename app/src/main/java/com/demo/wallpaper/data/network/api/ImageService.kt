package com.demo.wallpaper.data.network.api

import com.demo.wallpaper.BuildConfig
import com.demo.wallpaper.data.network.model.GiphySearchResponse
import com.demo.wallpaper.data.network.model.UnsplashSearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface ImageService {

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): UnsplashSearchResponse

    @GET("v1/gifs/search")
    suspend fun searchGifs(
        @Query("api_key") clientId: String = BuildConfig.GIPHY_ACCESS_KEY,
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("rating") rating: String = "g",
        @Query("lang") lang: String = "en",
        @Query("bundle") bundle: String = "clips_grid_picker",
    ): GiphySearchResponse

    companion object {
        const val UNSPLASH_BASE_URL = "https://api.unsplash.com/"
        const val GIPHY_BASE_URL = "https://api.giphy.com/"

        fun create(baseUrl: String): ImageService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ImageService::class.java)
        }
    }
}
