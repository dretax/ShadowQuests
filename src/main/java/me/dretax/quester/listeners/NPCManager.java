/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.listeners;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import me.dretax.quester.quests.LoadedQuest;

/**
 *
 * @author DreTaX
 */
public class NPCManager {
	private static Map<Integer, List<String>> npcBinds = new HashMap<Integer, List<String>>();
	private static final int SAVE_VERSION = 1;
	
	public static boolean hasQuests(Integer npc) {
		return npcBinds.containsKey(npc);
	}
	
	public static List<String> getQuests(Integer npc) {
		return npcBinds.get(npc);
	}
	
	public static void addQuest(Integer npc, LoadedQuest lq) {
		List<String> ls = npcBinds.get(npc);
		if(ls == null) {
			ls = new ArrayList<String>();
			npcBinds.put(npc, ls);
		}
		ls.add(lq.getName());
	}
	
	public static void save(File dataFolder) throws Exception {
		File bindFile = new File(dataFolder, "npcBinds.dat");
		if(bindFile.exists()) {
			bindFile.delete();
		}
		bindFile.createNewFile();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(bindFile));
		dos.writeInt(SAVE_VERSION);
		dos.writeInt(npcBinds.size());
		for(Entry<Integer, List<String>> ent : npcBinds.entrySet()) {
			dos.writeInt(ent.getKey());
			dos.writeInt(ent.getValue().size());
			for(String st : ent.getValue()) {
				dos.writeUTF(st);
			}
		}
		dos.flush();
		dos.close();
	}
	
	public static void load(File dataFolder) throws Exception {
		File bindFile = new File(dataFolder, "npcBinds.dat");
		if(!bindFile.exists()) {
			return;
		}
		DataInputStream dis = new DataInputStream(new FileInputStream(bindFile));
		int sVersion = dis.readInt();
		int nbinds = dis.readInt();
		for(int i=1;i<=nbinds;i++) {
			int npcID = dis.readInt();
			int nQuests = dis.readInt();
			List<String> quests = new ArrayList<String>();
			for(int j=1;j<=nQuests;j++) {
				quests.add(dis.readUTF());
			}
			npcBinds.put(npcID, quests);
		}
		dis.close();
	}
	
}
