package com.demo.wallpaper.ui.list_image.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.demo.wallpaper.MultipleWidthPreview
import com.demo.wallpaper.ui.theme.WallpaperApplicationTheme

@Composable
fun TitleSection(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
    )
}

@MultipleWidthPreview
@Composable
fun PreviewTitle() {
    WallpaperApplicationTheme {
        TitleSection(title = "Hello")
    }
}
