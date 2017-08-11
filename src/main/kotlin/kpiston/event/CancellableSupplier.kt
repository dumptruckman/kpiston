package kpiston.event

import org.bukkit.event.Cancellable

class CancellableSupplier : Cancellable {

    private var cancel = false

    override fun setCancelled(cancelled: Boolean) {
        this.cancel = cancelled
    }

    override fun isCancelled() = cancel
}
