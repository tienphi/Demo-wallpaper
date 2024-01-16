/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demo.wallpaper.ui.home

import com.demo.wallpaper.ui.theme.WallpaperApplicationTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.demo.wallpaper.MultipleWidthPreview

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is HomeUiState.Success) {
        HomeScreen(
            items = (items as HomeUiState.Success).data,
            onSave = viewModel::addHome,
            modifier = modifier
        )
    }
}

@Composable
internal fun HomeScreen(
    items: List<String>,
    onSave: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        var nameHome by remember { mutableStateOf("Compose") }
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = nameHome,
                onValueChange = { nameHome = it }
            )

            Button(modifier = Modifier.width(96.dp), onClick = { onSave(nameHome) }) {
                Text("Save")
            }
        }
        items.forEach {
            Text("Saved item: $it")
        }
    }
}

// Previews

@MultipleWidthPreview
@Composable
private fun DefaultPreview() {
    WallpaperApplicationTheme {
        HomeScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
    }
}
