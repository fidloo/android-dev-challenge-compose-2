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
