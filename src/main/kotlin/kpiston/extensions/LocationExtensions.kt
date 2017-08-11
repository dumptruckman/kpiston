package kpiston.extensions

import kpiston.util.ImmutableStableLocation
import kpiston.util.StableLocation
import org.bukkit.Location

fun Location.isStable() = this is StableLocation
fun Location.isImmutable() = this is ImmutableStableLocation
fun Location.isMutable() = this !is ImmutableStableLocation

/**
 * Casts this location as [StableLocation] if not immutable, else creates a new [StableLocation] for this location.
 */
fun Location.asStableLocation() = if (isStable() && isMutable()) this as StableLocation else StableLocation(this)

/**
 * Casts this location as [ImmutableStableLocation] if immutable, else creates a new [ImmutableStableLocation] for this
 * location.
 */
fun Location.asImmutableLocation() = if (isImmutable()) this as ImmutableStableLocation else ImmutableStableLocation(this)