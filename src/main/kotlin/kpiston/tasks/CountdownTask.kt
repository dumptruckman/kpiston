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