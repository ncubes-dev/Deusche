package com.ndozdev.deuche.presentation

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf

import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Settings(
    onEvent: (MainScreenEvents) -> Unit,
    speed: Float,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.onBackground)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            val context = LocalContext.current
            var slideValue by remember {
                mutableFloatStateOf(speed)
            }
            Text(
                text = "Reader speed settings",
                modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.surface,
            )
            Text(
                text = "Reader speed ${speed}x", modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 20.dp, start = 10.dp, end = 10.dp),
                color = MaterialTheme.colorScheme.surface,
            )
            Slider(
                onValueChangeFinished = {
                    onEvent(MainScreenEvents.UpdateSpeed(slideValue))
                    Toast.makeText(context, "$slideValue", Toast.LENGTH_SHORT).show()
                },
                colors = SliderDefaults.colors(
                    activeTickColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    inactiveTickColor = MaterialTheme.colorScheme.secondary,
                ),
                valueRange = 0f..2f,
                steps = 10,
                value = slideValue,
                onValueChange = { slideValue = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )

        }
    }
}