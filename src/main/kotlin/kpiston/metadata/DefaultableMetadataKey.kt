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

import kotlin.reflect.KClass

/**
 * A class to be used as a unique key in Metadata in order to retain type information of metadata values.
 *
 * Using this instead of [MetadataKey] allows a supplier of default values to be specified. This can be used along with
 * [Metadata.invoke] for added convenience.
 *
 * @param V The type of the metadata value.
 *
 * @constructor
 * Creates a MetadataKey for uniquely identifying a metadata value with the given type.
 *
 * @param defaultSupplier A function that supplies a default value for the values of this key.
 * @param type The type used for the metadata value this MetdataKey will identify, null can be specified if the generic
 * parameter is given.
 * @param V The type of the metadata value.
 */
@Suppress("AddVarianceModifier")
class DefaultableMetadataKey<V : Any>(val defaultSupplier: () -> V, type: KClass<out V>? = null)
    : MetadataKey<V>(type)