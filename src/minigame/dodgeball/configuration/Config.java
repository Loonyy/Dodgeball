package minigame.dodgeball.configuration;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import minigame.dodgeball.Dodgeball;

public class Config {

	private Dodgeball plugin;
	private File file;
	private FileConfiguration config;

	public Config(File file) {
		plugin = Dodgeball.plugin();
		this.file = new File(plugin.getDataFolder() + File.pathSeparator + file);
		config = YamlConfiguration.loadConfiguration(this.file);
		reload();
	}
	
	public Config(File parent, File file) {
		plugin = Dodgeball.plugin();
		this.file = new File(parent + File.pathSeparator + file);
		config = YamlConfiguration.loadConfiguration(this.file);
		reload();
	}

	public void create() {
		if (!file.getParentFile().exists()) {
			try {
				file.getParentFile().mkdir();
			} catch (Exception e) {
				plugin.getLogger().info("Failed to generate directory!");
				e.printStackTrace();
			}
		}

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				plugin.getLogger().info("Failed to generate " + file.getName() + "!");
				e.printStackTrace();
			}
		}
	}

	public FileConfiguration get() {
		return config;
	}
	
	public void reload() {
		create();
		try {
			config.load(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			config.options().copyDefaults(true);
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean delete() {
		try {
			if (file.delete()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
