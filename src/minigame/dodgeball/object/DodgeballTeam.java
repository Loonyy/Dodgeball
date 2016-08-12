package minigame.dodgeball.object;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import minigame.dodgeball.Dodgeball;
import minigame.dodgeball.configuration.Config;

public class DodgeballTeam {
	
	private static HashMap<String, DodgeballTeam> nameMap = new HashMap<>();
	private static HashMap<UUID, DodgeballTeam> uuidMap = new HashMap<>();
	
	private String name;
	private int[] color;
	private UUID uuid;
	private List<Player> members = new ArrayList<>();
	private Player leader;
	
	/**
	 * Making a new team that doesn't already exist and only has a leader
	 * @param name Name of team
	 * @param color RGB int[] for color
	 */
	public DodgeballTeam(String name, int[] color, Player leader) {
		this(name, color, UUID.randomUUID(), Arrays.asList(leader), leader);
	}
	
	public DodgeballTeam(String name, int[] color, UUID uuid, List<Player> members, Player leader) {
		this.name = name;
		this.color = color;
		this.uuid = uuid;
		this.members = members;
		this.leader = leader;
		nameMap.put(name, this);
		uuidMap.put(uuid, this);
	}

	public static void loadSavedTeams() {
		File folder = new File(Dodgeball.plugin().getDataFolder(), "teams");
		if (!folder.exists()) {
			folder.mkdirs();
			return;
		}
		
		for (File file : folder.listFiles()) {
			Config c = new Config(folder, file);
			String name = file.getName();
			int[] color = new int[] {c.get().getInt("Red"), c.get().getInt("Green"), c.get().getInt("Blue")};
			UUID uuid = UUID.fromString(c.get().getString("UUID"));
			List<Player> members = new ArrayList<>();
			Player leader = null;
			
			for (String uid : c.get().getStringList("Members")) {
				String[] entry = uid.split(",");
				UUID id = UUID.fromString(entry[0]);
				Player player = Bukkit.getPlayer(id);
				
				members.add(player);
				
				if (entry[1].equalsIgnoreCase("Leader")) {
					leader = player;
				}
			}
			
			new DodgeballTeam(name, color, uuid, members, leader);
		}
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
	
	public Player getLeader() {
		return leader;
	}
	
	public void setLeader(Player player) {
		leader = player;
	}
	
	public boolean isLeader(Player player) {
		return (leader.getUniqueId() == player.getUniqueId());
	}
	
	public List<Player> getMembers() {
		return members;
	}
	
	public void addMember(Player player) {
		members.add(player);
	}
	
	public void removeMember(Player player) {
		if (members.contains(player)) {
			members.remove(player);
		}
	}
	
	public boolean isMember(Player player) {
		return members.contains(player);
	}
	
	public static Collection<DodgeballTeam> getAllTeams() {
		return nameMap.values();
	}
}
