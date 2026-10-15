package com.mikapoty.plugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class CigarItem {
    private final NamespacedKey key;
    private final NamespacedKey is_lit_key;
    private final NamespacedKey wearKey;
    private final NamespacedKey durabilityKey;
    private final NamespacedKey unlitModel;
    private final NamespacedKey litModel;

    public CigarItem(Plugin plugin) {
        this.key = new NamespacedKey(plugin, "sigar");
        this.is_lit_key = new NamespacedKey(plugin, "is_lit");
        this.wearKey = new NamespacedKey(plugin, "wear");
        this.durabilityKey = new NamespacedKey(plugin, "durability");
        this.unlitModel = new NamespacedKey("testtaskpack", "cigar_unlit");
        this.litModel = new NamespacedKey("testtaskpack", "cigar_lit");
    }

    public ItemStack createSigar() {
        ItemStack cigar = new ItemStack(Material.PAPER);

        cigar.editMeta(meta -> {
           meta.displayName(Component.text("Сигара"));
           meta.lore(List.of(Component.text("Держите ее в одной руке, а поджигайте второй"),
                   Component.text("при помощи огнива или другой подожженной сигары.")));

           meta.setMaxStackSize(1);

           meta.getPersistentDataContainer().set(
             key,
             PersistentDataType.BOOLEAN,
                   true
           );

           meta.getPersistentDataContainer().set(
                   is_lit_key,
                   PersistentDataType.BOOLEAN,
                   false
           );

            meta.getPersistentDataContainer().set(
                    durabilityKey,
                    PersistentDataType.INTEGER,
                    180
            );

            meta.getPersistentDataContainer().set(
                    wearKey,
                    PersistentDataType.DOUBLE,
                    0.0
            );

            meta.setItemModel(unlitModel);

        });

        return cigar;
    }

    public boolean isLit(ItemStack item) {
        return item.getItemMeta()
                .getPersistentDataContainer()
                .getOrDefault(
                        is_lit_key,
                        PersistentDataType.BOOLEAN,
                        false
                );
    }

    public boolean setLit(ItemStack item) {
        return item.editMeta(meta -> {
            meta.getPersistentDataContainer().set(
                    is_lit_key,
                    PersistentDataType.BOOLEAN,
                    true
            );

            meta.setItemModel(litModel);
        });
    }

    public boolean isCigar(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta()
                .getPersistentDataContainer()
                .getOrDefault(
                        key,
                        PersistentDataType.BOOLEAN,
                        false
                );
    }

    public boolean tick(ItemStack item) {
        if (!isCigar(item) || !isLit(item)) {
            return false;
        }

        item.editMeta(meta -> {
            PersistentDataContainer pdc =
                    meta.getPersistentDataContainer();

            double wear = pdc.getOrDefault(
                    wearKey,
                    PersistentDataType.DOUBLE,
                    0.0
            );

            wear += 0.05;

            int durability = pdc.getOrDefault(
                    durabilityKey,
                    PersistentDataType.INTEGER,
                    100
            );

            while (wear >= 1.0) {
                wear -= 1.0;
                durability--;
            }

            pdc.set(wearKey, PersistentDataType.DOUBLE, wear);
            pdc.set(
                    durabilityKey,
                    PersistentDataType.INTEGER,
                    Math.max(0, durability)
            );
        });

        int durability = item.getItemMeta().getPersistentDataContainer().getOrDefault(durabilityKey, PersistentDataType.INTEGER, 180);

        if (durability <= 0) {
            item.setAmount(0);
            return true;
        }

        return false;
    }
}
