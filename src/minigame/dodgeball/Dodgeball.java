package minigame.dodgeball;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import minigame.dodgeball.listener.PlayerListener;
import minigame.dodgeball.object.Ball;
import minigame.dodgeball.object.DBPlayer;
import minigame.dodgeball.object.DodgeballTeam;

public class Dodgeball extends JavaPlugin{

	private static Dodgeball plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
		DodgeballTeam.loadAll();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			DBPlayer.get(player);
		}
	}
	
	@Override
	public void onDisable() {
		Ball.destroyAll();
		DBPlayer.unloadAll();
		DodgeballTeam.unloadAll();
	}
	
	public static Dodgeball plugin() {
		return plugin;
	}
	
	public static Logger logger() {
		return plugin.getLogger();
	}
}
