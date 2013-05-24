/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.dretax.quester.listeners.NPCManager;
import me.dretax.quester.listeners.QuestListener;
import me.dretax.quester.quests.QuestManager;
import me.dretax.quester.quests.QuestPlayer;
import me.dretax.quester.spout.QuestInfo;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
import org.getspout.spoutapi.keyboard.Keyboard;

/**
 * @author DreTaX
 */
public class Quester extends JavaPlugin implements BindingExecutionDelegate {

	private static Economy economy;
	private static QuestManager questManager;
	private static Quester instance;

	@Override
	public void onDisable() {
		try {
			NPCManager.save(getDataFolder());
			QuestPlayer.save(getDataFolder());
		} catch (Exception ex) {
			Logger.getLogger(Quester.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void onEnable() {
		try {
			NPCManager.load(getDataFolder());
			QuestPlayer.load(getDataFolder());
		} catch (Exception ex) {
			Logger.getLogger(Quester.class.getName()).log(Level.SEVERE, null, ex);
		}
		instance = this;
		setupEconomy();
		getCommand("qu").setExecutor(new QuestCommands());
		questManager = new QuestManager(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new NPCListener(this), this);
		pm.registerEvents(new QuestListener(this), this);

		getConfig().addDefault("gui-open", Keyboard.KEY_R.name());
		getConfig().options().copyDefaults(true);
		saveConfig();
		SpoutManager.getKeyBindingManager().registerBinding("Quest Info", Keyboard.valueOf(getConfig().getString("gui-open")), "Opens the quest info", this, this);

	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}

	public static Economy getEconomy() {
		return economy;
	}

	public static QuestManager getQuestManager() {
		return questManager;
	}

	public static Quester getInstance() {
		return instance;
	}

	public void keyPressed(KeyBindingEvent kbe) {
		if (kbe.getScreenType() != ScreenType.GAME_SCREEN) {
			return;
		}
		QuestInfo qi = new QuestInfo(kbe.getPlayer(), QuestPlayer.getQuestPlayer(kbe.getPlayer()));
		kbe.getPlayer().getMainScreen().attachPopupScreen(qi);
	}

	public void keyReleased(KeyBindingEvent kbe) {
	}
}
