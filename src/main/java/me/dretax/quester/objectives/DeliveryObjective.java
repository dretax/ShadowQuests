/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.objectives;

import java.io.DataOutputStream;
import java.util.Map;
import me.dretax.quester.quests.QuestPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.getspout.spoutapi.inventory.SpoutItemStack;

/**
 *
 * @author DreTaX
 */
public class DeliveryObjective extends Objective {

	private String nname;
	private SpoutItemStack is;
	private boolean finished, giveItem;

	protected DeliveryObjective() {
	}

	private DeliveryObjective(String nname, SpoutItemStack is) {
		this.nname = nname;
		this.is = is;
		this.finished = false;
	}

	@Override
	public void load(Map<String, Object> toLoad) {
		Integer id = (Integer) toLoad.get("item-id");
		Integer data = (Integer) toLoad.get("item-data");
		Integer amount = (Integer) toLoad.get("item-amount");
		giveItem = (Boolean) toLoad.get("give-item");
		is = new SpoutItemStack(id, amount, data.shortValue(), null);
		nname = (String) toLoad.get("npc-name");
	}

	@Override
	public Objective copyForActive(Player plr) {
		if(giveItem) {
			plr.getInventory().addItem(is.clone());
		}
		return new DeliveryObjective(nname, is);
	}

	@Override
	public boolean isFinished(Player plr) {
		return finished;
	}

	@Override
	public String format() {
		return "Deliver " + is.getAmount() + " x " + is.getMaterial().getName();
	}

	@Override
	public void onEvent(QuestPlayer quester, Event event) {
		if (finished) {
			return;
		}
		if (event instanceof PlayerInteractEntityEvent) {
			PlayerInteractEntityEvent piee = (PlayerInteractEntityEvent) event;
			if (piee.getRightClicked() instanceof Player) {
				Player pplr = (Player) piee.getRightClicked();
				if (pplr.getName().equals(nname)) {
					if (!contains(pplr, pplr.getInventory())) {
						piee.getPlayer().sendMessage(ChatColor.RED+"You do not have the item in your inventory!");
						return;
					}
					piee.getPlayer().getInventory().remove(is);
					piee.getPlayer().sendMessage(ChatColor.GREEN + "Item delivered!");
					finished = true;
				}
			}
		}
	}
	private boolean contains(Player plr, PlayerInventory inventory) {
		int amountNeeded = is.getAmount();
		plr.sendMessage("Amount needed:"+amountNeeded);
		for(ItemStack iss : inventory.getContents()) {
			if(iss == null) {
				continue;
			}
			plr.sendMessage("Checking "+iss.getTypeId()+" "+iss.getDurability()+" with "+is.getType()+" "+is.getDurability());
			
			if(iss.getTypeId() == is.getTypeId() && iss.getDurability()== is.getDurability()) {
				amountNeeded-=iss.getAmount();
				plr.sendMessage("Still need "+amountNeeded);
				if(amountNeeded <= 0) {
					return true;
				}
			}
		}
		return false;
	}
}
