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

import kpiston.metadata.BukkitMetadata
import kpiston.metadata.Metadata
import kpiston.util.ImmutableStableLocation
import kpiston.util.StableLocation
import org.bukkit.block.Block

val Block.immutableLocation: ImmutableStableLocation
    get() = ImmutableStableLocation(this.world, this.x.toDouble(), this.y.toDouble(), this.z.toDouble())

val Block.stableLocation: StableLocation
    get() = StableLocation(this.world, this.x.toDouble(), this.y.toDouble(), this.z.toDouble())

val Block.metadata: Metadata
    get() = BukkitMetadata.strongMetadataStore.getMetadata(stableLocation)