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