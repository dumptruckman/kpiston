package kpiston.extensions

import org.apache.commons.io.FileUtils
import org.bukkit.Bukkit
import org.bukkit.World
import java.io.File

fun World.delete() = FileUtils.forceDelete(this.worldFolder)

fun World.copy(newName: CharSequence) = FileUtils.copyDirectory(this.worldFolder,
        File(Bukkit.getWorldContainer(), newName.toString()))
