package com.mikapoty.plugin;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        CigarItem cigarItem = new CigarItem(this);
        ItemStack cigar = cigarItem.createSigar();

        this.getServer().getPluginManager().registerEvents(new CigarListener(cigarItem), this);

        getServer().getScheduler().runTaskTimer(
                this,
                () -> {
                    for (Player player : getServer().getOnlinePlayers()) {
                        ItemStack mainHand =
                                player.getInventory().getItemInMainHand();
                        ItemStack offHand =
                                player.getInventory().getItemInOffHand();

                        cigarItem.tick(mainHand);
                        cigarItem.tick(offHand);
                    }
                },
                1L,
                1L
        );

        getCommand("cigar").setExecutor(new CigarCommand(cigarItem));
    }


}
