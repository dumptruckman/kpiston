package kpiston.metadata

import kpiston.player.GamePlayer
import kpiston.team.GameTeam

object PlayerKeys {

    val GAME_PLAYER = MetadataKey(GamePlayer::class)
    val GAME_TEAM = MetadataKey(GameTeam::class)
}