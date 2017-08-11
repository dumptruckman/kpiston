package com.dumptruckman.bukkit.metadata

import kotlin.reflect.KClass

/**
 * A simple class to be used as a unique key in Metadata in order to retain type information of metadata values.
 *
 * @param V The type of the metadata value.
 *
 * @constructor
 * Creates a MetadataKey for uniquely identifying a metadata value with the given type.
 *
 * @param type The type used for the metadata value this MetdataKey will identify, null can be specified if the generic
 * parameter is given.
 * @param V The type of the metadata value.
 */
@Suppress("AddVarianceModifier")
open class MetadataKey<V : Any>(val type: KClass<out V>? = null)