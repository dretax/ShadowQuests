/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.rewards;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericWidget;
import org.getspout.spoutapi.gui.WidgetAnchor;

/**
 *
 * @author DreTaX
 */
public class CommandReward extends Reward{
	private String command;

	@Override
	public void load(Map<String, Object> oneRew) {
		command = (String) oneRew.get("command");
	}

	@Override
	public void finish(Player player) {
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("<player>", player.getName()));
	}

	@Override
	public String format() {
		return command;
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
