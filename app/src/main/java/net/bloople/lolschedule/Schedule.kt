package net.bloople.lolschedule

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.lang.Exception
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.collections.HashMap

class Schedule(serializedSchedule: SerializedSchedule) {
    val matches: Map<Int, List<Match>>;
    val streams: List<Stream>;
    val years get() = matches.keys.toList();
    val currentYear get() = years.last();

    init {
        val logos: HashMap<String, Bitmap> = HashMap();
        for(serializedLogo in serializedSchedule.logos) {
            val byteArray = Base64.decode(serializedLogo.data, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            for(alias in serializedLogo.aliases) logos[alias] = bitmap
        }

        matches = serializedSchedule.matches.map { serializedMatch ->
            Match(
                serializedMatch.time.withZoneSameInstant(ZoneId.systemDefault()),
                serializedMatch.tags.toMutableList(),
                serializedMatch.league,
                serializedMatch.league_long,
                serializedMatch.team_1 ?: serializedMatch.player_1!!,
                serializedMatch.team_1?.let { logos[serializedMatch.team_1_logo] },
                serializedMatch.team_2 ?: serializedMatch.player_2!!,
                serializedMatch.team_2?.let { logos[serializedMatch.team_2_logo] },
                serializedMatch.vods,
                serializedMatch.key
            )
        }.groupBy { match -> match.time.year }.toSortedMap()

        streams = serializedSchedule.streams.map { serializedStream ->
            Stream(
                serializedStream.name,
                serializedStream.url,
                serializedStream.league,
                serializedStream.league_long
            )
        }
    }

    operator fun get(index: Int): List<Match>? {
        return matches[index]
    }

    fun getStreams(year: Int): List<Stream> {
        return if(year == currentYear) streams else emptyList();
    }

    companion object {
        @JvmStatic
        fun download(): Schedule {
            try {
                val connection = URL("https://lol.bloople.net/data.json").openConnection();
                return Schedule(Json.decodeFromStream(connection.getInputStream()));
            }
            catch(e: Exception) {
                e.printStackTrace();
                throw e;
            }
        }
    }
}

class Match(
    val time: ZonedDateTime,
    val tags: MutableList<String>,
    val league: String,
    val leagueLong: String,
    val participant1: String,
    val participant1Logo: Bitmap?,
    val participant2: String,
    val participant2Logo: Bitmap?,
    val vods: List<String>,
    val key: String,
    var todayish: Boolean = false
)

class Stream(
    val name: String,
    val url: String,
    val league: String,
    val leagueLong: String
)
