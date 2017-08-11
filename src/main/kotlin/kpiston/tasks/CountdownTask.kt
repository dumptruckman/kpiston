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
package kpiston.tasks

import org.bukkit.scheduler.BukkitRunnable

/**
 * A simple countdown task for counting down some number of seconds.
 *
 * Uses system time rather than server ticks for greater accuracy.
 *
 * @constructor
 * Creates a countdown task for the given number of seconds.
 *
 * @param length The length of the countdown in seconds.
 * @param notifySeconds A function to call when the number of seconds remaining changes.
 * @param notifyFinished A function to call when the countdown is complete.
 */
class CountdownTask(length: Int,
                    private val notifySeconds: ((Int) -> Unit)? = null,
                    private val notifyFinished: (() -> Unit)? = null)
    : BukkitRunnable() {

    private var countdown: Long = length.toLong() * 1000
    private var lastTime: Long = -1
    private var lastSeconds: Int = length

    override fun run() {
        if (lastTime < 0) {
            lastTime = System.currentTimeMillis()
        } else {
            val currentTime = System.currentTimeMillis()
            val delta = currentTime - lastTime
            lastTime = currentTime
            countdown -= delta

            if (notifyFinished != null && countdown <= 0) {
                notifyFinished.invoke()
            } else if (notifySeconds != null) {
                val remaining = remainingSeconds
                if (remaining < lastSeconds) {
                    notifySeconds.invoke(remaining)
                    lastSeconds = remaining
                }
            }
        }
    }

    /**
     * The seconds remaining on this countdown.
     */
    val remainingSeconds: Int
        get() = Math.ceil(countdown / 1000.0).toInt()
}