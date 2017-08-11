package com.dumptruckman.bukkit.metadata

import kotlin.reflect.KClass

/**
 * A class to be used as a unique key in Metadata in order to retain type information of metadata values.
 *
 * Using this instead of [MetadataKey] allows a supplier of default values to be specified. This can be used along with
 * [Metadata.invoke] for added convenience.
 *
 * @param V The type of the metadata value.
 *
 * @constructor
 * Creates a MetadataKey for uniquely identifying a metadata value with the given type.
 *
 * @param defaultSupplier A function that supplies a default value for the values of this key.
 * @param type The type used for the metadata value this MetdataKey will identify, null can be specified if the generic
 * parameter is given.
 * @param V The type of the metadata value.
 */
@Suppress("AddVarianceModifier")
class DefaultableMetadataKey<V : Any>(val defaultSupplier: () -> V, type: KClass<out V>? = null)
    : MetadataKey<V>(type)