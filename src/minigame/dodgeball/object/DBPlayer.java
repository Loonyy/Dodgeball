package minigame.dodgeball.object;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import minigame.dodgeball.Dodgeball;
import minigame.dodgeball.storage.Serializer;
import minigame.dodgeball.storage.StorageUtil;

public class DBPlayer extends Serializer{

	private static HashMap<UUID, DBPlayer> players = new HashMap<>();

	@SerializedName("uuid")
	@Expose @Since(1.0)
	private UUID uuid;
	@SerializedName("username")
	@Expose @Since(1.0)
	private String username;
	@SerializedName("firstlogin")
	@Expose @Since(1.0)
	private long firstlogin;

	private Player player;
	private boolean hasDodgeball;
	private DodgeballTeam team;

	public DBPlayer(Player player) {
		super(StorageUtil.getJSONFile("/players/", player.getUniqueId().toString()));
		if (get(player.getUniqueId()) == null) {
			this.uuid = player.getUniqueId();
			this.username = player.getName();
			this.firstlogin = System.currentTimeMillis();
			players.put(uuid, this);
			serialize();
		} else {
			DBPlayer dbp = get(player.getUniqueId());
			dbp.username = player.getName();
			dbp.serialize();
		}
	}

	public UUID getUniqueId() {
		return uuid;
	}

	public String getName() {
		return username;
	}

	public long getFirstLogin() {
		return firstlogin;
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

	public void unload() {
		serialize();
		players.remove(uuid);
	}

	/**
	 * Gets an instance of a DBPlayer
	 * @param player
	 * @return
	 */
	public static DBPlayer get(OfflinePlayer player) {
		return get(player.getUniqueId());
	}
	
	/**
	 * Gets an instance of a DBPlayer
	 * @param uuid
	 * @return
	 */
	public static DBPlayer get(UUID uuid) {
		if (!players.containsKey(uuid)) {
			DBPlayer player = (DBPlayer) Serializer.get(DBPlayer.class, StorageUtil.getJSONFile("/players/", uuid.toString()));
			if (player == null) {
				return null;
			}
			players.put(uuid, player);
			new BukkitRunnable() {
				@Override
				public void run() {
					if (Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline()) {
						player.unload();
					}
				}
			}.runTaskLater(Dodgeball.plugin(), 60*20l);
		}
		DodgeballTeam.getAllTeams().stream().filter(team -> team.isMember(uuid)).findFirst().ifPresent(team -> players.get(uuid).team = team);
		return players.get(uuid);
	}

	/**
	 * Unloads all the players and saves them
	 */
	public static void unloadAll() {
		players.values().stream().forEach(player -> player.serialize());
		players.clear();
	}
}
