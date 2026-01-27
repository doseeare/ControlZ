package kg.doseeare.controlz_timetravel.core.controller

import kg.doseeare.controlz_timetravel.core.model.ChangeType
import kg.doseeare.controlz_timetravel.core.model.Difference
import kg.doseeare.controlz_timetravel.core.util.JsonUtil
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer
import kotlin.collections.iterator

internal object DiffFinder {
	
    @OptIn(InternalSerializationApi::class)
    fun <T> computeJsonDiff(old: T?, new: T?): Map<String, List<Difference>>? {
	    val changes = mutableMapOf<String, MutableList<Difference>>()
	    try {
		    
		    if (old == null || new == null) return null
		    
		    val oldJson = JsonUtil.getJsonObject(old)
		    val newJson = JsonUtil.getJsonObject(new)
		    
		    for ((key, newVal) in newJson) {
			    val oldVal = oldJson[key]
			    when {
				    oldVal == null -> {
					    changes.getOrPut(key) { mutableListOf() }
						    .add(Difference(ChangeType.ADDED, valueString(newVal)))
				    }
				    
				    oldVal != newVal -> {
					    if (newVal is JsonArray && oldVal is JsonArray) {
						    val arrayDiffs = diffArrays(oldVal, newVal)
						    if (arrayDiffs.isNotEmpty()) {
							    changes.getOrPut(key) { mutableListOf() }.addAll(arrayDiffs)
						    }
					    } else {
						    changes.getOrPut(key) { mutableListOf() }
							    .add(
								    Difference(
									    ChangeType.CHANGED,
									    "${valueString(oldVal)} → ${valueString(newVal)}"
								    )
							    )
					    }
				    }
			    }
		    }
		    for ((key, oldVal) in oldJson) {
			    if (!newJson.containsKey(key)) {
				    changes.getOrPut(key) { mutableListOf() }
					    .add(Difference(ChangeType.REMOVED, valueString(oldVal)))
			    }
		    }
		    
		    return changes.takeIf { it.isNotEmpty() }?.mapValues { it.value.toList() }
	    }catch (e : Exception){
			changes.put("not_found", mutableListOf(Difference(ChangeType.ERROR, "${e.message}")))
			return changes
		}
    }

    private fun diffArrays(oldArray: JsonArray, newArray: JsonArray): List<Difference> {
        val diffs = mutableListOf<Difference>()

        val oldCounts = oldArray.groupingBy { it }.eachCount()
        val newCounts = newArray.groupingBy { it }.eachCount()

        for ((elem, oldCount) in oldCounts) {
            val newCount = newCounts[elem] ?: 0
            if (newCount < oldCount) {
                repeat(oldCount - newCount) {
                    diffs.add(Difference(ChangeType.REMOVED, valueString(elem)))
                }
            }
        }

        for ((elem, newCount) in newCounts) {
            val oldCount = oldCounts[elem] ?: 0
            if (newCount > oldCount) {
                repeat(newCount - oldCount) {
                    diffs.add(Difference(ChangeType.ADDED, valueString(elem)))
                }
            }
        }

        val minSize = minOf(oldArray.size, newArray.size)
        for (i in 0 until minSize) {
            val oldElem = oldArray[i]
            val newElem = newArray[i]
            if (oldElem != newElem) {
                diffs.add(
	                Difference(
		                ChangeType.CHANGED,
		                "${valueString(oldElem)} → ${valueString(newElem)}"
	                )
                )
            }
        }

        return diffs
    }
	
    private fun valueString(el: JsonElement): String = when (el) {
        is JsonPrimitive -> {
            if (el.isString) el.content else el.toString()
        }

        is JsonObject -> el.toString()
        is JsonArray -> el.toString()
    }

   
}