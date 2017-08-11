package com.dumptruckman.bukkit.metadata

/**
 * A simple [HashMap] backed [Metadata] implementation.
 */
class SimpleMetadata : Metadata {

    private val metadataMap = HashMap<MetadataKey<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    override operator fun <V : Any> get(key: MetadataKey<V>): V? {
        return metadataMap.get(key) as? V?
    }

    @Suppress("UNCHECKED_CAST")
    override operator fun <V : Any> invoke(key: MetadataKey<V>, defaultSupplier: () -> V): V {
        var value: V? = get(key)
        if (value == null) {
            value = defaultSupplier()
            set(key, value)
        }
        return value
    }

    override fun <V : Any> invoke(key: DefaultableMetadataKey<V>): V = invoke(key, key.defaultSupplier)

    override operator fun <V : Any> set(key: MetadataKey<V>, value: V) {
        metadataMap.put(key, value)
    }

    override fun remove(key: MetadataKey<*>) {
        metadataMap.remove(key)
    }
}
