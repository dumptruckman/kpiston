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
package kpiston.extensions

import kpiston.util.ImmutableStableLocation
import kpiston.util.StableLocation
import org.bukkit.Location

fun Location.isStable() = this is StableLocation
fun Location.isImmutable() = this is ImmutableStableLocation
fun Location.isMutable() = this !is ImmutableStableLocation

/**
 * Casts this location as [StableLocation] if not immutable, else creates a new [StableLocation] for this location.
 */
fun Location.asStableLocation() = if (isStable() && isMutable()) this as StableLocation else StableLocation(this)

/**
 * Casts this location as [ImmutableStableLocation] if immutable, else creates a new [ImmutableStableLocation] for this
 * location.
 */
fun Location.asImmutableLocation() = if (isImmutable()) this as ImmutableStableLocation else ImmutableStableLocation(this)