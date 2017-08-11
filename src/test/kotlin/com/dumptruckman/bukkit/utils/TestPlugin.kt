package com.dumptruckman.bukkit.utils

import kpiston.GamesManager
import kpiston.metadata.BukkitMetadata
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPluginLoader

import java.io.File
import java.io.InputStream
import java.util.logging.Logger

open class TestPlugin : Plugin {

    val gamesManager by lazy { GamesManager(this) }

    private val _server = MockServerFactory.createMockedServer()
    private val _pluginLoader = JavaPluginLoader(_server)

    private val classLoader = ClassLoader.getSystemClassLoader()

    private val dataFolder = File("./build/server/plugins/dataFolder")
    private val description = PluginDescriptionFile("TestPlugin", "1.0", "com.dumptruckman.bukkit.utils.TestPlugin")

    override fun getServer() = _server
    override fun getPluginLoader() = _pluginLoader

    override fun getDataFolder(): File {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }
        return dataFolder
    }

    override fun getDescription() = description

    override fun getConfig(): FileConfiguration? {
        return null
    }

    override fun getResource(filename: String): InputStream? {
        return null
    }

    override fun saveConfig() {

    }

    override fun saveDefaultConfig() {

    }

    override fun saveResource(resourcePath: String, replace: Boolean) {

    }

    override fun reloadConfig() {

    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun onDisable() {
        BukkitMetadata.clearAllMetadata()
    }

    override fun onLoad() {

    }

    override fun onEnable() {

    }

    override fun isNaggable(): Boolean {
        return false
    }

    override fun setNaggable(canNag: Boolean) {

    }

    override fun getDefaultWorldGenerator(worldName: String, id: String): ChunkGenerator? {
        return null
    }

    override fun getLogger(): Logger? {
        return null
    }

    override fun getName(): String? {
        return "Test-Plugin"
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String>? {
        return null
    }
}
