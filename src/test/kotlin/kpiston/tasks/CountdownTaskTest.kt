package kpiston.tasks

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class CountdownTaskTest {

    lateinit var countdown: CountdownTask

    @Before
    fun setUp() {
        countdown = CountdownTask(5)
    }

    @Test
    fun getRemainingSeconds() {
        assertEquals(5, countdown.remainingSeconds)
        countdown.run() // run to set initial time
        Thread.sleep(200)
        countdown.run()
        assertEquals(5, countdown.remainingSeconds)
        Thread.sleep(1000)
        countdown.run()
        assertEquals(4, countdown.remainingSeconds)
    }

}