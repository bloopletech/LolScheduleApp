package net.bloople.lolschedule

import java.time.DayOfWeek
import java.time.Month
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class TimeRenderer {
    fun isSameDate(a: ZonedDateTime, b: ZonedDateTime): Boolean {
        return a.year == b.year && a.month == b.month && a.dayOfMonth == b.dayOfMonth;
    }

    fun isSameWeekFuzzy(a: ZonedDateTime, b: ZonedDateTime): Boolean {
        var startOfWeek = b.truncatedTo(ChronoUnit.HOURS);
        startOfWeek = b.truncatedTo(ChronoUnit.HOURS).minusHours(24L * startOfWeek.dayOfWeek.value);

        val endOfWeek = startOfWeek.plusHours(24L * 9);

        return (a.isEqual(startOfWeek) || a.isAfter(startOfWeek)) && a.isBefore(endOfWeek);
    }

    fun formatDate(dateTime: ZonedDateTime, today: ZonedDateTime): String {
        val ampm = if(dateTime.hour >= 12) "PM" else "AM";
        var hours = dateTime.hour % 12;
        if(hours == 0) hours = 12;

        var minutes = dateTime.minute.toString();
        if(dateTime.minute == 0) minutes = "";
        else if(dateTime.minute < 10) minutes = ":0$minutes";
        else minutes = ":${dateTime.minute}";

        if(isSameDate(dateTime, today)) {
            return listOf("Today", "$hours$minutes$ampm").joinToString(" ");
        }

        if(isSameWeekFuzzy(dateTime, today) && dateTime.isAfter(today)) {
            return listOf(day(dateTime.dayOfWeek), "$hours$minutes$ampm").joinToString(" ");
        }

        return listOf(
            day(dateTime.dayOfWeek),
            dateTime.dayOfMonth,
            month(dateTime.month),
            "$hours$minutes$ampm"
        ).joinToString(" ");
    }

    companion object {
        private val DAYS = arrayOf("", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
        private val MONTHS = arrayOf("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

        fun day(dayOfWeek: DayOfWeek): String {
            return DAYS[dayOfWeek.value];
        }

        fun month(month: Month): String {
            return MONTHS[month.value];
        }
    }
}
