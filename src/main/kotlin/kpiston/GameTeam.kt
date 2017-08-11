package kpiston

class GameTeam<G : Game<G>>(val game: G, val id: Any, val name: CharSequence? = null) {

    val players: PlayerList<G> = TeamPlayerList(game, this)
}