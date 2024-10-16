package com.edogawakazuki.bookkeeping

import android.util.Log
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import com.edogawakazuki.bookkeeping.utils.Utils
import java.util.Calendar

@Composable
fun DatePickerFieldToModal(
    modifier: Modifier = Modifier,
    calendar: Calendar,
    showModel: MutableState<Boolean>,
    onDateSelected: (Calendar) -> Unit
) {
    var selectedDate by remember { mutableStateOf<Long?>(calendar.timeInMillis) }
    var showModal by remember { showModel }

    TextField(
        value = selectedDate?.let { Utils.convertMillisToDateString(it) } ?: "",
        onValueChange = {  },
        label = { Text("Date") },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
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
        DatePickerModal(
            selectedDate = selectedDate,
            onDateSelected = {
                selectedDate = it
                val tempCalender = Calendar.getInstance().apply { timeInMillis = it!! }
                calendar.set(tempCalender.get(Calendar.YEAR), tempCalender.get(Calendar.MONTH), tempCalender.get(Calendar.DAY_OF_MONTH))
                onDateSelected(calendar)
                             },
            onDismiss = {
                showModal = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    selectedDate: Long?,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = (selectedDate ?: System.currentTimeMillis()) + Utils.getOffsetMillis(),
        initialDisplayedMonthMillis = (selectedDate ?: System.currentTimeMillis()) + Utils.getOffsetMillis(),
    )
    Log.d("DatePickerModal", "selectedDate: $selectedDate")
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}
