package fr.dd06.skydefender.event;

import fr.dd06.skydefender.SkyDefenderRun;
import fr.dd06.skydefender.kits.Kit;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;

import java.util.Random;

public class InventoryEvents implements Listener {

    private SkyDefenderRun main;

    public InventoryEvents(SkyDefenderRun main) {
        this.main = main;
    }


    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        Player player = (Player) e.getWhoClicked();
        ItemStack current = e.getCurrentItem();

        if (current == null)
            return;

        if (!main.getGame().isGameStarted()) {
            e.setCancelled(true);
        }

        if (e.getView().getTitle().equals("§8Choisir une équipe")) {

            if (current.getType().equals(Material.RED_WOOL)) {
                player.closeInventory();
                player.updateInventory();
            }
            if (current.getType().equals(Material.BLUE_BANNER )) {

                player.closeInventory();
                player.updateInventory();
                if (main.getGame().defenders.contains(player.getUniqueId())) {
                    player.sendMessage("§cVous êtes déjà dans cette équipe !");
                    return;
                }
                if (main.getGame().defenders.size() >= main.getConfig()
                        .getInt("skydefenderconfig.ingameconfig.teams.defenseurs.maxplayers")) {
                    player.sendMessage("§cL'équipe est complète");
                    return;
                }
                if (main.getGame().attackers.contains(player.getUniqueId())) {
                    main.getGame().attackers.remove(player.getUniqueId());
                }
                if (main.getGame().spectators.contains(player.getUniqueId())) {
                    main.getGame().spectators.remove(player.getUniqueId());
                }
                main.getGame().defenders.add(player.getUniqueId());
                player.sendMessage("§aVous avez rejoint l'équipe des §9Défenseurs !");

            }
            if (current.getType().equals(Material.RED_BANNER)) {

                player.closeInventory();
                player.updateInventory();
                if (main.getGame().attackers.contains(player.getUniqueId())) {
                    player.sendMessage("§cVous êtes déjà dans cette équipe !");
                    return;
                }
                if (main.getGame().attackers.size() >= main.getConfig()
                        .getInt("skydefenderconfig.ingameconfig.teams.attaquants.maxplayers")) {
                    player.sendMessage("§cL'équipe est complète");
                    return;
                }
                if (main.getGame().defenders.contains(player.getUniqueId())) {
                    main.getGame().defenders.remove(player.getUniqueId());
                }
                if (main.getGame().spectators.contains(player.getUniqueId())) {
                    main.getGame().spectators.remove(player.getUniqueId());
                }
                main.getGame().attackers.add(player.getUniqueId());
                player.sendMessage("§aVous avez rejoint l'équipe des §cAttaquants !");

            }
            if (current.getType().equals( Material.WHITE_BANNER)) {

                player.closeInventory();
                player.updateInventory();
                if (main.getGame().spectators.contains(player.getUniqueId())) {
                    main.getGame().spectators.remove(player.getUniqueId());
                }
                if (main.getGame().attackers.contains(player.getUniqueId())) {
                    main.getGame().attackers.remove(player.getUniqueId());
                }
                if (main.getGame().defenders.contains(player.getUniqueId())) {
                    main.getGame().defenders.remove(player.getUniqueId());
                }

                Random randomTeam = new Random();
                int result = randomTeam.nextInt(100);
                if (result >= 50) {
                    if (main.getGame().defenders.size() >= main.getConfig()
                            .getInt("skydefenderconfig.ingameconfig.teams.defenseurs.maxplayers")) {
                        main.getGame().attackers.add(player.getUniqueId());
                    } else {
                        main.getGame().attackers.add(player.getUniqueId());
                    }
                } else {
                    if (main.getGame().attackers.size() >= main.getConfig()
                            .getInt("skydefenderconfig.ingameconfig.teams.attaquants.maxplayers")) {
                        main.getGame().defenders.add(player.getUniqueId());
                    } else {
                        main.getGame().attackers.add(player.getUniqueId());
                    }
                }

                player.sendMessage("§aVotre équipe sera aléatoire !");

            }

            if (current.getType().equals(Material.PURPLE_BANNER )) {

                player.closeInventory();
                player.updateInventory();
                if (main.getGame().spectators.contains(player.getUniqueId())) {
                    player.sendMessage("§cVous êtes déjà dans cette équipe !");
                    return;
                }

                if (main.getGame().defenders.contains(player.getUniqueId())) {
                    main.getGame().defenders.remove(player.getUniqueId());
                }
                if (main.getGame().attackers.contains(player.getUniqueId())) {
                    main.getGame().attackers.remove(player.getUniqueId());
                }
                main.getGame().spectators.add(player.getUniqueId());
                player.sendMessage("§aVous avez rejoint l'équipe des §5Spectateurs !");

            }
        }

        if (e.getView().getTitle().equals("§6Kits")) {


            for (Kit kit : Kit.values()) {

                if (current.getItemMeta() != null && current.getItemMeta().getDisplayName() != null) {
                    if(current.getItemMeta().getDisplayName().equals("§bElémentaliste")) {
                        Inventory invElementaliste = Bukkit.createInventory(null, 9, "§6Elémentaliste");
                        invElementaliste.setItem(3, Kit.getIconFromKit(Kit.ELEMENTALISTE_FEU));
                        invElementaliste.setItem(5, Kit.getIconFromKit(Kit.ELEMENTALISTE_EAU));
                        player.openInventory(invElementaliste);
                        player.updateInventory();

                    }
                    else if (current.getItemMeta().getDisplayName().equals("§b" + kit.getName())) {
                        if (!kit.getPlayersKit().contains(player.getUniqueId())) {
                            Kit.selectKit(player, kit);
                            player.sendMessage("§aVous avez choisi le kit " + "§b" + kit.getName() + " §a!");
                            Kit.giveItemsToPlayer(player);
                            Kit.addEffectsToPlayer(player);
                            player.closeInventory();
                            player.updateInventory();

                            break;

                        }

                    }
                    if (current.getItemMeta().getDisplayName().equals("§bRandom")) {
                        Kit.selectRandomKit(player);

                        player.sendMessage("§aVous avez choisi un kit aléatoire !");
                        Kit.giveItemsToPlayer(player);
                        Kit.addEffectsToPlayer(player);

                        player.closeInventory();
                        player.updateInventory();


                        break;

                    }
                }


            }
            e.setCancelled(true);


        }
        else if (e.getView().getTitle().equals("§6Elémentaliste")) {
            if (current.getItemMeta() != null && current.getItemMeta().getDisplayName() != null) {
                for (Kit kit : Kit.values()) {
                    if (current.getItemMeta().getDisplayName().equals("§b" + kit.getName())) {
                        if (!kit.getPlayersKit().contains(player.getUniqueId())) {
                            Kit.selectKit(player, kit);
                            player.sendMessage("§aVous avez choisi le kit " + "§b" + kit.getName() + " §a!");
                            Kit.giveItemsToPlayer(player);
                            Kit.addEffectsToPlayer(player);
                            player.closeInventory();
                            player.updateInventory();

                            break;

                        }

                    }
                }
            }
        }
    }
}
