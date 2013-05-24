/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.quests;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import me.dretax.quester.Quester;
import me.dretax.quester.objectives.Objective;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 *
 * @author DreTaX
 */
public class QuestPlayer {
	private static Map<String, QuestPlayer> cc = new HashMap<String, QuestPlayer>();
	private static final int SAVE_VERSION = 1;
	private String name;
	private Integer targetNPC; //do not save, useless
	private Set<String> finishedQuests = new HashSet<String>();
	private ActiveQuest activeQuest;
	
	public static void save(File dataFolder) throws Exception {
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(dataFolder, "players.dat")));
		dos.writeInt(SAVE_VERSION);
		dos.writeInt(cc.size());
		for(Entry<String, QuestPlayer> ee : cc.entrySet()) {
			dos.writeUTF(ee.getKey());
			ee.getValue().saveData(dos);
		}
		dos.flush();
		dos.close();
	}
	
	public void saveData(DataOutputStream dos) throws Exception{
		dos.writeInt(finishedQuests.size());
		for(String fQ : finishedQuests) {
			dos.writeUTF(fQ);
		}
		dos.writeBoolean(activeQuest != null);
		if(activeQuest == null) {
			return;
		}
		activeQuest.save(dos);
		dos.flush();
		dos.close();
	}
	
	public static void load(File dataFolder) throws Exception {
		File pData = new File(dataFolder, "players.dat");
		if(!pData.exists()) {
			return;
		}
		DataInputStream dis = new DataInputStream(new FileInputStream(pData));
		int ver = dis.readInt();
		int nPlayers = dis.readInt();
		for(int i=1;i<=nPlayers;i++) {
			QuestPlayer qplr = new QuestPlayer(dis.readUTF());
			qplr.loadData(dis);
		}
		dis.close();
	}

	private void loadData(DataInputStream dis) throws Exception{
		int fQuests = dis.readInt();
		for(int j=1;j<=fQuests;j++) {
			finishedQuests.add(dis.readUTF());
		}
		boolean haq = dis.readBoolean();
		if(!haq) {
			return;
		}
		activeQuest = new ActiveQuest(dis);
	}
	
	public QuestPlayer(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getTargetNPC() {
		return targetNPC;
	}
	
	public void setTargetNPC(Entity newNPC) {
		if(newNPC == null) {
			targetNPC = null;
			return;
		}
		targetNPC = newNPC.getEntityId();
	}
	
	public ActiveQuest getActiveQuest() {
		return activeQuest;
	}
	
	public void finishActiveQuest() {
		if(activeQuest != null) {
			finishedQuests.add(activeQuest.getName());
		}
		activeQuest = null;
	}
	
	public boolean hasFinished(String quest) {
		return finishedQuests.contains(quest);
	}
	
	public void setActiveQuest(LoadedQuest newQ, Player plr) {
		if(newQ == null) {
			activeQuest = null;
		}
		activeQuest = new ActiveQuest(newQ, plr);
	}
	
	public static QuestPlayer getQuestPlayer(Player normalPlayer) {
		QuestPlayer qp = cc.get(normalPlayer.getName());
		if(qp == null) {
			qp = new QuestPlayer(normalPlayer.getName());
			cc.put(normalPlayer.getName(), qp);
		}
		return qp;
	}
	
	public static void broadcastEvent(Event event) {
		for(QuestPlayer aq : cc.values()) {
			if(aq.activeQuest == null) {
				continue;
			}
			for(Objective ob : aq.activeQuest.getObjectives()) {
				ob.onEvent(aq, event);
			}
		}
	}
}
