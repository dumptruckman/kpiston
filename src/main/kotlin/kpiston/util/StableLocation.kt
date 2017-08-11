package kpiston.util

import org.bukkit.*
import org.bukkit.block.Block
import java.util.HashMap
import org.bukkit.util.NumberConversions
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.util.Vector

/**
 * A [Location] object that does not store the world object. This makes StableLocation ideal as a [Map] key that
 * can avoid ghosting objects such as blocks.
 */
open class StableLocation(open var worldName: CharSequence? = null,
                     x: Double = 0.0, y: Double = 0.0, z: Double = 0.0,
                     yaw: Float = 0F, pitch: Float = 0F)
    : Location(null, x, y, z, yaw, pitch) {

    constructor(l: Location) : this(l.world?.name, l.x, l.y, l.z, l.yaw, l.pitch)
    constructor(world: World?, x: Double = 0.0, y: Double = 0.0, z: Double = 0.0, yaw: Float = 0F, pitch: Float = 0F)
            : this(world?.name, x, y, z, yaw, pitch)

    override fun getWorld(): World? = if (worldName != null) Bukkit.getWorld(worldName.toString()) else null

    override fun setWorld(world: World?) {
        worldName = world?.name
    }

    override fun getChunk(): Chunk? = world?.getChunkAt(this)

    override fun getBlock(): Block? = world?.getBlockAt(this)

    override fun multiply(m: Double): StableLocation {
        return super.multiply(m) as StableLocation
    }

    override fun add(vec: Location): StableLocation {
        return super.add(vec) as StableLocation
    }

    override fun add(vec: Vector): StableLocation {
        return super.add(vec) as StableLocation
    }

    override fun add(x: Double, y: Double, z: Double): StableLocation {
        return super.add(x, y, z) as StableLocation
    }

    override fun subtract(vec: Location): StableLocation {
        return super.subtract(vec) as StableLocation
    }

    override fun subtract(vec: Vector): StableLocation {
        return super.subtract(vec) as StableLocation
    }

    override fun subtract(x: Double, y: Double, z: Double): StableLocation {
        return super.subtract(x, y, z) as StableLocation
    }

    override fun setDirection(vector: Vector): StableLocation {
        return super.setDirection(vector) as StableLocation
    }

    override fun zero(): StableLocation {
        return super.zero() as StableLocation
    }

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
        return "StableLocation{world=$world,x=$x,y=$y,z=$z,pitch=$pitch,yaw=$yaw}"
    }

    override fun clone(): StableLocation {
        return super.clone() as StableLocation
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
        fun deserialize(args: Map<String, Any>): StableLocation {
            val worldName = args["world"] as? String

            return StableLocation(worldName, NumberConversions.toDouble(args["x"]),
                    NumberConversions.toDouble(args["y"]), NumberConversions.toDouble(args["z"]),
                    NumberConversions.toFloat(args["yaw"]), NumberConversions.toFloat(args["pitch"]))
        }
    }

}