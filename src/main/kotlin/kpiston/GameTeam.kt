package kpiston

import org.bukkit.plugin.Plugin

class GameTeam<G : Game<out Plugin>>(val game: G, val name: CharSequence) {

    
}