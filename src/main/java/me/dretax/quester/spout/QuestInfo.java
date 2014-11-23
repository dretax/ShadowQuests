/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.spout;

import java.util.Set;
import me.dretax.quester.Quester;
import me.dretax.quester.objectives.Objective;
import me.dretax.quester.quests.ActiveQuest;
import me.dretax.quester.quests.QuestPlayer;
import me.dretax.quester.rewards.Reward;
import org.bukkit.ChatColor;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.GenericWidget;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 *
 * @author DreTaX
 */
public class QuestInfo extends GenericPopup { 
	
	public QuestInfo(SpoutPlayer sp, QuestPlayer qu) {
		ActiveQuest aq = qu.getActiveQuest();
		if(aq == null) {
			return;
		}
		
		int sx = -180, sy = -140;
		
		int tsx = sx, tsy = -140;
		
		GenericLabel title = new GenericLabel("Quest: "+aq.getName());
		title.setAnchor(WidgetAnchor.CENTER_CENTER);
		title.setHeight(30).setWidth(GenericLabel.getStringWidth(title.getText(), 2.0f));
		title.shiftXPos(sx).shiftYPos(sy);
		title.setScale(2.0f);
		
		sy+=30;
		GenericLabel cstart = new GenericLabel(aq.getStartMessage());
		cstart.setAnchor(WidgetAnchor.CENTER_CENTER);
		cstart.setHeight(15).setWidth(GenericLabel.getStringWidth(cstart.getText()));
		cstart.shiftXPos(sx).shiftYPos(sy);
		
		sy+=27;
		GenericLabel otitle = new GenericLabel("Objectives: ");
		otitle.setAnchor(WidgetAnchor.CENTER_CENTER);
		otitle.setScale(1.5f);
		otitle.setHeight(23).setWidth(GenericLabel.getStringWidth(otitle.getText(), 1.5f));
		otitle.setX(sx).shiftYPos(sy);
		
		sy+=23;
		for(Objective ob : aq.getObjectives()) {
			String sl = ob.isFinished(sp) ? ""+ChatColor.GREEN : ""+ChatColor.YELLOW;
			GenericLabel gl = new GenericLabel(sl+ob.format());
			gl.setAnchor(WidgetAnchor.CENTER_CENTER);
			gl.shiftXPos(sx).shiftYPos(sy);
			gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText()));
			sy+=17;
			attachWidget(Quester.getInstance(), gl);
		}
		sy+=3;
		
		GenericLabel rtitle = new GenericLabel("Rewards: ");
		rtitle.setAnchor(WidgetAnchor.CENTER_CENTER);
		rtitle.setScale(1.5f);
		rtitle.setHeight(23).setWidth(GenericLabel.getStringWidth(rtitle.getText(), 1.5f));
		rtitle.setX(sx).shiftYPos(sy);
		sy+=19;
		
		for(Reward rew : aq.getRewards()) {
			Set<?> gw = rew.createWidgets(sx, sy);
			for(Object gww : gw) {
				attachWidget(Quester.getInstance(), (GenericWidget) gww);
			}
			sy += 17;
		}
		
		GenericTexture gt = new GenericTexture("https://www.dropbox.com/s/8x6ud0tnc2xpydu/conceptquestfixed.png?dl=1");
		gt.setX(tsx-15).setY(tsy-15);
		gt.setWidth(sx - tsx+15).setHeight(sy - tsy+15);
		
		attachWidgets(Quester.getInstance(), title, cstart, otitle, rtitle, gt);
	}
}
