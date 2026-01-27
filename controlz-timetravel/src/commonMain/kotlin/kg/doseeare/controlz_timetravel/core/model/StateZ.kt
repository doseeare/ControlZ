package kg.doseeare.controlz_timetravel.core.model

import kg.doseeare.controlz_timetravel.core.util.JsonUtil
import kg.doseeare.controlz_timetravel.core.util.TimeUtil
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
internal data class StateZ<T> @OptIn(ExperimentalTime::class) constructor(
	val state: T,
	val isCurrent: Boolean,
	val timestamp: String = TimeUtil.getTime(),
	val actionName: String? = null,
	val diff: Map<String, List<Difference>>? = null,
) {
    fun toJson(): String {
        return JsonUtil.getJsonObject(this.state).toString()
    }
}

@Serializable
data class Difference(val type: ChangeType, val changes: String)

@Serializable
enum class ChangeType {
    ADDED, REMOVED, CHANGED, ERROR
}
