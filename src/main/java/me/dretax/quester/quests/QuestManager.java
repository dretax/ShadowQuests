/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.quests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.dretax.quester.Quester;
import me.dretax.quester.objectives.Objective;
import me.dretax.quester.objectives.ObjectiveManager;
import me.dretax.quester.rewards.Reward;
import me.dretax.quester.rewards.RewardManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author DreTaX
 */
public class QuestManager {

	private Quester instance;
	private Map<String, LoadedQuest> loadedQuests = new HashMap<String, LoadedQuest>();

	public QuestManager(Quester instance) {
		this.instance = instance;
		reloadQuests();
	}
	
	public LoadedQuest getLoadedQuest(String name) {
		return loadedQuests.get(name.toLowerCase());
	}

	private void reloadQuests() {
		File dataFolder = instance.getDataFolder();
		File questFolder = new File(dataFolder, "quests");
		if (!questFolder.exists()) {
			questFolder.mkdirs();
			generateDefaults(questFolder);
		}

		for (File f : questFolder.listFiles()) {
			if(!(f.getName().endsWith(".yml"))) {
				continue;
			}
			YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
			Set<String> questNames = config.getKeys(false);
			for (String questName : questNames) {
				try {
				ConfigurationSection questInfo = config.getConfigurationSection(questName);
				LoadedQuest lq = new LoadedQuest(questName, questInfo.getString("StartMessage", "I got nothin'"), questInfo.getString("EndMessage","I got nothin'"), questInfo.getBoolean("Repeatable", false));
				List<Map<String, Object>> allObjs = (List<Map<String, Object>>) questInfo.getList("Objectives");
				for (Map<String, Object> oneObj : allObjs) {
					String type = (String) oneObj.get("type");
					Objective ob = ObjectiveManager.createObjective(type);
					if (ob == null) {
						System.out.println("Could not create objective of type " + type);
						continue;
					}
					ob.load(oneObj);
					lq.addObjective(ob);
				}
				List<Map<String, Object>> allRewards = (List<Map<String, Object>>) questInfo.getList("Rewards");
				for (Map<String, Object> oneRew : allRewards) {
					String type = (String) oneRew.get("type");
					Reward rw = RewardManager.createReward(type);
					if (rw == null) {
						System.out.println("Could not create reward of type " + type);
						continue;
					}
					rw.load(oneRew);
					lq.addReward(rw);
				}
				loadedQuests.put(questName.toLowerCase(), lq);
				} catch(Exception ex) {
					System.out.println("Failed at loading: "+questName);
					ex.printStackTrace();
				}
			}
		}
	}

	private void generateDefaults(File questFolder) {

		File f = new File(questFolder, "default.yml");
		try {
			f.createNewFile();
		} catch (IOException ex) {
			Logger.getLogger(QuestManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		config.createSection("Get me wooden stuff");
		ConfigurationSection cs = config.getConfigurationSection("Get me wooden stuff");
		List<Map<String, Object>> toSet = new ArrayList<Map<String, Object>>();
		Map<String, Object> someObjs = new HashMap<String, Object>();
		someObjs.put("type", "gather");
		someObjs.put("item-id", 1);
		someObjs.put("item-amount", 64);
		toSet.add(someObjs);
		someObjs = new HashMap<String, Object>();

		someObjs.put("type", "gather");
		someObjs.put("item-id", 2);
		someObjs.put("item-amount", 64);
		toSet.add(someObjs);
		cs.set("Objectives", toSet);

		toSet = new ArrayList<Map<String, Object>>();
		Map<String, Object> someRews = new HashMap<String, Object>();
		someRews.put("type", "money");
		someRews.put("amount", 10);
		toSet.add(someRews);
		cs.set("Rewards", toSet);
		
		cs.set("StartMessage", "Get me WOOOD!");
		cs.set("EndMessage", "WOOOOOOOOOOODDD!");
		cs.set("Repeatable", false);
		try {
			config.save(f);
		} catch (IOException ex) {
			Logger.getLogger(QuestManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
