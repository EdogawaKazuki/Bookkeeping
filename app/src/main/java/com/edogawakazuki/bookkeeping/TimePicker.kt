package com.edogawakazuki.bookkeeping

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import com.edogawakazuki.bookkeeping.utils.Utils
import java.util.Calendar

@Composable
fun TimePickerFieldToModal(
    modifier: Modifier = Modifier,
    calendar: Calendar,
    showModel: MutableState<Boolean>,
    onTimeSelected: (Calendar) -> Unit
) {
    var selectedTime by remember { mutableLongStateOf(calendar.timeInMillis) }
    var showModal by remember { showModel }

    TextField(
        value = selectedTime.let { Utils.convertMillisToTimeString(it) } ?: "",
        onValueChange = {  },
        label = { Text("Time") },
        placeholder = { Text("HH:ss") },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedTime) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        TimePickerModal(
            calendar = calendar,
            onTimeSelected = {
                selectedTime = it.timeInMillis
                val tempCalendar = Calendar.getInstance().apply{timeInMillis = it.timeInMillis}
                calendar.set(Calendar.HOUR_OF_DAY, tempCalendar.get(Calendar.HOUR_OF_DAY))
                calendar.set(Calendar.MINUTE, tempCalendar.get(Calendar.MINUTE))
                onTimeSelected(calendar)
                showModal = false
            },
            onDismiss = {
                showModal = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerModal(
    calendar: Calendar,
    onTimeSelected: (Calendar) -> Unit,
    onDismiss: () -> Unit,
) {

    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                onTimeSelected(calendar)
            }) {
                Text("OK")
            }
        },
        text = {
            TimePicker(
                state = timePickerState,
            ) }
    )

}