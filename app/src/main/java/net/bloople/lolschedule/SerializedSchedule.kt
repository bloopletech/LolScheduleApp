@file:UseSerializers(ZonedDateTimeSerializer::class)
package net.bloople.lolschedule

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


object ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
    override val descriptor = PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: ZonedDateTime) = encoder.encodeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    override fun deserialize(decoder: Decoder): ZonedDateTime = ZonedDateTime.parse(decoder.decodeString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}

@Serializable
data class SerializedSchedule(
    val version: Int,
    val generated: ZonedDateTime,
    val data_generated: ZonedDateTime,
    val streams: List<SerializedStream>,
    var matches: List<SerializedMatch>,
    val logos: List<SerializedLogo>
)

@Serializable
data class SerializedStream(
    val name: String,
    val url: String,
    val league: String,
    val league_long: String,
    val league_slug: String
)

@Serializable
data class SerializedMatch(
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
    val vods: List<String> = ArrayList()
) {
    val key get() = "$league_slug-${team_1.orEmpty()}-${team_2.orEmpty()}-${player_1.orEmpty()}-${player_2.orEmpty()}-$time"
}

@Serializable
data class SerializedLogo(
    val aliases: List<String>,
    val data: String
)