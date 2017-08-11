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
 */
class CountdownTask(length: Int) : BukkitRunnable() {

    private var countdown: Long = length.toLong() * 1000
    private var lastTime: Long = -1

    override fun run() {
        if (lastTime < 0) {
            lastTime = System.currentTimeMillis()
        } else {
            val currentTime = System.currentTimeMillis()
            val delta = currentTime - lastTime
            lastTime = currentTime
            countdown -= delta
        }
    }

    /**
     * The seconds remaining on this countdown.
     */
    val remainingSeconds: Int
        get() = Math.ceil(countdown / 1000.0).toInt()
}