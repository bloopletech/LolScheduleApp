package net.bloople.lolschedule

import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit


class MatchTagger(private val match: Match) {
    fun tag(now: ZonedDateTime) {
        val midnight = now.truncatedTo(ChronoUnit.DAYS);

        if(TimeUtils.isSameDate(match.time, now)) match.tags.add("today");
        match.todayish = match.time.isEqual(midnight) || match.time.isAfter(midnight);
    }
}

//function parseTimes() {
//  var now = new Date();
//
//  var midnight = new Date(now.getTime());
//  midnight.setHours(0, 0, 0, 0);
//
//  var times = document.querySelectorAll(".time");
//  for(var i = 0; i < times.length; i++) {
//    var time = new Date(times[i].dataset.value);
//
//    times[i].textContent = time.formatDate(now);
//
//    var earlyStart = new Date(time.getTime());
//    earlyStart.setHours(time.getHours() - 3);
//    var scheduledEnd = new Date(time.getTime());
//    scheduledEnd.setHours(time.getHours() + 3);
//
//    var matchElement = times[i].parentNode;
//    if(scheduledEnd.getTime() < now.getTime()) matchElement.classList.add("past");
//    if((now.getTime() >= earlyStart) && (now.getTime() <= scheduledEnd)) matchElement.classList.add("current");
//    if(time.isSameDate(now)) matchElement.classList.add("today");
//    if(time.isSameWeekFuzzy(now)) matchElement.classList.add("current-week");
//    if(earlyStart.getTime() >= now.getTime()) matchElement.classList.add("future");
//    if(time.getTime() >= midnight.getTime()) matchElement.classList.add("today-ish");
//  }
//}