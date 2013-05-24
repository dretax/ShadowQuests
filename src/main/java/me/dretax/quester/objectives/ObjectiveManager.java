/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.objectives;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DreTaX
 */
public class ObjectiveManager {
	
	private static final Map<String, Class<? extends Objective>> allObjs = new HashMap<String, Class<? extends Objective>>();
	
	static {
		allObjs.put("gather", GatherObjective.class);
		allObjs.put("kill_one", KillOneObjective.class);
		allObjs.put("kill", KillObjective.class);
		allObjs.put("block_damage", BlockDamageObjective.class);
		allObjs.put("block_place", BlockPlaceObjective.class);
		allObjs.put("block_destroy", BlockDestroyObjective.class);
		allObjs.put("assassinate", AssassinateObjective.class);
		allObjs.put("delivery", DeliveryObjective.class);
		allObjs.put("find", FindObjective.class);
	}
	
	public static Objective createObjective(String type) {
		Class<? extends Objective> cc = allObjs.get(type);
		if(cc == null) {
			return null;
		}
		try {
			Objective ob = cc.newInstance();
			return ob;
		} catch (InstantiationException ex) {
			Logger.getLogger(ObjectiveManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(ObjectiveManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
}
