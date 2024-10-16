package com.edogawakazuki.bookkeeping.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Utils {
    companion object {
        fun convertMillisToDateString(millis: Long): String {
            val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            return formatter.format(Date(millis))
        }

        fun convertMillisToTimeString(millis: Long): String {
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            return formatter.format(Date(millis))
        }

        fun convertMillisToDateTimeString(millis: Long): String {
            val formatter = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault())
            return formatter.format(Date(millis))
        }



        fun convertDateToMillis(date: String): Long {
            val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            return formatter.parse(date)?.time ?: 0
        }

        fun getOffsetMillis(): Long {
            val calendar = Calendar.getInstance()
            val offset = calendar.timeZone.getOffset(calendar.timeInMillis)
            return offset.toLong()
        }

        fun splitDateTimeMillis(datetimeMillis: Long): Pair<Long, Long> {
            // Create a Calendar instance from the datetimeMillis
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = datetimeMillis

            // Extract the time component (time within the day)
            val timeMillis = calendar.get(Calendar.HOUR_OF_DAY) * 3600000L +
                    calendar.get(Calendar.MINUTE) * 60000L +
                    calendar.get(Calendar.SECOND) * 1000L +
                    calendar.get(Calendar.MILLISECOND)

            // Set the time to midnight (00:00:00) to extract the date in millis
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val dateMillis = calendar.timeInMillis

            // Return both dateMillis and timeMillis
            return Pair(dateMillis, timeMillis)
        }

        fun splitTimeMillis(timeMillis: Long): Pair<Int, Int> {
            val hours = (timeMillis / 3600000).toInt()
            val minutes = ((timeMillis % 3600000) / 60000).toInt()
            return Pair(hours, minutes)
        }

        fun combineHourAndMinute(hour: Int, minute: Int): Long {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar.timeInMillis
        }

        fun combineDateAndTime(date: Long, time: Long): Long {
            val dateCalendar = Calendar.getInstance().apply { timeInMillis = date }
            val timeCalendar = Calendar.getInstance().apply { timeInMillis = time }
            val combinedCalendar = Calendar.getInstance().apply {
                set(
                    dateCalendar.get(Calendar.YEAR),
                    dateCalendar.get(Calendar.MONTH),
                    dateCalendar.get(Calendar.DAY_OF_MONTH),
                    timeCalendar.get(Calendar.HOUR_OF_DAY),
                    timeCalendar.get(Calendar.MINUTE),
                    timeCalendar.get(Calendar.SECOND)
                )
            }
            return combinedCalendar.timeInMillis
        }

        fun combineDateAndTime(date: Long, hour: Int, minute: Int): Long {
            val dateCalendar = Calendar.getInstance().apply { timeInMillis = date }
            val combinedCalendar = Calendar.getInstance().apply {
                set(
                    dateCalendar.get(Calendar.YEAR),
                    dateCalendar.get(Calendar.MONTH),
                    dateCalendar.get(Calendar.DAY_OF_MONTH),
                    hour,
                    minute,
                    0
                )
            }
            return combinedCalendar.timeInMillis
        }
    }

}