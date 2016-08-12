package minigame.dodgeball;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class Dodgeball extends JavaPlugin{

	private static Dodgeball plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
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
