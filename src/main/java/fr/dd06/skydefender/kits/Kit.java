package fr.dd06.skydefender.kits;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;

import java.time.Duration;
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
        switch (result) {
            case 1:
                selectKit(player, Kit.ASSASSIN);
                break;
            case 2:
                selectKit(player, Kit.TANK);
                break;
            case 3:
                selectKit(player, Kit.DRUIDE);
                break;
            case 4:
                selectKit(player, Kit.ELEMENTALISTE);
                break;
            case 5:
                selectKit(player, Kit.ARCHER);
                break;
            default:
                selectKit(player, Kit.BERSERKER);
                break;
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
            itM.setLore(Arrays.asList("§eLivre Tranchant 3","§eEffet Force 1"));

        }
        else if(kit == Kit.ASSASSIN) {
            it.setType(Material.IRON_SWORD);
            itM.setLore(Arrays.asList("§e3 Potions de Poison", "§eBâton knockback 3","§eInvisibilité"));


        }
        else if(kit == Kit.DRUIDE) {
            it.setType(Material.GLASS_BOTTLE);
            itM.setLore(Arrays.asList("§e3 Potions de Régénération", "§e3 Potions d'instant heal", "§e10 Coeurs supplémentaires"));

        }
        else  if (kit == Kit.ELEMENTALISTE) {
            it.setType(Material.FEATHER);
            itM.setLore(Arrays.asList("§eLivre fire aspect","§eLivre flame", "§eRésistance au feu", "§eLivre depth strider 3","§eEffet apnée"));

        }
        else if(kit == Kit.ARCHER) {
            it.setType(Material.BOW);
            itM.setLore(Arrays.asList("§eLivre Infinity","§eLivre Power 4","§e16 toiles d'araignées", "§eSpeed 1"));

        }
        else if(kit == Kit.TANK) {
            it.setType(Material.DIAMOND_CHESTPLATE);
            itM.setLore(Arrays.asList("§eLivre Protection 2","§eLivre Protection 3", "§eRésistance 1"));

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
                    player.getInventory().addItem(getEnchantedBook(Enchantment.DAMAGE_ALL, 3));
                    return;
                }
                else if(kit.getId() == "assassin") {
                    ItemStack stack = getPotionItem(PotionType.POISON,false,false, true, 3);
                    stack.setAmount(3);
                    ItemStack knockbackStick = new ItemStack(Material.STICK);
                    knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
                    player.getInventory().addItem(knockbackStick,stack);


                    return;
                }

                else if(kit.getId() == "tank") {
                    player.getInventory().addItem(getEnchantedBook(Enchantment.PROTECTION_ENVIRONMENTAL, 2));
                    player.getInventory().addItem(getEnchantedBook(Enchantment.PROTECTION_ENVIRONMENTAL, 3));

                    return;
                }
                else if(kit.getId() == "archer") {
                    player.getInventory().addItem(getEnchantedBook(Enchantment.ARROW_INFINITE, 1));
                    player.getInventory().addItem(getEnchantedBook(Enchantment.ARROW_DAMAGE, 4));
                    player.getInventory().addItem(new ItemStack(Material.COBWEB, 16));


                    return;
                }
                else if(kit.getId() == "druide") {

                    ItemStack stack = getPotionItem(PotionType.REGEN,false,false, true, 3);
                    ItemStack stack2 = getPotionItem(PotionType.INSTANT_HEAL,false,false, true, 3);
                    player.getInventory().addItem(stack, stack2);
                    return;
                }
                else if(kit.getId() == "elementaliste") {
                    player.getInventory().addItem(getEnchantedBook(Enchantment.ARROW_FIRE, 1));
                    player.getInventory().addItem(getEnchantedBook(Enchantment.FIRE_ASPECT, 1));
                    player.getInventory().addItem(getEnchantedBook(Enchantment.DEPTH_STRIDER, 3));
                    return;
                }

            }
        }
    }

    public static void addEffectsToPlayer(Player player) {
        if(hasKit(player)) {
            Kit kit = getPlayerKit(player);

            if(kit.getId() == "berserker") {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE,0, true, false));
            }
            else if(kit.getId() == "assassin") {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE,1, true, false));

            }
            else if(kit.getId() == "tank") {
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE,0, true, false));

            }
            else if(kit.getId() == "archer") {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE,0, true, false));

            }
            else if(kit.getId() == "druide") {
                AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                attribute.setBaseValue(60.0D);
                player.setHealth(60.0D);

            }
            else if(kit.getId() == "elementaliste") {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE,0, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE,0, true, false));

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

}
