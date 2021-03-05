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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme
import java.util.Timer
import kotlin.concurrent.timer
import kotlin.math.max

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    MyTheme {
        Surface(
            color = MaterialTheme.colors.surface,
            contentColor = Color.Cyan
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                var seconds by remember { mutableStateOf(0) }
                var timer: Timer? by remember { mutableStateOf(null) }

                Clock(
                    seconds = seconds,
                    onSecondsChanged = { diff ->
                        seconds = max(0, seconds + diff)
                    }
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Crossfade(targetState = timer) { screen ->
                        when (screen) {
                            null -> StylishButton(
                                onClick = {
                                    timer = timer(period = 1000) {
                                        when {
                                            seconds <= 0 -> {
                                                cancel()
                                                timer = null
                                            }
                                            else -> seconds--
                                        }
                                    }
                                }
                            ) {
                                Text(
                                    text = "START",
                                    color = Color.Cyan
                                )
                            }

                            else -> StylishButton(
                                onClick = {
                                    timer?.cancel()
                                    timer = null
                                }
                            ) {
                                Text(
                                    text = "PAUSE",
                                    color = Color.Cyan
                                )
                            }
                        }
                    }

                    StylishButton(
                        onClick = {
                            timer?.cancel()
                            seconds = 0
                            timer = null
                        }
                    ) {
                        Text(
                            text = "RESET",
                            color = Color.Cyan
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StylishButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) = OutlinedButton(
    border = BorderStroke(2.dp, Color.Cyan), // Todo use material theme
    modifier = modifier
        .padding(8.dp)
        .height(48.dp)
        .width(120.dp),
    onClick = onClick,
    content = content
)

@Composable
private fun Clock(
    seconds: Int,
    onSecondsChanged: (Int) -> Unit,
) {
    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60

            Digits(minutes)
            Text(
                text = ":",
                style = MaterialTheme.typography.h2,
                fontWeight = FontWeight.Bold
            )
            Digits(remainingSeconds)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            MinutesPicker(onSecondsChanged = onSecondsChanged)
            SecondsPicker(onSecondsChanged = onSecondsChanged)
        }
    }
}

@Composable
fun Digits(time: Int) {
    Row {
        Text(
            text = String.format("%02d", time),
            style = MaterialTheme.typography.h1
        )
    }
}

@Composable
fun MinutesPicker(onSecondsChanged: (Int) -> Unit) {
    Column {
        StylishButton(onClick = { onSecondsChanged(60) }) {
            Text(
                text = "+",
                color = Color.Cyan
            )
        }
        StylishButton(onClick = { onSecondsChanged(-60) }) {
            Text(
                text = "-",
                color = Color.Cyan
            )
        }
    }
}

@Composable
fun SecondsPicker(onSecondsChanged: (Int) -> Unit) {
    Column {
        StylishButton(onClick = { onSecondsChanged(+1) }) {
            Text(
                text = "+",
                color = Color.Cyan
            )
        }
        StylishButton(onClick = { onSecondsChanged(-1) }) {
            Text(
                text = "-",
                color = Color.Cyan
            )
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
