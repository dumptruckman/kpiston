package com.dumptruckman.bukkit.metadata

/**
 * A simple [HashMap] backed metadata store.
 *
 * @constructor
 * Creates a new [MutableMap] backed metadata store.
 *
 * @param objectMetadataMap Optionally provide the backing Map. Defaults to a [HashMap].
 * @param metadataProvider A provider of Metadata objects can be given if metadata should be something other than
 * [SimpleMetadata] be desired.
 */
open class SimpleMetadataStore(
        private val objectMetadataMap: MutableMap<Any, Metadata> = HashMap<Any, Metadata>(),
        private val metadataProvider: () -> Metadata = { SimpleMetadata() })
    : MetadataStore {

    override fun getMetadata(obj: Any): Metadata {
        return objectMetadataMap.getOrPut(obj, metadataProvider)
    }

    override fun hasMetadata(obj: Any): Boolean {
        return objectMetadataMap.containsKey(obj)
    }

    override fun clearMetadata(obj: Any) {
        objectMetadataMap.remove(obj)
    }

    override fun clearAllMetadata() {
        objectMetadataMap.clear()
    }
}
