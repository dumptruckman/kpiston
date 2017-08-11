package kpiston.extensions

import org.bukkit.event.Cancellable

val Cancellable.isNotCancelled: Boolean
    get() = !this.isCancelled