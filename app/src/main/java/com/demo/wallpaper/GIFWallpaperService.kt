package com.demo.wallpaper

import android.graphics.Canvas
import android.graphics.Movie
import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.util.Log
import android.view.SurfaceHolder

class GIFWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine? {

        return try {
            GIFWallpaperEngine()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(GIFWallpaperService::class.java.simpleName, "Error creating GIFWallpaperEngine")
            stopSelf()
            null
        }
    }

    private inner class GIFWallpaperEngine : WallpaperService.Engine() {

        // A Handler allows you to send and process Message and Runnable objects associated
        // with a thread's MessageQueue
        private var movie: Movie? = null
        private val handler = Handler(Looper.getMainLooper())
        private val drawGIF = Runnable { draw() }

        // The holder to hold the pixels
        private var holder: SurfaceHolder? = null
        private val frameDuration = 20
        private var isVisible = false

        private var scaleX: Float? = null
        private var scaleY: Float? = null

        init {
            val inputStream = resources.openRawResource(R.raw.unicorn)
            inputStream.use { stream ->
                movie = Movie.decodeStream(stream)
            }
        }

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            holder = surfaceHolder
        }

        override fun onVisibilityChanged(visible: Boolean) {
            //  Called when the visibility of the view or an ancestor of the view
            isVisible = visible
            if (visible) {
                handler.post(drawGIF)
            } else {
                handler.removeCallbacks(drawGIF)
            }
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder?,
            format: Int,
            width: Int,
            height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)
            movie?.let { movie->
                scaleX = width / (1f * movie.width())
                scaleY = height / (1f * movie.height())
                draw()
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder?) {
            super.onSurfaceDestroyed(holder)
            isVisible = false
            handler.removeCallbacks(drawGIF)
        }

        override fun onDestroy() {
            super.onDestroy()
            handler.removeCallbacks(drawGIF)
        }

        fun draw() {
            if (isVisible) {
                holder?.let { holder ->
                    // A Canvas to host the draw calls (writing into the holder)
                    var canvas: Canvas? = null
                    try {
                        canvas = holder.lockCanvas().apply {
                            save()
                            movie?.let { movie ->
                                // Adjust size and position so that
                                // the image looks good on your screen
                                scale(scaleX!!, scaleY!!)
                                movie.draw(this, 0f, 0f)
                                restore()
                                movie.setTime((System.currentTimeMillis() % movie.duration()).toInt())
                            }
                        }
                    } finally {
                        canvas?.let { holder.unlockCanvasAndPost(it) }
                    }

                    handler.removeCallbacks(drawGIF)

                    if (isVisible) {
                        handler.postDelayed(drawGIF, frameDuration.toLong())
                    }
                }
            }
        }
    }
}