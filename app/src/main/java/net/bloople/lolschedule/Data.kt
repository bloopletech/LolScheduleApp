@file:UseSerializers(ZonedDateTimeSerializer::class)
package net.bloople.lolschedule

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
    override val descriptor = PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: ZonedDateTime) = encoder.encodeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    override fun deserialize(decoder: Decoder): ZonedDateTime = ZonedDateTime.parse(decoder.decodeString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}

@Serializable
class Schedule(
    val version: Int,
    val generated: ZonedDateTime,
    val data_generated: ZonedDateTime,
    val streams: List<Stream>,
    val matches: List<Match>,
    val logos: List<Logo>
)

@Serializable
class Stream(
    val name: String,
    val url: String,
    val league: String,
    val league_long: String,
    val league_slug: String
)

@Serializable
class Match(
    val time: ZonedDateTime,
    val tags: List<String>,
    val league: String,
    val league_long: String,
    val league_slug: String,
    val team_1: String? = null,
    val team_1_logo: String? = null,
    val team_2: String? = null,
    val team_2_logo: String? = null,
    val player_1: String? = null,
    val player_2: String? = null,
    val vods: List<String>? = ArrayList()
) {
    val participant_1: String get() = this.team_1 ?: this.player_1!!
    val participant_1_logo: String? get() = this.team_1_logo
    val participant_2: String get() = this.team_2 ?: this.player_2!!
    val participant_2_logo: String? get() = this.team_2_logo
    val local_time: ZonedDateTime get() = time.withZoneSameInstant(ZoneId.systemDefault())
}

@Serializable
class Logo(
    val aliases: List<String>,
    val data: String
)