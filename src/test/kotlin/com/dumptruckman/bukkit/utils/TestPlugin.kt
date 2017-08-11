/*
 * This file is part of kpiston.
 *
 * Copyright (c) 2017 Jeremy Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
