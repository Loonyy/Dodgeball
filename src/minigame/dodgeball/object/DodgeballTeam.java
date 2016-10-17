package minigame.dodgeball.object;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import minigame.dodgeball.Dodgeball;
import minigame.dodgeball.storage.Serializer;
import minigame.dodgeball.storage.StorageUtil;

public class DodgeballTeam extends Serializer {
	
	private static HashMap<UUID, DodgeballTeam> uuidMap = new HashMap<>();
	
	@SerializedName("name")
	@Expose @Since(1.0)
	private String name;
	@SerializedName("color")
	@Expose @Since(1.0)
	private int[] color;
	@SerializedName("uuid")
	@Expose @Since(1.0)
	private UUID uuid;
	@SerializedName("members")
	@Expose @Since(1.0)
	private List<UUID> members = new ArrayList<>();
	@SerializedName("leader")
	@Expose @Since(1.0)
	private UUID leader;
	
	/**
	 * Making a new team that doesn't already exist and only has a leader
	 * @param name Name of team
	 * @param color RGB int[] for color
	 */
	public DodgeballTeam(String name, int[] color, Player leader) {
		this(name, color, UUID.randomUUID(), Arrays.asList(leader.getUniqueId()), leader);
	}
	
	public DodgeballTeam(String name, int[] color, UUID uuid, List<UUID> members, Player leader) {
		super (StorageUtil.getJSONFile("/teams/", uuid.toString()));
		this.name = name;
		this.color = color;
		this.uuid = uuid;
		this.members = members;
		this.leader = leader.getUniqueId();
		uuidMap.put(uuid, this);
		serialize();
	}

	public String getName() {
		return name;
	}
	
	public int[] getColorRGB() {
		return color;
	}
	
	public UUID getUniqueID() {
		return uuid;
	}
	
	public UUID getLeader() {
		return leader;
	}
	
	public void setLeader(UUID leader) {
		this.leader = leader;
	}
	
	public boolean isLeader(UUID leader) {
		return this.leader.equals(leader);
	}
	
	public List<UUID> getMembers() {
		return members;
	}
	
	public void addMember(UUID uuid) {
		if (!members.contains(uuid)) {
			members.add(uuid);
		}
	}
	
	public void removeMember(UUID uuid) {
		if (members.contains(uuid)) {
			members.remove(uuid);
		}
	}
	
	public boolean isMember(UUID uuid) {
		return members.contains(uuid);
	}
	
	public void unload() {
		serialize();
		uuidMap.remove(uuid);
	}
	
	public static Collection<DodgeballTeam> getAllTeams() {
		return uuidMap.values();
	}
	
	/**
	 * Gets an instance of a DodgeballTeam
	 * @param uuid
	 * @return
	 */
	public static DodgeballTeam get(UUID uuid) {
		if (!uuidMap.containsKey(uuid)) {
			DodgeballTeam team = (DodgeballTeam) Serializer.get(DodgeballTeam.class, StorageUtil.getJSONFile("/teams/", uuid.toString()));
			if (team == null) {
				return null;
			}
			uuidMap.put(uuid, team);
		}
		return uuidMap.get(uuid);
	}
	
	/**
	 * Loads all instances of saved DodgeballTeams
	 */
	public static void loadAll() {
		File teamsFolder = new File(Dodgeball.plugin().getDataFolder() + File.separator + "teams");
		if (!teamsFolder.exists()) {
			return;
		} else {
			Arrays.asList(teamsFolder.list()).stream().filter(s -> s.endsWith(".json")).forEach(s -> {
				s = s.replace(".json", "");
				if (s.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
					get(UUID.fromString(s));
				}
			});
		}
	}

	/**
	 * Unloads all the teams and saves them
	 */
	public static void unloadAll() {
		uuidMap.values().stream().forEach(player -> player.serialize());
		uuidMap.clear();
	}
}
