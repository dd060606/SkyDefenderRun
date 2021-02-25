package fr.dd06.skydefender.commands;

import fr.dd06.skydefender.SkyDefenderRun;
import fr.dd06.skydefender.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandKits implements CommandExecutor {

    private SkyDefenderRun main;

    public CommandKits(SkyDefenderRun skyDefenderRun) {
        this.main = skyDefenderRun;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (s.equalsIgnoreCase("kits")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;


                if (main.getConfig().getConfigurationSection("skydefenderconfig.ingameconfig.kits")
                        .getBoolean("enabled")) {

                    if (SkyDefenderRun.getGamestarted()) {

                        if (Kit.hasKit(player)) {
                            player.sendMessage("§cVous avez déjà un kit !");
                            return true;

                        }

                        if (main.spectateurs.contains(player.getUniqueId())) {
                            player.sendMessage("§cVous ne pouvez pas recevoir de kit en tant que spectateur !");
                            return true;

                        }


                        Inventory invKits = Bukkit.createInventory(null, 27, "§6Kits");

                        for (int i = 0; i < 27; i++) {


                            if (i == 3) {
                                invKits.setItem(i, Kit.getIconFromKit(Kit.BERSERKER));
                            } else if (i == 5) {
                                invKits.setItem(i, Kit.getIconFromKit(Kit.ASSASSIN));

                            } else if (i == 10) {
                                invKits.setItem(i, Kit.getIconFromKit(Kit.TANK));

                            } else if (i == 13) {
                                invKits.setItem(i, getItem(Material.WOOL, "§bRandom", (byte) 0));

                            } else if (i == 16) {
                                invKits.setItem(i, Kit.getIconFromKit(Kit.ARCHER));

                            } else if (i == 21) {
                                invKits.setItem(i, Kit.getIconFromKit(Kit.DRUIDE));

                            } else if (i == 23) {
                                invKits.setItem(i, Kit.getIconFromKit(Kit.ELEMENTALISTE));

                            } else {
                                invKits.setItem(i, getItem(Material.STAINED_GLASS_PANE, "", (byte) 14));
                            }
                        }

                        player.openInventory(invKits);

                    } else {
                        player.sendMessage("§cLe jeu n'est pas commencé !");
                    }

                } else {
                    player.sendMessage("§cLes kits ne sont pas activés !");
                }
            } else {
                commandSender.sendMessage("§cCette commande doit être faite par un joueur !");
            }
            return true;
        }
        return false;
    }


    public ItemStack getItem(Material material, String customName, byte num) {
        ItemStack it = new ItemStack(material, 1, num);
        ItemMeta itM = it.getItemMeta();
        if (customName != null)
            itM.setDisplayName(customName);
        it.setItemMeta(itM);

        it.setItemMeta(itM);
        return it;

    }
}
