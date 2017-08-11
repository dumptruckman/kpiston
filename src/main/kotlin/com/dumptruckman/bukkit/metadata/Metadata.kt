package com.dumptruckman.bukkit.metadata

interface Metadata {

    /**
     * Returns the metadata value of a given key or null if no value has been set for that key.
     *
     * @param key The key to retrieve the metadata value for.
     * @return The metadata value for the given key or null if none has been set.
     */
    operator fun <V : Any> get(key: MetadataKey<V>): V?

    /**
     * Invokes the metadata to return the metadata value of a given metadata key.
     *
     * If no value has been set for that key, the defaultSupplier is called to set a value for that key and then
     * return it.
     *
     * @param key The key to retrieve the metadata value for.
     * @param defaultSupplier A functional supplier for a default metadata value for the given key.
     * @return The metadata value for the given key or value provided by the supplier if none was present.
     */
    operator fun <V : Any> invoke(key: MetadataKey<V>, defaultSupplier: () -> V): V

    /**
     * Invokes the metadata to return the metadata value of a given metadata key.
     *
     * If no value has been set for that key, the key's defaultSupplier is called to set a value for that key and then
     * return it.
     *
     * @param key The key to retrieve the metadata value for.
     * @return The metadata value for the given key or value provided by the supplier if none was present.
     */
    operator fun <V : Any> invoke(key: DefaultableMetadataKey<V>): V

    /**
     * Sets the metadata value for a given key.
     *
     * If the a value has already been set for the given key, this will replace it with the new given value.
     *
     * @param key The key to set the metadata value for.
     * @param value The new metadata value.
     */
    operator fun <V : Any> set(key: MetadataKey<V>, value: V)

    /**
     * Removes any metadata value associated with the given key.
     *
     * @param key The key to remove metadata for.
     */
    fun remove(key: MetadataKey<*>)
}