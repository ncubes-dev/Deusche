package com.ndozdev.deuche.presentation.ui_utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ndozdev.deuche.R

@Composable
fun VoiceBubble(speak:()->Unit,text:String,selectedAnswerState:Boolean) {
    val color = MaterialTheme.colorScheme.secondaryContainer
    Row {

        Image(
            painter = painterResource(id = R.drawable.kid),
            modifier = Modifier
                .padding(top = 70.dp)
                .size(50.dp),
            contentDescription = "avatar"
        )
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.padding(bottom = 50.dp)
        ) {
            Row(
                modifier = Modifier
                    .drawBehind {
                        val cornerRadius = 10.dp.toPx()
                        val triangleHeight = 20.dp.toPx()
                        val triangleWidth = 25.dp.toPx()
                        val trianglePath = Path().apply {
                            moveTo(0f, size.height - cornerRadius)
                            lineTo(0f, size.height + triangleHeight)
                            lineTo(triangleWidth, size.height - cornerRadius)
                            close()
                        }
                        drawPath(
                            path = trianglePath,
                            color = color
                        )
                    }
                    .background(
                        color = color,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp)
            ) {
                Button(onClick = { speak() }) {
                    Icon(painterResource(id = R.drawable.baseline_volume_up_24), contentDescription = "Speak", tint = Color.Blue, modifier = Modifier.size(50.dp))
                    Text(text = if(selectedAnswerState) text else "listen")
                }
            }
        }
    }
}