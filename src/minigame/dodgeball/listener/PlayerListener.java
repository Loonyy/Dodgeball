package minigame.dodgeball.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import minigame.dodgeball.object.DBPlayer;

public class PlayerListener  implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		new DBPlayer(player);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onQuit(PlayerQuitEvent event) {
		DBPlayer.get(event.getPlayer()).unload();
	}

}
