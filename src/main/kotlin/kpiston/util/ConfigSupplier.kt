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
package kpiston.util

import kpiston.state.GameState
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.UnsaveableYamlConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

internal class ConfigSupplier(private val configName: CharSequence, plugin: Plugin) {

    private val dataFolder: File = plugin.dataFolder
    private val classLoader: ClassLoader

    init {
        val classLoaderMethod = plugin::class.java.superclass.getDeclaredField("classLoader")
        classLoaderMethod.isAccessible = true
        classLoader = classLoaderMethod.get(plugin) as ClassLoader
    }

    private val configFile = File(dataFolder, configName.toString())
    private var config: YamlConfiguration? = null

    /**
     * Gets the configuration for this GameState.
     *
     * Note that this configuration object cannot be saved to a file.
     */
    fun getConfig(): FileConfiguration {
        if (config == null) {
            reloadConfig();
        }
        return config!!;
    }

    /**
     * Saves the raw contents of this game state's default config file to the location retrievable by [getConfig].
     *
     * This should fail silently if the config file already exists.
     */
    fun saveDefaultConfig() {
        if (!configFile.exists()) {
            saveResource(configName.toString(), false);
        }
    }

    fun reloadConfig() {
        config = UnsaveableYamlConfiguration(YamlConfiguration.loadConfiguration(configFile))

        val defConfigStream = getResource(configName.toString()) ?: return

        config?.defaults = YamlConfiguration.loadConfiguration(InputStreamReader(defConfigStream, Charsets.UTF_8))
    }

    /**
     * This method is unsupported for [GameState] since multiple instances of this GameState may exist and may be
     * modifying the configuration separately.
     */
    fun saveConfig() {
        throw UnsupportedOperationException("GameState configs may not be saved!")
    }

    fun saveResource(resourcePath: String, replace: Boolean) {
        var resourcePath = resourcePath
        if (resourcePath == "") {
            throw IllegalArgumentException("ResourcePath cannot be empty")
        }

        resourcePath = resourcePath.replace('\\', '/')
        val `in` = getResource(resourcePath) ?: throw IllegalArgumentException("The embedded resource '$resourcePath' cannot be found in plugin jar")

        val outFile = File(dataFolder, resourcePath)
        val lastIndex = resourcePath.lastIndexOf('/')
        val outDir = File(dataFolder, resourcePath.substring(0, if (lastIndex >= 0) lastIndex else 0))

        if (!outDir.exists()) {
            outDir.mkdirs()
        }

        try {
            if (!outFile.exists() || replace) {
                val out = FileOutputStream(outFile)
                val buf = ByteArray(1024)
                var len: Int
                do {
                    len = `in`!!.read(buf)
                    if (len <= 0) break;
                    out.write(buf, 0, len)
                } while (true)
                out.close()
                `in`!!.close()
            } else {
                log.warn("Could not save ${outFile.name} to $outFile because ${outFile.name} already exists.")
            }
        } catch (ex: IOException) {
            log.error(ex) { "Could not save ${outFile.name} to $outFile" }
        }

    }

    fun getResource(filename: String): InputStream? {
        try {
            val url = classLoader.getResource(filename) ?: return null

            val connection = url.openConnection()
            connection.setUseCaches(false)
            return connection.getInputStream()
        } catch (ex: IOException) {
            return null
        }
    }
}
