/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.rewards;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.dretax.quester.objectives.ObjectiveManager;

/**
 *
 * @author DreTaX
 */
public class RewardManager {
	
	private static final Map<String, Class<? extends Reward>> allRews = new HashMap<String, Class<? extends Reward>>();
	
	static {
		allRews.put("money", MoneyReward.class);
		allRews.put("item", ItemReward.class);
		allRews.put("command", CommandReward.class);
	}

	public static Reward createReward(String type) {
		Class<? extends Reward> cc = allRews.get(type);
		if(cc == null) {
			return null;
		}
		try {
			Reward rw = cc.newInstance();
			return rw;
		} catch (InstantiationException ex) {
			Logger.getLogger(ObjectiveManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(ObjectiveManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
}
