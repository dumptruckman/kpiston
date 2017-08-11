package kpiston

class TeamList<G : Game<G>> protected constructor (val game: G) : Iterable<GameTeam<out G>> {

    private val teams = mutableMapOf<Any, GameTeam<out G>>()

    val count: Int
        get() = teams.size

    fun createTeam(id: Any, name: CharSequence? = null): GameTeam<G> {
        val team = GameTeam(game, id, name)
        teams[id] = team
        return team
    }

    override fun iterator() = teams.values.iterator()

    operator fun get(id: Any) = getTeam(id)

    fun getTeam(id: Any) = teams[id]

    operator fun contains(id: Any) = teams.containsKey(id)
}