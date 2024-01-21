package com.demo.wallpaper.ui.choose_type

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.demo.wallpaper.ui.theme.WallpaperApplicationTheme

@Composable
fun ChooseTypeScreen(
    modifier: Modifier = Modifier,
    onClickImage: () -> Unit,
    onClickGif: () -> Unit,
) {
    WallpaperApplicationTheme {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "What type of wallpaper do you want?")
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.width(200.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onClickImage
                ) {
                    Text(text = "Normal image")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onClickGif
                ) {
                    Text(text = "Gif")
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 980, heightDp = 360)
@Composable
fun ChooseTypeScreenPreview() {
    ChooseTypeScreen(
        onClickImage = {},
        onClickGif = {}
    )
}
