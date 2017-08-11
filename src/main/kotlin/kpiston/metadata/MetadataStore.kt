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
