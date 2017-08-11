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
