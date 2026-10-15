package com.mikapoty.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CigarCommand implements CommandExecutor {
    private final CigarItem cigarItem;

    public CigarCommand(CigarItem cigarItem) {
        this.cigarItem = cigarItem;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Эта команда доступна только игроку.");
            return true;
        }

        ItemStack cigar = cigarItem.createSigar();

        player.getInventory().addItem(cigar);

        return true;
    }
}
