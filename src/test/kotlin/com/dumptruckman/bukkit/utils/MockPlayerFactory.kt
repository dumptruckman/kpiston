package com.dumptruckman.bukkit.utils

import org.bukkit.entity.Player
import org.mockito.Mockito.*
import java.util.*

object MockPlayerFactory {

    fun createMockedPlayer(name: CharSequence): Player {
        val player = mock(Player::class.java)

        `when`(player.name).thenReturn(name.toString())
        `when`(player.uniqueId).thenReturn(UUID.randomUUID())

        return player;
    }
}