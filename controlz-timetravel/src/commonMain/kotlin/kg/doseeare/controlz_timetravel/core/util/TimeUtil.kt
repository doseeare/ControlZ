package kg.doseeare.controlz_timetravel.core.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal object TimeUtil {
	
    @OptIn(ExperimentalTime::class)
    fun getTime() : String {
        val timestamp: Long = Clock.System.now().toEpochMilliseconds()

        val instant = Instant.fromEpochMilliseconds(timestamp)
        val timeZone = TimeZone.currentSystemDefault()
        val localDateTime: LocalDateTime = instant.toLocalDateTime(timeZone)
        return "${localDateTime.hour.toString().padStart(2, '0')}:${localDateTime.minute.toString().padStart(2, '0')}"
    }
}