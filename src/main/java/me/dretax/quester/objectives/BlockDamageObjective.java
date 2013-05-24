/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dretax.quester.objectives;

import java.io.DataOutputStream;
import java.util.Map;
import me.dretax.quester.quests.QuestPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockDamageEvent;

/**
 *
 * @author DreTaX
 */
public class BlockDamageObjective extends Objective{
	private Integer blockId, blockData, amount;

	private BlockDamageObjective(Integer blockId, Integer blockData, Integer amount) {
		this.blockId = blockId;
		this.blockData = blockData;
		this.amount = amount;
	}
	
	public BlockDamageObjective() {
		
	}

	@Override
	public void onEvent(QuestPlayer quester, Event event) {
		if(event instanceof BlockDamageEvent) {
			BlockDamageEvent bde = (BlockDamageEvent) event;
			int id = bde.getBlock().getTypeId();
			int data = bde.getBlock().getData();
			if(blockId == id && blockData == data) {
				if(amount > 0) {
					amount--;
				}
			}
		}
	}

	@Override
	public void load(Map<String, Object> data) {
		blockId = (Integer) data.get("block-id");
		blockData = (Integer) data.get("block-data");
		amount = (Integer) data.get("amount");
	}

	@Override
	public Objective copyForActive(Player plr) {
		return new BlockDamageObjective(blockId, blockData, amount);
	}

	@Override
	public boolean isFinished(Player plr) {
		return amount == 0;
	}

	@Override
	public String format() {
		return "Damage "+blockId+":"+blockData+" "+amount+" times";
	}
	
}
