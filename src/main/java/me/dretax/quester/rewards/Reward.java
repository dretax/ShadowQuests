/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.rewards;

import java.io.DataOutputStream;
import java.util.Map;
import java.util.Set;
import org.bukkit.entity.Player;

/**
 *
 * @author DreTaX
 */
public abstract class Reward {
	
	private Map<String, Object> map;

	public void load(Map<String, Object> oneRew) {
		this.map = oneRew;
	}
	
	public Map<String, Object> getSaveMap() {
		return map;
	}
	
	public abstract void finish(Player player);

	public abstract String format();

	public abstract Set<?> createWidgets(int sx, int sy);
	
}
