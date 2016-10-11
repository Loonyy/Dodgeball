package minigame.dodgeball.object;

import java.util.HashMap;

import org.bukkit.entity.Player;

import minigame.dodgeball.storage.Serializer;
import minigame.dodgeball.storage.StorageUtil;

public class DBPlayer extends Serializer{
	
	private static HashMap<Player, DBPlayer> players = new HashMap<>();

	private Player player;
	private boolean hasDodgeball;
	private DodgeballTeam team;
	
	public DBPlayer(Player player, DodgeballTeam team) {
		super(StorageUtil.getJSONFile("/players/", player.getUniqueId().toString()));
	}
	
	public static DBPlayer fromPlayer(Player player) {
		return players.containsKey(player) ? players.get(player) : null;
	}
	
	public static DBPlayer loadPlayer(Player player) {
		DodgeballTeam team = null;
		
		for (DodgeballTeam check : DodgeballTeam.getAllTeams()) {
			if (check.isMember(player)) {
				team = check;
				break;
			}
		}
		
		return new DBPlayer(player, team);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public DodgeballTeam getTeam() {
		return team;
	}
	
	public void setTeam(DodgeballTeam team) {
		this.team = team;
	}
	
	public boolean hasDodgeball() {
		return hasDodgeball;
	}
}
