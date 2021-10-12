package net.bloople.lolschedule

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import java.lang.Long.max
import java.net.URL
import java.time.ZonedDateTime

class ScheduleRepository(private val context: Context) {
    val schedules: Flow<Schedule>
    private var lastDownloaded: Long = 0L

    init {
        schedules = flow {
            load()?.let {
                emit(Schedule(it))
                val now = System.currentTimeMillis()
                val age = now - lastDownloaded
                println("now: $now, age: $age, delay: ${REFRESH_INTERVAL_MS - age}")
                delay(max(REFRESH_INTERVAL_MS - age, 0L))
            }

            while(true) {
                download()?.let {
                    emit(Schedule(it))
                    save(it)
                }
                delay(REFRESH_INTERVAL_MS)
            }
        }
    }

    private suspend fun load(): SerializedSchedule? {
        try {
            val serializedSchedule: SerializedSchedule

            withContext(Dispatchers.IO) {
                val externalCacheFile = File(context.externalCacheDir, DOWNLOADED_SCHEDULE_PATH)
                println("Loading schedule from file $externalCacheFile")
                lastDownloaded = externalCacheFile.lastModified()
                println("Updated lastDownloaded to $lastDownloaded")
                externalCacheFile.inputStream().use { serializedSchedule = Json.decodeFromStream(it) }
            }

            return serializedSchedule
        }
        catch(e: FileNotFoundException) {
            return null
        }
        catch(e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private suspend fun save(serializedSchedule: SerializedSchedule) {
        try {
            withContext(Dispatchers.IO) {
                val externalCacheFile = File(context.externalCacheDir, DOWNLOADED_SCHEDULE_PATH)
                println("Saving schedule to file $externalCacheFile")
                externalCacheFile.outputStream().use { Json.encodeToStream(serializedSchedule, it) }
            }
        }
        catch(e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    private suspend fun download(): SerializedSchedule? {
        try {
            val serializedSchedule: SerializedSchedule

            withContext(Dispatchers.IO) {
                println("Downloading latest schedule")
                val connection = URL("https://lol.bloople.net/data.json").openConnection()
                connection.getInputStream().use { serializedSchedule = Json.decodeFromStream(it) }
            }

            lastDownloaded = System.currentTimeMillis()
            println("Updated lastDownloaded to $lastDownloaded")

            return serializedSchedule
        }
        catch(e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    companion object {
        const val DOWNLOADED_SCHEDULE_PATH = "schedule.json"
        const val REFRESH_INTERVAL_MS = 10 * 60 * 1000L //10 minutes
    }
}