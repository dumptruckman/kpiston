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
package kpiston.team

import kpiston.Game

class TeamList<G : Game<G>> protected constructor (val game: G) : Iterable<GameTeam<out G>> {

    private val teams = mutableMapOf<Any, GameTeam<out G>>()

    val count: Int
        get() = teams.size

    fun createTeam(id: Any, name: CharSequence? = null): GameTeam<G> {
        val team = GameTeam(game, id, name)
        teams[id] = team
        return team
    }

    override fun iterator() = teams.values.iterator()

    operator fun get(id: Any) = getTeam(id)

    fun getTeam(id: Any) = teams[id]

    operator fun contains(id: Any) = teams.containsKey(id)
}