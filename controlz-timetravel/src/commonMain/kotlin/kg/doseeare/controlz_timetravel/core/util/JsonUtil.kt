package kg.doseeare.controlz_timetravel.core.util

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer

internal object JsonUtil {
	private val json: Json by lazy {
		Json {
			encodeDefaults = true
			explicitNulls = true
		}
	}
	
	@OptIn(InternalSerializationApi::class)
	fun <T> getJsonObject(`object`: T): JsonObject {
		if (`object` == null) return JsonObject(emptyMap())
		
		val oldStateSerializer = `object`::class.serializer() as SerializationStrategy<T>
		return json.encodeToJsonElement(oldStateSerializer, `object`).jsonObject
	}
	
	@OptIn(InternalSerializationApi::class)
	fun <T> toObject(sampleObject: T, string: String): T? {
		if (sampleObject == null) return null
		
		val sampleSerializer = sampleObject::class.serializer() as DeserializationStrategy<T>
		return json.decodeFromString(sampleSerializer, string)
	}
	
}