package minigame.dodgeball.object;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

public class Ball {
	
	private static List<Ball> balls = new ArrayList<>();

	private Player holder;
	private Slime ball;
	private boolean active;
	
	public Ball(Player holder, Location loc) {
		this(holder, loc, true);
	}
	
	public Ball(Player holder, Location loc, boolean active) {
		setHolder(holder);
		this.active = active;
		ball = (Slime) loc.getWorld().spawnEntity(loc, EntityType.SLIME);
		ball.setAI(false);
		ball.setSilent(true);
		ball.setCollidable(true);
		ball.setInvulnerable(true);
		ball.setSize(-1);
		balls.add(this);
	}
	
	public Player getHolder() {
		return holder;
	}
	
	public void setHolder(Player holder) {
		this.holder = holder;
	}
	
	public Slime getBall() {
		return ball;
	}
	
	public boolean isActive() {
		return active;
	}
}
