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

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DurationSelector(duration: TimerDuration) {
    Row {
        DurationValue(duration.hours, Modifier.alignByBaseline(), duration.isSet())
        DurationUnit("h", Modifier.alignByBaseline(), duration.isSet())
        Spacer(Modifier.size(8.dp))
        DurationValue(duration.minutes, Modifier.alignByBaseline(), duration.isSet())
        DurationUnit("min", Modifier.alignByBaseline(), duration.isSet())
        Spacer(Modifier.size(8.dp))
        DurationValue(duration.seconds, Modifier.alignByBaseline(), duration.isSet())
        DurationUnit("s", Modifier.alignByBaseline(), duration.isSet())
    }
}

@Composable
fun DurationValue(value: Int, modifier: Modifier, isSet: Boolean) {
    val textColor by animateColorAsState(
        if (isSet) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        }
    )
    Text(
        text = value.toString().padStart(2, '0'),
        style = MaterialTheme.typography.h3,
        modifier = modifier,
        color = textColor
    )
}

@Composable
fun DurationUnit(unit: String, modifier: Modifier, isSet: Boolean) {
    val textColor by animateColorAsState(
        if (isSet) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        }
    )
    Text(
        text = unit,
        style = MaterialTheme.typography.h6,
        fontSize = 20.sp,
        modifier = modifier,
        color = textColor
    )
}
