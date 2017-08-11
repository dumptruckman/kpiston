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
package kpiston.jdbc

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.io.File

class SpringJdbcAgent(val settings: DatabaseSettings, val dataSource: DriverManagerDataSource) {

    /**
     * A convenience method for creating JdbcTemplate which allows for very intuitive database access.
     */
    fun createJdbcTemplate(): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }

    companion object {
        /**
         * Initializes a SpringJdbcAgent, starting a connection to a database based on the given settings.
         *
         * @param settings The settings which indicate how and what database to connect to.
         * @param databaseFolder A folder which will contain any embedded database.  This may or may not be used depending on the given settings.
         * @param classLoader This is the ClassLoader that will be used to load the database Driver class.
         * @return a new SpringJdbcAgent.
         * @throws ClassNotFoundException if the driver specified in settings is not found.
         */
        @Throws(ClassNotFoundException::class)
        fun createAgent(settings: DatabaseSettings, databaseFolder: File, classLoader: ClassLoader): SpringJdbcAgent {
            var dbType = settings.type
            var url = settings.connectionInfo.url
            if (dbType.equals("H2", ignoreCase = true)) {
                dbType = "org.h2.Driver"
                if (!url.startsWith("jdbc")) {
                    url = "jdbc:h2:" + File(databaseFolder, url).path
                }
            } else if (dbType.equals("MySQL", ignoreCase = true)) {
                dbType = "com.mysql.jdbc.Driver"
            } else if (dbType.equals("SQLite", ignoreCase = true)) {
                dbType = "org.sqlite.JDBC"
                if (!url.startsWith("jdbc")) {
                    url = "jdbc:sqlite:" + File(databaseFolder, url).path
                }
            }
            val previousClassLoader = Thread.currentThread().contextClassLoader
            Thread.currentThread().contextClassLoader = classLoader
            val dataSource = DriverManagerDataSource()
            dataSource.setDriverClassName(dbType)
            dataSource.url = url
            dataSource.username = settings.connectionInfo.user
            dataSource.password = settings.connectionInfo.pass
            Thread.currentThread().contextClassLoader = previousClassLoader
            return SpringJdbcAgent(settings, dataSource)
        }
    }
}