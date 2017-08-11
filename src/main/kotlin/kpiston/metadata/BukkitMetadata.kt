package kpiston.metadata

object BukkitMetadata {

    val weakMetadataStore: MetadataStore by lazy {
        WeakMetadataStore()
    }

    val strongMetadataStore: MetadataStore by lazy {
        SimpleMetadataStore()
    }

    /**
     * Clears all of the metadata in the static metadata stores.
     */
    fun clearAllMetadata() {
        weakMetadataStore.clearAllMetadata()
        strongMetadataStore.clearAllMetadata()
    }
}