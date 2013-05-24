/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.rewards;

import java.io.DataOutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import me.dretax.quester.Quester;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericWidget;
import org.getspout.spoutapi.gui.WidgetAnchor;

/**
 *
 * @author DreTaX
 */
public class MoneyReward extends Reward{
	private Integer amount;

	@Override
	public void load(Map<String, Object> oneRew) {
		super.load(oneRew);
		amount = (Integer) oneRew.get("amount");
	}

	@Override
	public void finish(Player player) {
		Quester.getEconomy().depositPlayer(player.getName(), amount);
	}
	
	@Override
	public String toString() {
		return "MoneyReward: "+amount;
	}

	@Override
	public String format() {
		return Quester.getEconomy().format(amount) + " " + Quester.getEconomy().currencyNamePlural();
	}

	@Override
	public Set<?> createWidgets(int sx, int sy) {
		Set<GenericWidget> toRet = new HashSet<GenericWidget>();
		
		GenericLabel howMany = new GenericLabel(ChatColor.YELLOW+format());
		howMany.setAnchor(WidgetAnchor.CENTER_CENTER);
		howMany.shiftXPos(sx).shiftYPos(sy);
		howMany.setHeight(15).setWidth(GenericLabel.getStringWidth(howMany.getText()));
		
		toRet.add(howMany);
		return toRet;
	}
	
}
