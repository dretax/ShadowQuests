/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.objectives;

import java.io.DataOutputStream;
import java.util.Map;
import me.dretax.quester.quests.QuestPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 *
 * @author DreTaX
 */
public class KillObjective extends Objective {

	private Integer amount;

	private KillObjective(Integer amount) {
		this.amount = amount;
	}

	public KillObjective() {
	}

	@Override
	public void load(Map<String, Object> data) {
		amount = (Integer) data.get("amount");
	}

	@Override
	public Objective copyForActive(Player plr) {
		return new KillObjective(amount);
	}

	@Override
	public boolean isFinished(Player plr) {
		return amount == 0;
	}

	@Override
	public void onEvent(QuestPlayer quester, Event event) {
		if (event instanceof EntityDeathEvent) {
			EntityDeathEvent eevent = (EntityDeathEvent) event;
			if (eevent.getEntityType() != EntityType.PLAYER) {
				return;
			}
			EntityDamageEvent ede = eevent.getEntity().getLastDamageCause();
			if (ede instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) ede;
				Entity killer = edbee.getDamager();
				if (killer instanceof Player) {
					Player pkiller = (Player) killer;
					if (pkiller.getName().equals(quester.getName())) {
						if (amount > 0) {
							amount--;
						}
					}
				}
			}
		}
	}

	@Override
	public String format() {
		return "Kill " + amount + " players";
	}
}
