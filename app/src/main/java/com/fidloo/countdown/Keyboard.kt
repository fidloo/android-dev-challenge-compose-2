/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fidloo.countdown

import androidx.annotation.IntRange
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Keyboard(onDigitClick: (Int) -> Unit, onBackClicked: () -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp),
        cells = GridCells.Fixed(3)
    ) {
        items(9) { index -> KeyboardKey(number = index + 1, onClick = { onDigitClick(index + 1) }) }
        item { // Empty space in the bottom left corner }
            item { KeyboardKey(number = 0, onClick = { onDigitClick(0) }) }
            item { KeyboardBackKey(onClick = onBackClicked) }
        }
    }
}

@Composable
fun KeyboardKey(@IntRange(from = 0, to = 9) number: Int, onClick: (Int) -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f, false)
            .clickable { onClick(number) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            modifier = Modifier,
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
        )
    }
}

@Composable
fun KeyboardBackKey(onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f, false),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = { onClick() },
            modifier = Modifier.fillMaxSize(),
        ) {
            Icon(
                imageVector = Icons.Default.Backspace,
                contentDescription = Icons.Default.Backspace.name,
            )
        }
    }
}
