/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.quests;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import me.dretax.quester.objectives.Objective;
import me.dretax.quester.objectives.ObjectiveManager;
import me.dretax.quester.rewards.Reward;
import me.dretax.quester.rewards.RewardManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author DreTaX
 */
public class LoadedQuest {
	private String questName;
	private String sMessage, eMessage;
	private boolean repeatable;
	private Set<Reward> rews = new HashSet<Reward>();
	private Set<Objective> obs = new HashSet<Objective>();

	public LoadedQuest(String questName, String sMessage, String eMessage, boolean repeatable) {
		System.out.println("Loaded quest: "+questName);
		this.questName = questName;
		this.sMessage = sMessage;
		this.eMessage = eMessage;
		this.repeatable = repeatable;
	}
	
	public LoadedQuest(DataInputStream dis) throws Exception{
		questName = dis.readUTF();
		sMessage = dis.readUTF();
		eMessage = dis.readUTF();
		repeatable = dis.readBoolean();
		int nRews = dis.readInt();
		for(int i=1;i<=nRews;i++) {
			Map<String, Object> info = new HashMap<String, Object>();
			ObjectInputStream ois = new ObjectInputStream(dis);
			int nData = dis.readInt();
			for(int j=1;j<=nData;j++) {
				String nname = ois.readUTF();
				Object ob = ois.readObject();
				info.put(nname, ob);
			}
			Reward rew = RewardManager.createReward((String) info.get("type"));
			rew.load(info);
			rews.add(rew);
		}
		int nObs = dis.readInt();
		for(int i=1;i<=nObs;i++) {
			Map<String, Object> info = new HashMap<String, Object>();
			ObjectInputStream ois = new ObjectInputStream(dis);
			int nData = dis.readInt();
			for(int j=1;j<=nData;j++) {
				String nname = ois.readUTF();
				Object ob = ois.readObject();
				info.put(nname, ob);
			}
			Objective obj = ObjectiveManager.createObjective((String) info.get("type"));
			obj.load(info);
			obs.add(obj);
		}
	}

	void save(DataOutputStream dos) throws Exception{
		dos.writeUTF(questName);
		dos.writeUTF(sMessage);
		dos.writeUTF(eMessage);
		dos.writeBoolean(repeatable);
		dos.writeInt(rews.size());
		for(Reward rew : rews) {
			Map<String, Object> toSave = rew.getSaveMap();
			ObjectOutputStream oos = new ObjectOutputStream(dos);
			dos.writeInt(toSave.size());
			for(Entry<String, Object> ee : toSave.entrySet()) {
				dos.writeUTF(ee.getKey());
				oos.writeObject(ee.getValue());
			}
		}
		dos.writeInt(obs.size());
		for(Objective ob : obs) {
			Map<String, Object> toSave = ob.getSaveMap();
			ObjectOutputStream oos = new ObjectOutputStream(dos);
			dos.writeInt(toSave.size());
			for(Entry<String, Object> ee : toSave.entrySet()) {
				dos.writeUTF(ee.getKey());
				oos.writeObject(ee.getValue());
			}
		}
	}

	public void addObjective(Objective ob) {
		System.out.println("Added objective: "+ob.getClass().getName()+" "+ob.toString());
		obs.add(ob);
	}

	public void addReward(Reward rw) {
		System.out.println("Added reward: "+rw.getClass().getName()+" "+rw.toString());
		rews.add(rw);
	}
	
	public String getName() {
		return questName;
	}
	
	public Set<Reward> getRewards() {
		return rews;
	}
	
	public Set<Objective> getObjectives() {
		return obs;
	}
	
	public String getStartMessage() {
		return sMessage;
	}
	
	public String getEndMessage() {
		return eMessage;
	}

	public void printInfo(Player plr) {
			plr.sendMessage(ChatColor.YELLOW + getName());
			plr.sendMessage(ChatColor.GOLD + "------------------");
			plr.sendMessage(ChatColor.BLUE + getStartMessage());
			plr.sendMessage(ChatColor.GOLD + "Objectives:");
			for (Objective obj : getObjectives()) {
				plr.sendMessage(ChatColor.AQUA + obj.format());
			}
			plr.sendMessage(ChatColor.GOLD + "Rewards:");
			for (Reward rew : getRewards()) {
				plr.sendMessage(ChatColor.AQUA + rew.format());
			}
	}

	public boolean isRepeatable() {
		return repeatable;
	}
	
}
