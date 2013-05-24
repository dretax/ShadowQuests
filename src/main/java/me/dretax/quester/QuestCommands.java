/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester;

import java.util.List;
import me.dretax.quester.listeners.NPCManager;
import me.dretax.quester.objectives.FindObjective;
import me.dretax.quester.objectives.Objective;
import me.dretax.quester.quests.ActiveQuest;
import me.dretax.quester.quests.LoadedQuest;
import me.dretax.quester.quests.QuestPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author DreTaX
 */
public class QuestCommands implements CommandExecutor {

	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		Player plr = (Player) cs;
		if (strings.length == 0) {
			plr.sendMessage(ChatColor.RED + "Not enough arguments!");
			return true;
		}
		String farg = strings[0];
		if (farg.equals("find")) {
			QuestPlayer qplr = QuestPlayer.getQuestPlayer(plr);
			ActiveQuest aq = qplr.getActiveQuest();
			if(aq == null) {
				cs.sendMessage(ChatColor.RED+"You don't have an active quest!");
				return true;
			}
			for(Objective ob : aq.getObjectives()) {
				if(ob instanceof FindObjective) {
					FindObjective fob = (FindObjective) ob;
					if(fob.isFinished(plr)) {
						continue;
					}
					if(fob.isInRange(plr)) {
						fob.setFound(true);
						cs.sendMessage(ChatColor.GREEN+"Found location!");
					}
				}
			}
			
		}
		if (farg.equals("info")) {
			if (strings.length < 2) {
				QuestPlayer qplr = QuestPlayer.getQuestPlayer(plr);
				ActiveQuest aq = qplr.getActiveQuest();
				if (aq == null) {
					plr.sendMessage(ChatColor.RED + "You do not have an active quest!");
					return true;
				}
				aq.printInfo(plr);
				return true;
			}
			String questName = "";
			for (int i = 1; i < strings.length; i++) {
				questName += strings[i] + " ";
			}
			questName = questName.trim().toLowerCase();
			LoadedQuest lq = Quester.getQuestManager().getLoadedQuest(questName);
			if (lq == null) {
				plr.sendMessage(ChatColor.RED + questName + " is not a valid quest!");
				return true;
			}
			lq.printInfo(plr);
			return true;
		}
		if (farg.equals("accept")) {
			if (strings.length < 2) {
				plr.sendMessage(ChatColor.RED + "You need to specify the quest's number!");
				return true;
			}
			String sqn = strings[1];
			Integer parsed = -1;
			try {
				parsed = Integer.parseInt(sqn);
			} catch (Exception ex) {
				plr.sendMessage(ChatColor.RED + sqn + " is not a valid number!");
				return true;
			}
			QuestPlayer qplr = QuestPlayer.getQuestPlayer(plr);
			if (qplr.getTargetNPC() == null) {
				plr.sendMessage(ChatColor.RED + "Right click a npc first!");
				return true;
			}
			List<String> allQ = NPCManager.getQuests(qplr.getTargetNPC());
			if (allQ == null) {
				plr.sendMessage(ChatColor.RED + "That npc doesn't have any quests!");
				return true;
			}
			try {
				String lqn = allQ.get(parsed - 1);
				LoadedQuest lq = Quester.getQuestManager().getLoadedQuest(lqn);
				if(qplr.hasFinished(lq.getName()) && !lq.isRepeatable()) {
					plr.sendMessage(ChatColor.RED+"You have already completed this quest!");
					return true;
				}
				qplr.setActiveQuest(lq, plr);
				qplr.setTargetNPC(null);
				plr.sendMessage(ChatColor.GREEN + "Accepted quest! ");
				lq.printInfo(plr);
			} catch (Exception ex) {
				plr.sendMessage(ChatColor.RED + "Invalid quest number!");
				return true;
			}

			return true;
		}

		if (farg.equals("finish")) {
			QuestPlayer qplr = QuestPlayer.getQuestPlayer(plr);
			ActiveQuest aq = qplr.getActiveQuest();
			if (aq == null) {
				plr.sendMessage(ChatColor.RED + "You do not have an active quest!");
				return true;
			}
			if (aq.finish(plr)) {
				plr.sendMessage(ChatColor.GREEN + aq.getEndMessage());
				qplr.finishActiveQuest();
			} else {
				plr.sendMessage(ChatColor.RED + "Could not finish quest, unfinished objectives:");
				for(Objective ob : aq.getObjectives()) {
					if(!ob.isFinished(plr)) {
						plr.sendMessage(ChatColor.GOLD+ob.format());
					}
				}
			}
			return true;
		}

		if (farg.equals("addquest")) {
			if (strings.length < 2) {
				plr.sendMessage(ChatColor.RED + "You need to specify the quest's name!");
				return true;
			}
			if (!plr.hasPermission("quester.addquests")) {
				plr.sendMessage(ChatColor.RED + "You do not have permission to do that!");
				return true;
			}
			String questName = "";
			for (int i = 1; i < strings.length; i++) {
				questName += strings[i] + " ";
			}
			questName = questName.trim();
			LoadedQuest lq = Quester.getQuestManager().getLoadedQuest(questName);
			if (lq == null) {
				plr.sendMessage(ChatColor.RED + questName + " is not a quest!");
				return true;
			}
			QuestPlayer qplr = QuestPlayer.getQuestPlayer(plr);
			if (qplr.getTargetNPC() == null) {
				plr.sendMessage(ChatColor.RED + "You have to right click a npc first!");
				return true;
			}
			NPCManager.addQuest(qplr.getTargetNPC(), lq);
			plr.sendMessage(ChatColor.GREEN + "Added quest to NPC!");

		}
		return true;
	}
}
