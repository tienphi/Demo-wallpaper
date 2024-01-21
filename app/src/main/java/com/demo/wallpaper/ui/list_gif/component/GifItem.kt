package com.demo.wallpaper.ui.list_gif.component

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.size.OriginalSize

@Composable
fun GifItem(
    modifier: Modifier = Modifier,
    url: String,
    onClickItem: () -> Unit
) {
    val context = LocalContext.current
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

    val painter2 = rememberAsyncImagePainter(
        model = imageLoader
    )

    val painter = rememberImagePainter(
        imageLoader = imageLoader,
        data = url,
        builder = {
            size(OriginalSize)
        }
    )

    Card(
        modifier = modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .size(width = 150.dp, height = 250.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        onClick = onClickItem,
        shape = MaterialTheme.shapes.medium
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )
    }
}
