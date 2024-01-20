package com.demo.wallpaper

import android.graphics.Canvas
import android.graphics.Movie
import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.util.Log
import android.view.SurfaceHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.net.URL

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

        private var gifRatio: Float? = null
        private var gifWidth: Float? = null
        private var gifHeight: Float? = null
        private var surfaceRatio: Float? = null
        private var surfaceWidth: Float? = null
        private var surfaceHeight: Float? = null

        // To scale gif image
        private val scale: Float
            get() = try {
                if (gifRatio!! < surfaceRatio!!) {
                    surfaceHeight!! / gifHeight!!
                } else {
                    surfaceWidth!! / gifWidth!!
                }
            } catch (e: Exception) {
                e.printStackTrace()
                1f
            }

        // Top-left x-coordinate value
        private val posX: Float
            get() = try {
                if (gifRatio!! < surfaceRatio!!) {
                    (surfaceWidth!! - gifWidth!! * scale) / 2
                } else {
                    0f
                }
            } catch (e: Exception) {
                e.printStackTrace()
                0f
            }

        // Top-left y-coordinate value
        private val posY: Float
            get() = try {
                if (gifRatio!! < surfaceRatio!!) {
                    0f
                } else {
                    (surfaceHeight!! - gifHeight!! * scale) / 2
                }
            } catch (e: Exception) {
                e.printStackTrace()
                0f
            }

        init {
//            val inputStream = resources.openRawResource(R.raw.unicorn)
            val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

            scope.launch {
                val inputStream = URL(
                    "https://media1.giphy.com/media/33zX3zllJBGY8/giphy.gif" +
                            "?cid=9eb124f18qbdxco9da2u9h43aawsudud3wsdwiffnlz0xxhd&ep=v1_gifs_search&rid=giphy.gif&ct=g"
                ).openStream()

                inputStream.use { stream ->
                    movie = Movie.decodeStream(stream)
                    movie?.let {
                        gifWidth = it.width().toFloat()
                        gifHeight = it.height().toFloat()
                        gifRatio = it.width().toFloat() / it.height()
                    }
                }
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
            holder?.let {
                surfaceWidth = width.toFloat()
                surfaceHeight = height.toFloat()
                surfaceRatio = width.toFloat() / height
            }
            movie?.let { draw() }
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
//                                scale(scale, scale)
//                                movie.draw(this, posX, posY)
                                scale(surfaceWidth!! / gifWidth!!, surfaceHeight!! / gifHeight!!)
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