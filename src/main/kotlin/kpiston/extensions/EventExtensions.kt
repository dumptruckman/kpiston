package kpiston.extensions

import org.bukkit.Bukkit
import org.bukkit.event.Event

fun <E : Event> E.callEvent(): E {
    Bukkit.getPluginManager().callEvent(this)
    return this
}