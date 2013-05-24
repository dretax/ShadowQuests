/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.quests;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import me.dretax.quester.objectives.Objective;
import me.dretax.quester.rewards.Reward;
import org.bukkit.entity.Player;

/**
 *
 * @author DreTaX
 */
public class ActiveQuest extends LoadedQuest{
	
	public ActiveQuest(DataInputStream dis) throws Exception{
		super(dis);
	}
	
	public ActiveQuest(LoadedQuest toWrap, Player activer) {
		super(toWrap.getName(), toWrap.getStartMessage(), toWrap.getEndMessage(), toWrap.isRepeatable());
		for(Objective ob : toWrap.getObjectives()) {
			Objective oba = ob.copyForActive(activer);
			addObjective(oba);
			
		}
		for(Reward rw : toWrap.getRewards()) {
			addReward(rw);
		}
	}

    @SuppressWarnings("Deprecated")
	public boolean finish(Player player) {
		for(Objective ob : getObjectives()) {
			if(!ob.isFinished(player)) {
				return false;
			}
		}
		for(Objective ob : getObjectives()) {
			ob.finish(player);
		}
		for(Reward rew : getRewards()) {
			rew.finish(player);
		}
		player.updateInventory();
		return true;
	}
	
}
