/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.objectives;

import java.io.DataOutputStream;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author DreTaX
 */
public class FindObjective extends Objective{
	private String world;
	private Integer x,y,z;
	private Integer radius;
	private boolean finished = false;
	
	protected FindObjective(String world, Integer x, Integer y, Integer z, Integer radius) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
	}
	
	public FindObjective() {
	}

	@Override
	public void load(Map<String, Object> data) {
		world = (String) data.get("world");
		x = (Integer) data.get("x");
		y = (Integer) data.get("y");
		z = (Integer) data.get("z");
		radius = (Integer) data.get("radius");
	}

	@Override
	public Objective copyForActive(Player plr) {
		return new FindObjective(world, x,y,z,radius);
	}

	@Override
	public boolean isFinished(Player plr) {
		return finished;
	}

	@Override
	public String format() {
		return "Find "+x+" "+y+" "+z;
	}
	
	public void setFound(boolean newF) {
		finished = newF;
	}

	public boolean isInRange(Player plr) {
		Location loc = plr.getLocation();
		if(!loc.getWorld().getName().equals(world)) {
			return false;
		}
		return loc.distance(new Location(Bukkit.getWorld(world),x,y,z)) <= radius;
	
	}
	
}
