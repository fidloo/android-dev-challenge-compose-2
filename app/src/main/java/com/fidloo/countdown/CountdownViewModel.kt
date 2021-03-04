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

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class CountdownViewModel : ViewModel() {

    private val _timerDuration = MutableLiveData(TimerDuration())
    val timerDuration: LiveData<TimerDuration> = _timerDuration

    private val _ticking = MutableLiveData(false)
    val ticking: LiveData<Boolean> = _ticking

    private val _progress = MutableLiveData<Float>()
    val progress: LiveData<Float> = _progress

    private var timer: CountDownTimer? = null

    fun appendToDuration(value: Int) {
        val oldDuration = (timerDuration.value ?: TimerDuration()).toCondensedFormat()

        if (oldDuration.trimStart { it == '0' }.length == CONDENSED_DURATION_MAX_LENGTH) {
            return
        }

        val durations = (oldDuration + value.toString())
            .removePrefix("0")
            .padStart(6, '0')
            .chunked(2)
        val hours = durations[0].toInt()
        val minutes = durations[1].toInt()
        val seconds = durations[2].toInt()

        _timerDuration.postValue(TimerDuration(hours, minutes, seconds))
    }

    fun back() {
        val oldDuration = (timerDuration.value ?: TimerDuration())
            .toCondensedFormat()

        val durations = oldDuration
            .substring(0, oldDuration.length - 1)
            .padStart(6, '0')
            .chunked(2)
        val hours = durations[0].toInt()
        val minutes = durations[1].toInt()
        val seconds = durations[2].toInt()

        _timerDuration.postValue(TimerDuration(hours, minutes, seconds))
    }

    fun startTimer() {
        val actualDuration = timerDuration.value?.toSeconds()?.toLong() ?: return
        _progress.postValue(1f)
        timer = object : CountDownTimer(TimeUnit.SECONDS.toMillis(actualDuration), 1_000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsUntilFinished = (millisUntilFinished / 1_000f).roundToInt()
                val seconds = secondsUntilFinished % 60
                val minutes = secondsUntilFinished / 60 % 60
                val hours = secondsUntilFinished / (60 * 60)
                _timerDuration.postValue(TimerDuration(hours, minutes, seconds))
                _progress.postValue(secondsUntilFinished / actualDuration.toFloat())
            }

            override fun onFinish() {
                _timerDuration.postValue(TimerDuration())
                _progress.postValue(0f)
            }
        }
        timer?.start()
        _ticking.postValue(true)
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
        _ticking.postValue(false)
    }

    companion object {
        private const val CONDENSED_DURATION_MAX_LENGTH = 6
    }
}

data class TimerDuration(
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
) {
    fun isSet() = hours != 0 || minutes != 0 || seconds != 0
    fun toCondensedFormat(): String {
        return hours.toString().padStart(2, '0') +
            minutes.toString().padStart(2, '0') +
            seconds.toString().padStart(2, '0')
    }

    fun toSeconds() = hours * 60 * 60 + minutes * 60 + seconds
}
