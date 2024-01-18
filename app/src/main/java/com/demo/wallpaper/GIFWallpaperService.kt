package com.demo.wallpaper

import android.graphics.Movie
import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.util.Log
import android.view.SurfaceHolder
import java.net.URL

class GIFWallpaperService(
//    val url: String
) : WallpaperService() {
    override fun onCreateEngine(): Engine? {
        return try {
            val movie = Movie.decodeStream(
//                URL(url).openStream()
                URL("https://media1.giphy.com/media/33zX3zllJBGY8/giphy.gif?cid=9eb124f18qbdxco9da2u9h43aawsudud3wsdwiffnlz0xxhd&ep=v1_gifs_search&rid=giphy.gif&ct=g").openStream()
            )
            GIFWallpaperEngine(movie)
        } catch (e: Exception) {
            Log.d("GIFWallpaperService", "Could not load asset");
            null
        }finally {

        }
    }

    private inner class GIFWallpaperEngine(val movie: Movie) : WallpaperService.Engine() {

        //        A Handler allows you to send and process Message and Runnable objects associated
//        with a thread's MessageQueue
        private val handler = Handler(Looper.getMainLooper())
        private val drawGIF = Runnable { draw() }

        //    The holder to hold the pixels
        private var holder: SurfaceHolder? = null
        private val frameDuration = 20
        private var isVisible = false

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            holder = surfaceHolder
        }

        override fun onVisibilityChanged(visible: Boolean) {
//        Called when the visibility of the view or an ancestor of the view
            isVisible = visible
            if (visible) {
                handler.post(drawGIF)
            } else {
                handler.removeCallbacks(drawGIF)
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder?) {
            super.onSurfaceDestroyed(holder)
            handler.removeCallbacks(drawGIF)
        }

        override fun onDestroy() {
            super.onDestroy()
            handler.removeCallbacks(drawGIF)
        }

        fun draw() {
            if (isVisible) {
                holder?.let { holder ->
//                A Canvas to host the draw calls (writing into the holder)
                    val canvas = holder.lockCanvas().apply {
                        save()
                        // Adjust size and position so that
                        // the image looks good on your screen
                        scale(1f, 1f)
                    }
                    movie.draw(canvas, 0f, 0f)
                    canvas.restore()
                    holder.unlockCanvasAndPost(canvas)
                    movie.setTime((System.currentTimeMillis() % movie.duration()).toInt())
                    handler.removeCallbacks(drawGIF, frameDuration)
                }
            }
        }
    }
}