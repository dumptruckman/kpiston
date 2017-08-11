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

import org.bukkit.Location
import org.bukkit.Utility
import org.bukkit.World
import org.bukkit.util.NumberConversions
import org.bukkit.util.Vector
import java.util.HashMap

class ImmutableStableLocation(override var worldName: CharSequence? = null,
                              x: Double = 0.0, y: Double = 0.0, z: Double = 0.0,
                              yaw: Float = 0F, pitch: Float = 0F)
    : StableLocation(worldName, x, y, z, yaw, pitch) {

    constructor(l: Location) : this(l.world?.name, l.x, l.y, l.z, l.yaw, l.pitch)
    constructor(world: World?, x: Double = 0.0, y: Double = 0.0, z: Double = 0.0, yaw: Float = 0F, pitch: Float = 0F)
            : this(world?.name, x, y, z, yaw, pitch)

    override fun setWorld(world: World?) {
        throw UnsupportedOperationException()
    }

    override fun setPitch(pitch: Float) {
        throw UnsupportedOperationException()
    }

    override fun setY(y: Double) {
        throw UnsupportedOperationException()
    }
    override fun setYaw(yaw: Float) {
        throw UnsupportedOperationException()
    }

    override fun setZ(z: Double) {
        throw UnsupportedOperationException()
    }

    override fun setX(x: Double) {
        throw UnsupportedOperationException()
    }

    override fun multiply(m: Double) = ImmutableStableLocation(worldName, x * m, y * m, z * m, yaw, pitch)

    override fun add(vec: Location): ImmutableStableLocation {
        if (vec.world != world) {
            throw IllegalArgumentException("Cannot add Locations of differing worlds")
        }
        return ImmutableStableLocation(worldName, x + vec.x, y + vec.y, z + vec.z, yaw, pitch)
    }

    override fun add(vec: Vector) = ImmutableStableLocation(worldName, x + vec.x, y + vec.y, z + vec.z, yaw, pitch)

    override fun add(x: Double, y: Double, z: Double) = ImmutableStableLocation(worldName, this.x + x, this.y + y,
            this.z + z, yaw, pitch)

    override fun subtract(vec: Location): ImmutableStableLocation {
        if (vec.world != world) {
            throw IllegalArgumentException("Cannot subtract Locations of differing worlds")
        }
        return ImmutableStableLocation(worldName, x - vec.x, y - vec.y, z - vec.z, yaw, pitch)
    }

    override fun subtract(vec: Vector) = ImmutableStableLocation(worldName, x - vec.x, y - vec.y, z - vec.z, yaw, pitch)

    override fun subtract(x: Double, y: Double, z: Double) = ImmutableStableLocation(worldName, this.x - x,
            this.y - y, this.z - z, yaw, pitch)

    override fun setDirection(vector: Vector): ImmutableStableLocation {
        /*
         * Sin = Opp / Hyp
         * Cos = Adj / Hyp
         * Tan = Opp / Adj
         *
         * x = -Opp
         * z = Adj
         */
        val _2PI = 2 * Math.PI
        val x = vector.x
        val z = vector.z

        val pitch: Float

        if (x == 0.0 && z == 0.0) {
            pitch = (if (vector.y > 0) -90 else 90).toFloat()
            return ImmutableStableLocation(worldName, this.x, this.y, this.z, this.yaw, pitch)
        }

        val yaw: Float

        val theta = Math.atan2(-x, z)
        yaw = Math.toDegrees((theta + _2PI) % _2PI).toFloat()

        val x2 = NumberConversions.square(x)
        val z2 = NumberConversions.square(z)
        val xz = Math.sqrt(x2 + z2)
        pitch = Math.toDegrees(Math.atan(-vector.y / xz)).toFloat()

        return ImmutableStableLocation(worldName, this.x, this.y, this.z, yaw, pitch)
    }

    override fun zero() = ImmutableStableLocation(worldName, 0.0, 0.0, 0.0, yaw, pitch)

    fun toStableLocation() = StableLocation(this)

    override fun clone() = super.clone() as ImmutableStableLocation

    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (this::class.java != obj::class.java) {
            return false
        }
        val other = obj as Location

        if (this.world !== other.world && (this.world == null || this.world != other.world)) {
            return false
        }
        if (java.lang.Double.doubleToLongBits(this.x) != java.lang.Double.doubleToLongBits(other.x)) {
            return false
        }
        if (java.lang.Double.doubleToLongBits(this.y) != java.lang.Double.doubleToLongBits(other.y)) {
            return false
        }
        if (java.lang.Double.doubleToLongBits(this.z) != java.lang.Double.doubleToLongBits(other.z)) {
            return false
        }
        if (java.lang.Float.floatToIntBits(this.pitch) != java.lang.Float.floatToIntBits(other.pitch)) {
            return false
        }
        if (java.lang.Float.floatToIntBits(this.yaw) != java.lang.Float.floatToIntBits(other.yaw)) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var hash = 3

        hash = 19 * hash + (this.world?.hashCode() ?: 0)
        hash = 19 * hash + (java.lang.Double.doubleToLongBits(this.x) xor java.lang.Double.doubleToLongBits(this.x).ushr(32)).toInt()
        hash = 19 * hash + (java.lang.Double.doubleToLongBits(this.y) xor java.lang.Double.doubleToLongBits(this.y).ushr(32)).toInt()
        hash = 19 * hash + (java.lang.Double.doubleToLongBits(this.z) xor java.lang.Double.doubleToLongBits(this.z).ushr(32)).toInt()
        hash = 19 * hash + java.lang.Float.floatToIntBits(this.pitch)
        hash = 19 * hash + java.lang.Float.floatToIntBits(this.yaw)
        return hash
    }

    override fun toString(): String {
        return "ImmutableStableLocation{world=$world,x=$x,y=$y,z=$z,pitch=$pitch,yaw=$yaw}"
    }

    @Utility
    override fun serialize(): Map<String, Any?> {
        val data = HashMap<String, Any?>()
        if (worldName != null) data.put("world", this.worldName.toString())

        data.put("x", this.x)
        data.put("y", this.y)
        data.put("z", this.z)

        data.put("yaw", this.yaw)
        data.put("pitch", this.pitch)

        return data
    }

    companion object {

        @JvmStatic
        fun deserialize(args: Map<String, Any>): ImmutableStableLocation {
            val worldName = args["world"] as? String

            return ImmutableStableLocation(worldName, NumberConversions.toDouble(args["x"]),
                    NumberConversions.toDouble(args["y"]), NumberConversions.toDouble(args["z"]),
                    NumberConversions.toFloat(args["yaw"]), NumberConversions.toFloat(args["pitch"]))
        }
    }

}