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
package kpiston.arena

import kpiston.Game
import kpiston.extensions.copy
import kpiston.extensions.delete
import kpiston.extensions.immutableLocation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator

class ArenaWorld<G : Game<G>>(game: G, name: CharSequence, private val templateName: CharSequence)
    : Arena<G>(game, name) {

    lateinit private var world: World

    override fun setUp() {
        val templateWorld = Bukkit.getWorld(templateName.toString())
        templateWorld?.copy(name) ?: throw IllegalStateException("Template world $templateName does not exist!")
        world = WorldCreator(name.toString()).copy(templateWorld).createWorld()
    }

    override fun tearDown() {
        world.delete()
    }

    override val primarySpawn: Location
        get() = world.spawnLocation

    fun test() {
        val t = world.getBlockAt(0, 0, 0).immutableLocation
    }
}