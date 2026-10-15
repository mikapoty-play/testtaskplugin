package com.mikapoty.plugin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;



public class CigarListener implements Listener {
    private final CigarItem cigarItem;

    public CigarListener(CigarItem cigarItem) {
        this.cigarItem = cigarItem;
    }

    @EventHandler
    public void onCigarUse(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if (mainHand.getType() == Material.AIR || offHand.getType() == Material.AIR) {
            return;
        }

        boolean canLightMain = mainHand.getType() == Material.FLINT_AND_STEEL  || cigarItem.isLit(mainHand);
        boolean canLightOff = offHand.getType() == Material.FLINT_AND_STEEL  || cigarItem.isLit(offHand);
        boolean hasNoLitMain = cigarItem.isCigar(mainHand) && !cigarItem.isLit(mainHand);
        boolean hasNoLitOff = cigarItem.isCigar(offHand) && !cigarItem.isLit(offHand);

        if (cigarItem.isCigar(mainHand) || cigarItem.isCigar(offHand)) {
            if (canLightOff && hasNoLitMain) {
                cigarItem.setLit(mainHand);
            }
            if (canLightMain && hasNoLitOff) {
                cigarItem.setLit(offHand);
            }
        }



    }
}
