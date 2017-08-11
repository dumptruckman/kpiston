package kpiston.metadata

import com.dumptruckman.bukkit.metadata.MetadataKey
import kpiston.GamePlayer
import kpiston.GameTeam

object PlayerKeys {

    val GAME_PLAYER = MetadataKey(GamePlayer::class)
    val GAME_TEAM = MetadataKey(GameTeam::class)
}