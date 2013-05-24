/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.rewards;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.gui.GenericItemWidget;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericWidget;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.inventory.SpoutItemStack;

/**
 *
 * @author DreTaX
 */
public class ItemReward extends Reward {

	private ItemStack is;

	@Override
	public void load(Map<String, Object> toLoad) {
		super.load(toLoad);
		Integer id = (Integer) toLoad.get("item-id");
		Integer data = (Integer) toLoad.get("item-data");
		Integer amount = (Integer) toLoad.get("item-amount");
		is = new SpoutItemStack(id, amount, data.shortValue(), null);
	}

	@Override
	public void finish(Player player) {
		player.getInventory().addItem(is.clone());
	}

	@Override
	public String format() {
		return is.getAmount() + "*" + is.getType().name().toLowerCase().replace("_", " ");
	}

	@Override
	public String toString() {
		return "ItemStack: " + is.getType().name() + " " + is.getAmount();
	}

	@Override
	public Set<?> createWidgets(int sx, int sy) {
		Set<GenericWidget> toRet = new HashSet<GenericWidget>();
		GenericItemWidget giw = new GenericItemWidget(is);
		giw.setAnchor(WidgetAnchor.CENTER_CENTER);
		giw.shiftXPos(sx).shiftYPos(sy - 5);
		giw.setHeight(15).setWidth(15);

		GenericLabel howMany = new GenericLabel("" + is.getAmount());
		howMany.setAnchor(WidgetAnchor.CENTER_CENTER);
		howMany.shiftXPos(sx + 20).shiftYPos(sy);
		howMany.setHeight(15).setWidth(GenericLabel.getStringWidth(howMany.getText()));

		toRet.add(giw);
		toRet.add(howMany);
		return toRet;
	}
}
