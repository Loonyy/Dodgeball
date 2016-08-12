package minigame.dodgeball;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import minigame.dodgeball.object.DBPlayer;
import minigame.dodgeball.object.DodgeballTeam;

public class Dodgeball extends JavaPlugin{

	private static Dodgeball plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		DodgeballTeam.loadSavedTeams();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			DBPlayer.loadPlayer(player);
		}
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static Dodgeball plugin() {
		return plugin;
	}
	
	public static Logger logger() {
		return plugin.getLogger();
	}
}
