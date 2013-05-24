/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester;

import java.util.List;
import me.dretax.quester.listeners.NPCManager;
import me.dretax.quester.quests.QuestPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 *
 * @author DreTaX
 */
public class NPCListener implements Listener{
	private Quester quester;
	
	public NPCListener(Quester quester) {
		this.quester = quester;
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Entity rced = event.getRightClicked();
		if(!(rced.hasMetadata("NPC"))) {
			return;
		}
		QuestPlayer qp = QuestPlayer.getQuestPlayer(event.getPlayer());
		
		
		qp.setTargetNPC(rced);
		
		
		if(!NPCManager.hasQuests(rced.getEntityId())) {
			event.getPlayer().sendMessage(ChatColor.GOLD+"Selected Quest NPC!");
			return;
		}
		
		List<String> questNames = NPCManager.getQuests(rced.getEntityId());
		
		
		event.getPlayer().sendMessage(ChatColor.GOLD+"Quest list");
		event.getPlayer().sendMessage(ChatColor.GOLD+"------------------------");
		for(int i=0;i<questNames.size();i++) {
			event.getPlayer().sendMessage(""+ChatColor.GREEN+(i+1)+". "+questNames.get(i));
		}
		event.getPlayer().sendMessage(ChatColor.AQUA+"Type /qu info [name] to view info about the quest, or /qu accept [id] to accept it!");
	}
	
}
