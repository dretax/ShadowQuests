/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.objectives;

import java.io.DataOutputStream;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author DreTaX
 */
public class GatherObjective extends Objective{
	
	private ItemStack is;
	
	private GatherObjective(ItemStack is) {
		this.is = is;
	}
	
	public GatherObjective() {
	}

	@Override
	public void load(Map<String, Object> toLoad) {
		Integer id = (Integer) toLoad.get("item-id");
		Integer amount = (Integer) toLoad.get("item-amount");
		is = new ItemStack(Material.getMaterial(id), amount);
		
	}

	@Override
	public void finish(Player player) {
		player.getInventory().remove(is);
	}

	@Override
	public Objective copyForActive(Player plr) {
		return new GatherObjective(is.clone());
	}

	@Override
	public boolean isFinished(Player plr) {
		return plr.getInventory().contains(is.getType(), is.getAmount());
	}

	@Override
	public String toString() {
		return "GatherObjective: "+is.getTypeId()+"-"+is.getAmount();
	}

	@Override
	public String format() {
		return "Gather "+is.getAmount()+"*"+is.getType().name().toLowerCase().replace("_", " ");
	}
}
