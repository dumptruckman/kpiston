package kpiston.metadata

import java.util.WeakHashMap

class WeakMetadataStore(metadataProvider: () -> Metadata = { SimpleMetadata() })
    : SimpleMetadataStore(WeakHashMap<Any, Metadata>(), metadataProvider)