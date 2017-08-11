package kpiston.extensions

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

fun BukkitRunnable.isRunning() = Bukkit.getScheduler().isCurrentlyRunning(this.taskId)