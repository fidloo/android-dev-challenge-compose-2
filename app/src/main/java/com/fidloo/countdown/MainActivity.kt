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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fidloo.countdown.ui.theme.BarsTheming
import com.fidloo.countdown.ui.theme.CountdownAppTheme

class MainActivity : AppCompatActivity() {

    private val countdownViewModel: CountdownViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CountdownAppTheme {
                BarsTheming(window)
                CountDownApp(countdownViewModel)
            }
        }
    }
}

@Composable
fun CountDownApp(countdownViewModel: CountdownViewModel) {
    Surface(color = MaterialTheme.colors.background) {
        AppContent(countdownViewModel)
    }
}

@Composable
fun AppContent(countdownViewModel: CountdownViewModel) {
    val scrollState = rememberScrollState()
    val duration: TimerDuration by countdownViewModel.timerDuration.observeAsState(TimerDuration())
    val ticking: Boolean by countdownViewModel.ticking.observeAsState(false)
    val progress: Float by countdownViewModel.progress.observeAsState(0f)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .scrollable(scrollState, Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Countdown", style = MaterialTheme.typography.h6)
        Spacer(Modifier.size(48.dp))
        DurationSelector(duration)
        Spacer(Modifier.size(48.dp))
        Divider()

        if (ticking) {
            Spacer(Modifier.size(48.dp))
            if (progress != 0f) {
                CircularProgressIndicator(
                    modifier = Modifier.size(200.dp),
                    progress = progress,
                    color = MaterialTheme.colors.primary,
                    strokeWidth = 120.dp,
                )
            } else {
                Text("Time's up\n", style = MaterialTheme.typography.h6)
            }
            Spacer(Modifier.size(48.dp))
        } else {
            Spacer(Modifier.size(16.dp))
            Keyboard(
                onDigitClick = { countdownViewModel.appendToDuration(it) },
                onBackClicked = { countdownViewModel.back() }
            )
        }
        BottomActions(
            duration,
            ticking,
            onStartClick = { countdownViewModel.startTimer() },
            onStopClick = { countdownViewModel.stopTimer() },
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomActions(
    duration: TimerDuration,
    ticking: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
) {
    if (ticking) {
        FloatingActionButton(onClick = onStopClick) {
            Icon(
                imageVector = Icons.Outlined.Stop,
                contentDescription = "Stop Timer",
                modifier = Modifier.size(24.dp)
            )
        }
    } else {
        AnimatedVisibility(
            visible = duration.isSet(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FloatingActionButton(onClick = onStartClick) {
                Icon(
                    imageVector = Icons.Outlined.PlayArrow,
                    contentDescription = "Start Timer",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    CountdownAppTheme {
        CountDownApp(countdownViewModel = CountdownViewModel())
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    CountdownAppTheme(darkTheme = true) {
        CountDownApp(countdownViewModel = CountdownViewModel())
    }
}
