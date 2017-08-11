package kpiston.team

import kpiston.Game
import kpiston.player.PlayerList
import kpiston.player.TeamPlayerList

class GameTeam<G : Game<G>>(val game: G, val id: Any, val name: CharSequence? = null) {

    val players: PlayerList<G> = TeamPlayerList(this)
}