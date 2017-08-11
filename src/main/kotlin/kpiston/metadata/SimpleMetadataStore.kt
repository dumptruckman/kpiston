/*
 * This file is part of kpiston.
 *
 * Copyright (c) 2017 Jeremy Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package kpiston.metadata

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
