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
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.androiddevchallenge.ui.theme.MyTheme
import java.util.*
import java.util.concurrent.TimeUnit
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
        Column {
            var seconds by remember { mutableStateOf(0) }
            var timer: Timer? by remember { mutableStateOf(null)}

            Clock(
                seconds = seconds,
                onSecondsChanged = { diff ->
                    seconds = max(0, seconds + diff)
                }
            )

            Button(onClick = {
                timer = timer(period = 1000) {
                    when {
                        seconds <= 0 -> cancel()
                        else -> seconds--
                    }
                }
            }) {
                Text(text = "START")
            }

            Button(onClick = {
                timer?.cancel()
                seconds = 0
            }) {
                Text(text = "STOP")
            }

            Button(onClick = {
                timer?.cancel()
            }) {
                Text(text = "PAUSE")
            }
        }
    }
}

@Composable
private fun Clock(
    seconds: Int,
    onSecondsChanged: (Int) -> Unit,
) {
    Column {

        Row {
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60

            Digits(minutes, onSecondsChanged)
            Text(":")
            Digits(remainingSeconds, onSecondsChanged)
        }
        Row {
            MinutesPicker(onSecondsChanged = onSecondsChanged)
            SecondsPicker(onSecondsChanged = onSecondsChanged)
        }
    }
}

@Composable
fun Digits(
    time: Int,
    onSecondsChanged: (Int) -> Unit,
) {
    Column {
        Row {
            Text(String.format("%02d", time))
        }
    }


}

@Composable
fun MinutesPicker(onSecondsChanged: (Int) -> Unit) {
    Row {
        Button(onClick = { onSecondsChanged(-60) }) {
            Text(text = "-")
        }
        Button(onClick = { onSecondsChanged(60) }) {
            Text(text = "+")
        }
    }
}

@Composable
fun SecondsPicker(onSecondsChanged: (Int) -> Unit) {
    Row {
        Button(onClick = { onSecondsChanged(-1) }) {
            Text(text = "-")
        }
        Button(onClick = { onSecondsChanged(+1) }) {
            Text(text = "+")
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
