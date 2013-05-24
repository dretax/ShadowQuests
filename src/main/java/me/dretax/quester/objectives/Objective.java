/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.objectives;

import java.io.DataOutputStream;
import java.util.Map;
import me.dretax.quester.quests.QuestPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 *
 * @author DreTaX
 */
public abstract class Objective {
	private Map<String, Object> map;
	
	public void load(Map<String, Object> data) {
		this.map = data;
	}
	
	public Map<String, Object> getSaveMap() {
		return map;
	}
	
	public abstract Objective copyForActive(Player plr);
	
	public abstract boolean isFinished(Player plr);
	
	public abstract String format();
	
	public void onEvent(QuestPlayer quester, Event event) {
	}

	public void finish(Player player) {
		
	}
	
	public String getBlockName(int blockId) {
		return Material.getMaterial(blockId).name().replace("_", " ");
	}

	
}
