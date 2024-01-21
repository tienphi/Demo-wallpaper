package com.demo.wallpaper.ui.detail

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.OriginalSize
import com.demo.wallpaper.GIFWallpaperService
import com.demo.wallpaper.MultipleWidthPreview
import com.demo.wallpaper.data.local.GIF_URL
import com.demo.wallpaper.data.local.dataStore
import com.demo.wallpaper.ui.theme.WallpaperApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState: DetailUiState by viewModel.uiState.collectAsStateWithLifecycle()
//    val context = LocalContext.current
//    val wallpaperManager = WallpaperManager.getInstance(LocalContext.current)
//    val composableScope = rememberCoroutineScope()

    WallpaperApplicationTheme {
        when (uiState) {
            is DetailUiState.Error -> ErrorScreen(
                modifier = Modifier,
                msg = (uiState as DetailUiState.Error).msg
            )

            is DetailUiState.Loading -> LoadingScreen(modifier = Modifier)
            is DetailUiState.Success -> {
                DetailScreen(
                    modifier = modifier,
                    url = (uiState as DetailUiState.Success).url,
                    type = (uiState as DetailUiState.Success).type,
//                    onClickSetWallpaper = { url ->
//                        composableScope.launch {
//                            setWallpaper(context, wallpaperManager, url)
//                        }
//                    },
//                    onClickCropSetWallpaper = { url ->
//                        cropAndSetWallpaper(wallpaperManager, url)
//                    },
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(1.0f)
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(200.dp),
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Composable
private fun ErrorScreen(
    msg: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(1.0f)
            .padding(8.dp)
    ) {
        Text(
            text = msg,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun DetailScreen(
    url: String,
    type: String,
//    onClickSetWallpaper: (String) -> Unit,
//    onClickCropSetWallpaper: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build()
    )

    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .crossfade(true)
        .build()

    val painterGif = rememberImagePainter(
        imageLoader = imageLoader,
        data = url,
        builder = {
            size(OriginalSize)
        }
    )

    var choseWallpaper by remember {
        mutableStateOf(false)
    }

    val composableScope = rememberCoroutineScope()
    val wallpaperManager = WallpaperManager.getInstance(context)
    var wallpaperBitmap by remember { mutableStateOf<Bitmap?>(null) }

    if (choseWallpaper && type == TYPE_IMAGE) {
        LaunchedEffect(key1 = true) {
            val loadBitmap = loadBitmapFromUrl(url)
            wallpaperBitmap = loadBitmap
        }
    }

    if (type == TYPE_GIF) {
        LaunchedEffect(key1 = true) {
            context.dataStore.edit { settings ->
                settings[GIF_URL] = url
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            painter = if (type == TYPE_GIF) painterGif else painter,
            contentScale = ContentScale.Fit,
            contentDescription = ""
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.onBackground),
        ) {
            if (type == TYPE_GIF) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                            putExtra(
                                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                                ComponentName(context, GIFWallpaperService::class.java)
                            )
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS or Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Text(text = "Set gif as wallpaper")
                }
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
//                    onClickSetWallpaper(url)
                        choseWallpaper = true
                        composableScope.launch {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                try {
                                    if (wallpaperBitmap != null) {
                                        Log.d("DetailScreen", "Set wallpaper")
                                        wallpaperManager.setBitmap(
                                            wallpaperBitmap,
                                            Rect(10, 200, 50, 400),
                                            true,
                                            WallpaperManager.FLAG_SYSTEM
                                        )
                                        Toast.makeText(
                                            context,
                                            "Home Screen Wallpaper Updated Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        delay(1000)
                                        choseWallpaper = false
                                    }

                                } catch (e: Exception) {
                                    e.printStackTrace()

                                    Toast.makeText(
                                        context,
                                        "Unsuccessful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                )
                {
                    Text(text = "Set wallpaper")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
//                    onClickCropSetWallpaper(url)
                        choseWallpaper = true
                        composableScope.launch {
                            try {
                                if (wallpaperBitmap != null) {
                                    Log.d("DetailScreen", "Crop & Set wallpaper")
                                    context.startActivity(
                                        wallpaperManager.getCropAndSetWallpaperIntent(
                                            context.getImageUri(wallpaperBitmap)
                                        )
                                    )
                                    choseWallpaper = false
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()

                                Toast.makeText(
                                    context,
                                    "Unsuccessful",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                )
                {
                    Text(text = "Crop & set wallpaper")
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@MultipleWidthPreview
@Composable
fun DetailScreenPreview() {
    WallpaperApplicationTheme {
        DetailScreen(
            url = "",
            type = TYPE_IMAGE
//            onClickSetWallpaper = {},
//            onClickCropSetWallpaper = {}
        )
    }
}

@MultipleWidthPreview
@Composable
fun ErrorScreenPreview() {
    WallpaperApplicationTheme {
        ErrorScreen(msg = "Wrong")
    }
}

@MultipleWidthPreview
@Composable
fun LoadingScreenPreview() {
    WallpaperApplicationTheme {
        LoadingScreen()
    }
}

suspend fun setWallpaper(context: Context, wallpaperManager: WallpaperManager, url: String) {
    val loadBitmap = loadBitmapFromUrl(url)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        try {
            wallpaperManager.setBitmap(
                loadBitmap,
                null,
                true,
                WallpaperManager.FLAG_SYSTEM
            )
            Toast.makeText(
                context,
                "Home Screen Wallpaper Updated Successfully",
                Toast.LENGTH_SHORT
            ).show()
            delay(1000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else {
//        val intent = Intent(Intent.ACTION_SET_WALLPAPER)
//        val chooserIntent: Intent =
//            Intent.createChooser(intent = intent, title = ("Select Wallpaper" as CharSequence))
//        startActivity(chooserIntent)
    }
}

//fun cropAndSetWallpaper(wallpaperManager: WallpaperManager, url: String) {
//
//}

suspend fun loadBitmapFromUrl(imageUrl: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.connect()

            val inputStream = BufferedInputStream(connection.inputStream)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            connection.disconnect()
            inputStream.close()

            return@withContext bitmap
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

fun Context.getImageUri(bitmap: Bitmap?): Uri {
    val bytes = ByteArrayOutputStream()
    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(
        contentResolver, bitmap, System.currentTimeMillis().toString(), null
    )
    return Uri.parse(path)
}
