package com.fidloo.countdown

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountdownViewModel : ViewModel() {

    private val _timerDuration = MutableLiveData(TimerDuration())
    val timerDuration: LiveData<TimerDuration> = _timerDuration

    fun appendToDuration(value: Int) {
        val oldDuration = (timerDuration.value ?: TimerDuration()).toCondensedFormat()

        if (oldDuration.length == CONDENSED_DURATION_MAX_LENGTH) {
            return
        }

        val durations = (oldDuration + value.toString())
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

    var timerValue = 0L

    val timer = object : CountDownTimer(20000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            timerValue = millisUntilFinished
        }

        override fun onFinish() {
        }
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
    fun toCondensedFormat() = (hours.toString() + minutes.toString() + seconds.toString())
}
