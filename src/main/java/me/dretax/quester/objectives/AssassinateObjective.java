/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.objectives;

import java.io.DataOutputStream;
import java.util.Map;
import me.dretax.quester.quests.QuestPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 *
 * @author DreTaX
 */
public class AssassinateObjective extends Objective{
	private boolean killed = false;
	
	public AssassinateObjective() {
	}

	@Override
	public void load(Map<String, Object> data) {
	}

	@Override
	public Objective copyForActive(Player plr) {
		return new AssassinateObjective();
	}

	@Override
	public void onEvent(QuestPlayer quester, Event event) {
		if(killed) {
			return;
		}
		if(event instanceof EntityDeathEvent) {
			EntityDeathEvent ede = (EntityDeathEvent) event;
			if(!(ede.getEntity() instanceof Player)) {
				return;
			}
			Player pkilled = (Player) ede.getEntity();
			EntityDamageEvent eve = ede.getEntity().getLastDamageCause();
			if(eve instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) eve;
				Entity killer = edbee.getDamager();
				if(!(killer instanceof Player)) {
					return;
				}
				Player pkiller = (Player) killer;
				if(!pkiller.getName().equals(quester.getName())) {
					return;
				}
				eve = pkiller.getLastDamageCause();
				if(eve instanceof EntityDamageByEntityEvent) {
					edbee = (EntityDamageByEntityEvent) eve;
					if(edbee.getDamager() instanceof Player) {
						if(((Player) edbee.getDamager()).getName().equals(pkilled.getName())) {
							return;
						}
					}
				}
				killed = true;
			}
		}
	}

	@Override
	public boolean isFinished(Player plr) {
		return killed;
	}

	@Override
	public String format() {
		return "Assassinate someone";
	}
	
}
