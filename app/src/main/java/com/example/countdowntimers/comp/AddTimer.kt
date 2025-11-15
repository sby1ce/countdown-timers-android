/*
Copyright 2025 sby1ce

SPDX-License-Identifier: AGPL-3.0-or-later
*/

package com.example.countdowntimers.comp

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.countdowntimers.viewmodel.TimerViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
private fun showDateTime(date: DatePickerState, time: TimePickerState): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val day = date.selectedDateMillis?.let {
        formatter.format(Date(it))
    } ?: ""

    return "$day ${time.hour}:${time.minute}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimer(viewModel: TimerViewModel) {
    // https://developer.android.com/develop/ui/compose/components/datepickers
    var showDateModal by remember { mutableStateOf(false) }
    var showTimeModal by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    // Set current date as default
    val date = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val time = rememberTimePickerState()

    @StringRes
    val errorText by viewModel.errorFlow.collectAsState()

    val addTimer: () -> Unit = {
        val dateMillis = date.selectedDateMillis
        val hour = time.hour
        val minute = time.minute
        viewModel.addTimer(name, dateMillis, hour, minute)
    }

    val dateModal = @Composable {
        DatePickerDialog(
            onDismissRequest = { showDateModal = false },
            confirmButton = {
                Button(
                    onClick = { showDateModal = false }) {
                    Text(text = "Confirm")
                }
            }) {
            DatePicker(
                state = date
            )
        }
    }
    val timeModal = @Composable {
        DatePickerDialog(
            onDismissRequest = { showTimeModal = false },
            confirmButton = {
                Button(
                    onClick = { showTimeModal = false }) {
                    Text(text = "Confirm")
                }
            }) {
            TimePicker(
                state = time,
            )
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Timer name here") }
        )
        OutlinedTextField(
            value = showDateTime(date, time),
            onValueChange = {},
            label = {
                Text(text = "Picked datetime")
            },
            readOnly = true,
        )
        errorText?.let { id ->
            Text(text = stringResource(id))
        }
        Row {
            FilledTonalButton(
                modifier = Modifier.padding(4.dp),
                onClick = { showDateModal = true }
            ) {
                Text(text = "Pick date")
            }
            FilledTonalButton(
                modifier = Modifier.padding(4.dp),
                onClick = { showTimeModal = true },
            ) {
                Text(text = "Pick time")
            }
            Button(
                modifier = Modifier.padding(4.dp), onClick = addTimer
            ) {
                Text(text = "Add timer")
            }
        }
    }
    if (showDateModal) {
        dateModal()
    } else if (showTimeModal) {
        timeModal()
    }
}

@Preview(showBackground = true)
@Composable
fun AddTimerPreview() {
    AddTimer(hiltViewModel<TimerViewModel>())
}
