package com.dumptruckman.bukkit.utils

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class TestEvent : Event() {

    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }
}