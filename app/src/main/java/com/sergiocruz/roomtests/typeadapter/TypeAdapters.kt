package com.sergiocruz.roomtests.typeadapter

import androidx.room.TypeConverter
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.time.*

/** GSON JSON Type Adapter **/
class DateTypeAdapter : TypeAdapter<LocalDate?>() {

    @Synchronized
    @Throws(IOException::class)
    override fun read(reader: JsonReader): LocalDate? {
        try {
            if (reader.peek() === JsonToken.NULL) {
                reader.nextNull()
                return null
            }
            return LocalDate.parse(reader.nextString())
        } catch (e: Exception) {
        }
        return null
    }

    @Synchronized
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: LocalDate?) {
        if (value == null) {
            out.nullValue()
            return
        }
        val dateAsString: String = value.toString()
        out.value(dateAsString)
    }

}

/** ROOM TYPE ADAPTER **/
class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): LocalDate? {
        Instant.now()
        return timestamp?.let {
            Instant.ofEpochMilli(it)
                .atOffset(ZoneOffset.UTC)
                .toLocalDate()
        }
    }

    @TypeConverter
    fun toTimestamp(date: LocalDate?): Long {
        val millis = LocalDateTime
            .of(date, LocalTime.MIN)
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli()
        return millis
    }
}