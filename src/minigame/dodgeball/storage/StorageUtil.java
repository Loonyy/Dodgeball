package minigame.dodgeball.storage;

import java.io.File;

import minigame.dodgeball.Dodgeball;

public class StorageUtil {

	public static File getJSONFile(String path, String name) {
		File folder = new File(Dodgeball.plugin().getDataFolder(), path);
		return new File(folder, name + ".json");
	}
}
