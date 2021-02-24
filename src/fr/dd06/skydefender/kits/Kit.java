package fr.dd06.skydefender.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public enum Kit {


    BERSERKER("berserker", "Berserker"),
    ASSASSIN("assassin", "Assassin"),
    TANK("tank","Tank"),
    ARCHER("archer","Archer"),
    DRUIDE("druide", "Druide"),
    ELEMENTALISTE("elementaliste", "Elémentaliste");
    private String name;
    private String id;
    private ArrayList<UUID> playersKit = new ArrayList<>();

    private Kit(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public ArrayList<UUID> getPlayersKit() {
        return playersKit;
    }

    public static void selectKit(Player player, Kit kit) {

        for(Kit kit2 :values()) {
            if(kit2.getPlayersKit().contains(player.getUniqueId())) {
                kit2.getPlayersKit().remove(player.getUniqueId());
            }
        }

        kit.getPlayersKit().add(player.getUniqueId());
    }

    public static Kit getKitFromId(String id) {

        for(Kit kit : values()) {
            if(kit.getId().equals(id)) {
                return kit;
            }
        }
        return null;
    }
    public static void selectRandomKit(Player player) {

        Random random = new Random();
        int result = random.nextInt(6);
        if(result == 0) {
            selectKit(player, Kit.BERSERKER);
            return;
        }
        else if(result == 1) {
            selectKit(player, Kit.ASSASSIN);
            return;

        }
        else if(result == 2) {
            selectKit(player, Kit.TANK);
            return;

        }
        else if(result == 3) {
            selectKit(player, Kit.DRUIDE);
            return;

        }
        else if(result == 4) {
            selectKit(player, Kit.ELEMENTALISTE);
            return;

        }
        else if(result == 5) {
            selectKit(player, Kit.ARCHER);
            return;

        }
        else {
            selectKit(player, Kit.BERSERKER);
            return;

        }
    }

    public static boolean hasKit(Player player) {
        boolean playerKit = false;
        for(Kit kit : values()) {
            if(kit.getPlayersKit().contains(player.getUniqueId())) {
                playerKit = true;
                break;
            }
        }

        return playerKit;
    }
    public static Kit getPlayerKit(Player player) {

        for(Kit kit : values()) {
            if(kit.getPlayersKit().contains(player.getUniqueId())) {
                return kit;
            }
        }
        return null;
    }
    public static ItemStack getIconFromKit(Kit kit) {

        ItemStack it = new ItemStack(Material.STONE, 1);
        ItemMeta itM = it.getItemMeta();
        if(kit == Kit.BERSERKER) {
            it.setType(Material.DIAMOND_AXE);
            itM.setLore(Arrays.asList("§eLivre Tranchant 4","§eEffet Force 1"));

        }
        else if(kit == Kit.ASSASSIN) {
            it.setType(Material.IRON_SWORD);
            itM.setLore(Arrays.asList("§e3 Potions de Poison","§eInvisibilité"));


        }
        else if(kit == Kit.DRUIDE) {
            it.setType(Material.GLASS_BOTTLE);
            itM.setLore(Arrays.asList("§e3 Potions de Régénération","§e5 Coeurs en plus"));

        }
        else if(kit == Kit.ELEMENTALISTE) {
            it.setType(Material.FLINT_AND_STEEL);
            itM.setLore(Arrays.asList("§eLivre fire aspect + flame","§eRésistance au feu"));


        }
        else if(kit == Kit.ARCHER) {
            it.setType(Material.BOW);
            itM.setLore(Arrays.asList("§eLivre Infinity + Power 3", "§eSpeed 1"));

        }
        else if(kit == Kit.TANK) {
            it.setType(Material.DIAMOND_CHESTPLATE);
            itM.setLore(Arrays.asList("§eLivre Protection 2", "§eRésistance 1"));

        }


        if (kit.getName() != null)
            itM.setDisplayName("§b" + kit.getName());
        it.setItemMeta(itM);

        return it;
    }

    public static void giveItemsToPlayer(Player player) {
        for(Kit kit : values()) {
            if(kit.getPlayersKit().contains(player.getUniqueId())) {
                if(kit.getId() == "berserker") {
                    player.getInventory().addItem(getEnchantedBook(Enchantment.DAMAGE_ALL, 4));
                    return;
                }
                else if(kit.getId() == "assassin") {
                    ItemStack stack = getPotionItem(PotionType.POISON,false,false, true, 3);
                    stack.setAmount(3);
                    player.getInventory().addItem(stack);

                    return;
                }
                else if(kit.getId() == "tank") {
                    player.getInventory().addItem(getEnchantedBook(Enchantment.PROTECTION_ENVIRONMENTAL, 2));

                    return;
                }
                else if(kit.getId() == "archer") {
                    player.getInventory().addItem(getEnchantedBook(Enchantment.ARROW_INFINITE, 1));
                    player.getInventory().addItem(getEnchantedBook(Enchantment.ARROW_DAMAGE, 3));


                    return;
                }
                else if(kit.getId() == "druide") {

                    ItemStack stack = getPotionItem(PotionType.REGEN,false,false, true, 3);

                    player.getInventory().addItem(stack);
                    return;
                }
                else if(kit.getId() == "elementaliste") {
                    player.getInventory().addItem(getEnchantedBook(Enchantment.ARROW_FIRE, 1));
                    player.getInventory().addItem(getEnchantedBook(Enchantment.FIRE_ASPECT, 1));
                    return;
                }
            }
        }
    }

    private static ItemStack getEnchantedBook(Enchantment enchantment, int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        book.setItemMeta(meta);
        return book;
    }

    private static ItemStack getPotionItem(PotionType potion, boolean extended, boolean upgraded, boolean splash, int amount) {
        ItemStack stack = new ItemStack(Material.POTION);
        if(splash) {
            stack.setType(Material.SPLASH_POTION);
        }


        PotionMeta meta = (PotionMeta) stack.getItemMeta();
        meta.setBasePotionData(new PotionData(potion,extended, upgraded));


        stack.setItemMeta(meta);
        stack.setAmount(amount);
        return stack;

    }
    private static ItemStack getPotionItem(PotionType potion, boolean extended, boolean upgraded, boolean splash) {
        ItemStack stack = new ItemStack(Material.POTION);
        if(splash) {
            stack.setType(Material.SPLASH_POTION);
        }


        PotionMeta meta = (PotionMeta) stack.getItemMeta();
        meta.setBasePotionData(new PotionData(potion,extended, upgraded));


        stack.setItemMeta(meta);
        return stack;

    }

}
