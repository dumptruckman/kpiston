package org.bukkit.configuration.file

import org.bukkit.configuration.Configuration
import org.bukkit.configuration.ConfigurationSection
import java.io.File
import java.io.Reader

/**
 * A wrapper for YamlConfiguration that does not allow saving to file.
 */
class UnsaveableYamlConfiguration(val baseConfig: YamlConfiguration) : YamlConfiguration(), Configuration by baseConfig {

    override fun setDefaults(defaults: Configuration?) {
        baseConfig.defaults = defaults
    }

    override fun save(file: File?) {
        throw UnsupportedOperationException("This config cannot be saved.")
    }

    override fun save(file: String?) {
        throw UnsupportedOperationException("This config cannot be saved.")
    }

    override fun loadFromString(contents: String?) {
        baseConfig.loadFromString(contents)
    }

    override fun saveToString(): String {
        throw UnsupportedOperationException("This config cannot be saved.")
    }

    override fun parseHeader(input: String?): String {
        return baseConfig.parseHeader(input)
    }

    override fun getDefaults(): Configuration {
        return baseConfig.defaults
    }

    override fun buildHeader(): String {
        return baseConfig.buildHeader()
    }

    override fun getParent(): ConfigurationSection {
        return baseConfig.parent
    }

    override fun options(): YamlConfigurationOptions {
        return baseConfig.options()
    }

    override fun addDefaults(defaults: Configuration?) {
        baseConfig.addDefaults(defaults)
    }

    override fun addDefaults(defaults: MutableMap<String, Any>?) {
        baseConfig.addDefaults(defaults)
    }

    override fun addDefault(path: String?, value: Any?) {
        baseConfig.addDefault(path, value)
    }

    override fun convertMapsToSections(input: MutableMap<*, *>?, section: ConfigurationSection?) {
        baseConfig.convertMapsToSections(input, section)
    }

    override fun load(file: File?) {
        baseConfig.load(file)
    }

    override fun load(reader: Reader?) {
        baseConfig.load(reader)
    }

    override fun load(file: String?) {
        baseConfig.load(file)
    }
}
