/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.listeners;

import me.dretax.quester.Quester;
import me.dretax.quester.quests.QuestPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 *
 * @author DreTaX
 */
public class QuestListener implements Listener{
	private Quester quester;

	public QuestListener(Quester aThis) {
		quester = aThis;
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		QuestPlayer.broadcastEvent(event);
	}
	
	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		QuestPlayer.broadcastEvent(event);
	}
	
	@EventHandler
	public void onBlockDamage(BlockBreakEvent event) {
		QuestPlayer.broadcastEvent(event);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		QuestPlayer.broadcastEvent(event);
	}
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		QuestPlayer.broadcastEvent(event);
	}
	
}
