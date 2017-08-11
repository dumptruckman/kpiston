package kpiston.metadata

/**
 * A store for linking [Metadata] to arbitrary objects.
 */
interface MetadataStore {

    /**
     * Returns the Metadata for the given object in this MetadataStore, creating it if it does not exist.
     *
     * @param obj The object to get Metadata for.
     * @return The Metadata for the given object.
     */
    fun getMetadata(obj: Any): Metadata

    /**
     * Returns whether or not the given object has Metadata associated with it in this MetadataStore.
     *
     * @param obj The object to check for Metadata.
     * @return true if the object has Metadata associated with it in this store, false otherwise.
     */
    fun hasMetadata(obj: Any): Boolean

    /**
     * Removes any Metadata associated with the given object in this MetadataStore.
     *
     * @param obj The object to remove the Metadata for.
     */
    fun clearMetadata(obj: Any)

    /**
     * Removes all Metadata from this MetadataStore.
     */
    fun clearAllMetadata()
}
