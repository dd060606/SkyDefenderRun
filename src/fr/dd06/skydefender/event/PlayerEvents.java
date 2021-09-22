package fr.dd06.skydefender.event;

import fr.dd06.skydefender.SkyDefenderRun;
import fr.dd06.skydefender.game.*;
import fr.dd06.skydefender.kits.Kit;
import fr.dd06.skydefender.scoreboards.CustomScoreBoard;
import fr.dd06.skydefender.utils.BlockLocationChecker;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerEvents implements Listener {

    private SkyDefenderRun main;

    public PlayerEvents(SkyDefenderRun main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (main.getGame().isGameStarted()) {
            if (main.getGame().players.contains(player.getUniqueId())) {

                event.setJoinMessage("§e[SkyDefenderRun] : §a" + player.getName() + " a rejoint le jeu !");

                if (main.getGame().defenders.contains(player.getUniqueId())) {
                    player.setPlayerListName(ChatColor.AQUA + "[Défenseur] " + player.getName());

                } else if (main.getGame().attackers.contains(player.getUniqueId())) {
                    player.setPlayerListName(ChatColor.RED + "[Attaquant] " + player.getName());

                }
                CustomScoreBoard board = new CustomScoreBoard(player);
                board.updateTitle("§bSkyDefender");


                main.getGame().boards.put(player.getUniqueId(), board);
                main.getGame().updateScoreboards(player.getUniqueId());
            } else {

                event.setJoinMessage(
                        "§e[SkyDefenderRun] : §a" + player.getName() + " a rejoint le jeu en tant que spectateur !");
                if (!main.getGame().spectators.contains(player.getUniqueId())) {
                    main.getGame().spectators.add(player.getUniqueId());
                }
                CustomScoreBoard specboard = new CustomScoreBoard(player);
                specboard.updateTitle("§bSkyDefender");
                main.getGame().specboards.put(player.getUniqueId(), specboard);
                main.getGame().updateSpectatorsBoards(player.getUniqueId());

                player.setPlayerListName(ChatColor.LIGHT_PURPLE + "[Spectateur] " + player.getName());

                player.setGameMode(GameMode.SPECTATOR);
                World w = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.spawn.world"));
                double x = main.getConfig().getDouble("skydefendersave.spawn.x");
                double y = main.getConfig().getDouble("skydefendersave.spawn.y");
                double z = main.getConfig().getDouble("skydefendersave.spawn.z");
                player.teleport(new Location(w, x, y, z));
            }

        } else {

            if (event.getPlayer() instanceof Player) {
                Player p = event.getPlayer();
                if (Bukkit.getOnlinePlayers().size() > main.getConfig()
                        .getInt("skydefenderconfig.starting.maxplayers")) {
                    p.kickPlayer("Server is full !");
                    return;
                }
            }

            if (!main.getGame().isPaused()) {

                main.getGame().kills.put(player.getUniqueId(), 0);

                player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().clear();
                player.setFoodLevel(20);
                player.setHealth(20);

                if (main.getConfig().getConfigurationSection("skydefenderconfig.teamchooser")
                        .getBoolean("enabled")) {
                    ItemStack teamchooser = new ItemStack(Material.WHITE_BANNER, 1);
                    ItemMeta teamchoosermeta = teamchooser.getItemMeta();
                    teamchoosermeta.addEnchant(Enchantment.DAMAGE_ALL, 0, true);
                    teamchoosermeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    teamchoosermeta.setDisplayName("§eChoisir une équipe");

                    teamchooser.setItemMeta(teamchoosermeta);

                    player.getInventory().setItem(4, teamchooser);
                    player.updateInventory();

                    player.sendMessage("§aVeuillez choisir une équipe !");
                }
                event.setJoinMessage("§e[SkyDefenderRun] : §a" + player.getName() + " a rejoint la partie ! ("
                        + Bukkit.getOnlinePlayers().size() + "/"
                        + main.getConfig().getInt("skydefenderconfig.starting.maxplayers") + ")");

                if (main.getConfig().getConfigurationSection("skydefenderconfig.autostart")
                        .getBoolean("enabled") == true) {
                    if (Bukkit.getOnlinePlayers().size() >= main.getConfig()
                            .getConfigurationSection("skydefenderconfig.starting").getInt("minplayers")) {
                        if (main.getGame().defenders.size() >= 1 && main.getGame().attackers.size() >= 1) {
                            AutoStart autostart = new AutoStart(main);
                            autostart.runTaskTimer(main, 0, 20);
                        }

                    }
                }

                double x, y, z;
                String world = main.getConfig().getString("skydefendersave.spawn.world");
                x = main.getConfig().getDouble("skydefendersave.spawn.x");
                y = main.getConfig().getDouble("skydefendersave.spawn.y");
                z = main.getConfig().getDouble("skydefendersave.spawn.z");
                Location spawn = new Location(Bukkit.getWorld(world), x, y, z);
                player.teleport(spawn);

            }

        }
    }


    @EventHandler
    public void OnQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        CustomScoreBoard board = main.getGame().boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }

        if (!main.getGame().isGameStarted()) {
            if (main.getGame().players.contains(player.getUniqueId()))
                main.getGame().players.remove(player.getUniqueId());
            if (main.getGame().attackers.contains(player.getUniqueId())) {
                main.getGame().attackers.remove(player.getUniqueId());
            }
            if (main.getGame().defenders.contains(player.getUniqueId())) {
                main.getGame().defenders.remove(player.getUniqueId());
            }
            if (main.getGame().spectators.contains(player.getUniqueId())) {
                main.getGame().spectators.remove(player.getUniqueId());
            }
            event.setQuitMessage("§e[SkyDefenderRun] : §c" + player.getName() + " a quitté la partie ! ("
                    + (Bukkit.getOnlinePlayers().size() - 1) + "/"
                    + main.getConfig().getInt("skydefenderconfig.starting.maxplayers") + ")");

        } else {
            if (main.getGame().players.contains(player.getUniqueId())) {

                event.setQuitMessage("§e[SkyDefenderRun] : §c" + player.getName() + " a quitté le SkyDefender !");

            }
        }

    }

    @EventHandler
    public void onWalk(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location ploc = e.getPlayer().getLocation();

        if (main.getGame().isGameStarted()) {
            if (ploc.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.REDSTONE_BLOCK)) {
                if (main.getGame().defenders.contains(p.getUniqueId())) {
                    main.reloadConfig();
                    Location locTP1 = new Location(
                            Bukkit.getWorld(main.getConfig().getString("skydefendersave.tp1.world")),
                            main.getConfig().getDouble("skydefendersave.tp1.x"),
                            main.getConfig().getDouble("skydefendersave.tp1.y"),
                            main.getConfig().getDouble("skydefendersave.tp1.z"));
                    Location locTP2 = new Location(
                            Bukkit.getWorld(main.getConfig().getString("skydefendersave.tp2.world")),
                            main.getConfig().getDouble("skydefendersave.tp2.x"),
                            main.getConfig().getDouble("skydefendersave.tp2.y"),
                            main.getConfig().getDouble("skydefendersave.tp2.z"));

                    Location playerLocationModified = new Location(ploc.getWorld(), ploc.getX(), ploc.getY() - 1,
                            ploc.getZ());

                    if (BlockLocationChecker.blockLocationCheck(playerLocationModified, locTP1)) {

                        Location locTP2modified = new Location(locTP2.getWorld(), locTP2.getX() + 1.0,
                                locTP2.getY() + 1.0, locTP2.getZ());
                        p.teleport(locTP2modified);
                        p.sendMessage("§aVous avez été téléporté(e) !");
                    }
                    if (BlockLocationChecker.blockLocationCheck(playerLocationModified, locTP2)) {

                        Location locTP1modified = new Location(locTP1.getWorld(), locTP1.getX() + 1.0,
                                locTP1.getY() + 1.0, locTP1.getZ());
                        p.teleport(locTP1modified);
                        p.sendMessage("§aVous avez été téléporté(e) !");
                    }

                } else {
                    p.sendMessage("§cSeulement les défenseurs peuvent utiliser le téléporteur !");

                }
            }
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack it = event.getItem();
        if (!main.getGame().isGameStarted()) {
            if (it != null) {
                if (main.getConfig().getConfigurationSection("skydefenderconfig.teamchooser")
                        .getBoolean("enabled")) {


                    if (it.getType() == Material.WHITE_BANNER && it.hasItemMeta()
                            && it.getItemMeta().getDisplayName().equals("§eChoisir une équipe")) {
                        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                            Inventory teamchoosergui = Bukkit.createInventory(null, 18, "§8Choisir une équipe");

                            teamchoosergui.setItem(3, getItem(Material.BLUE_BANNER, "§9Défenseur"));
                            teamchoosergui.setItem(4, getItem(Material.RED_BANNER, "§cAttaquant"));
                            teamchoosergui.setItem(5, getItem(Material.WHITE_BANNER, "§fAléatoire"));
                            teamchoosergui.setItem(13, getItem(Material.RED_WOOL, "§4Fermer"));
                            teamchoosergui.setItem(8, getItem(Material.PURPLE_BANNER, "§5Spectateur"));

                            player.openInventory(teamchoosergui);

                        }
                    }

                }


            } else {
                return;
            }

        }
        if (main.getGame().isPaused()) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void ondrop(PlayerDropItemEvent e) {

        if (!main.getGame().isGameStarted() || main.getGame().isPaused()) {
            e.setCancelled(true);

        }

    }


    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (main.getGame().isGameStarted()) {
            if (e.getEntityType() == EntityType.PLAYER) {

                Player player = e.getEntity();

                if (player.getKiller() != null) {

                    Player killer = player.getKiller();
                    if (!main.getGame().kills.containsKey(killer.getUniqueId())) {
                        main.getGame().kills.put(killer.getUniqueId(), 0);
                    }

                    main.getGame().kills.put(killer.getUniqueId(), main.getGame().kills.get(killer.getUniqueId()) + 1);


                    killer.sendMessage("§bVous avez éliminé(e) §c" + player.getName());


                }

                e.setDeathMessage("§1" + player.getName() + "§9 est mort !");
                if (main.getGame().attackers.contains(player.getUniqueId())) {
                    main.getGame().attackers.remove(player.getUniqueId());
                } else if (main.getGame().defenders.contains(player.getUniqueId())) {
                    main.getGame().defenders.remove(player.getUniqueId());
                }
                if (!main.getGame().spectators.contains(player.getUniqueId())) {
                    main.getGame().spectators.add(player.getUniqueId());
                }
                main.getGame().players.remove(player.getUniqueId());

                World w = player.getWorld();
                player.setGameMode(GameMode.SPECTATOR);
                w.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 5, 1);

                if (main.getGame().attackers.size() == 0) {
                    SkyDefenderEnd end = new SkyDefenderEnd(main);

                    int num = 0;
                    while (main.getGame().defenders.get(num) != null) {

                        if (Bukkit.getPlayer(main.getGame().defenders.get(num)) != null) {
                            end.winTheGame(Bukkit.getPlayer(main.getGame().defenders.get(num)));
                            break;
                        }
                        num++;
                    }

                }
                for (Kit kit : Kit.values()) {
                    if (kit.getPlayersKit().contains(player.getUniqueId())) {
                        kit.getPlayersKit().remove(player.getUniqueId());
                        break;
                    }
                }

            }

        }

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {

        if (!main.getGame().isGameStarted()) {

            Player player = e.getPlayer();
            player.getInventory().clear();

            ItemStack teamchooser = new ItemStack(Material.WHITE_BANNER, 1);
            ItemMeta teamchoosermeta = teamchooser.getItemMeta();
            teamchoosermeta.addEnchant(Enchantment.DAMAGE_ALL, 0, true);
            teamchoosermeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            teamchoosermeta.setDisplayName("§eChoisir une équipe");
            teamchoosermeta.setLore(Arrays.asList("§cVeuillez choisir une équipe"));

            teamchooser.setItemMeta(teamchoosermeta);

            player.getInventory().setItem(4, teamchooser);
            player.updateInventory();
            World w = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.spawn.world"));
            double x = main.getConfig().getDouble("skydefendersave.spawn.x");
            double y = main.getConfig().getDouble("skydefendersave.spawn.y");
            double z = main.getConfig().getDouble("skydefendersave.spawn.z");
            player.teleport(new Location(w, x, y, z));

        } else {
            Player player = e.getPlayer();
            World w = Bukkit.getServer().getWorld(main.getConfig().getString("skydefendersave.spawn.world"));
            double x = main.getConfig().getDouble("skydefendersave.spawn.x");
            double y = main.getConfig().getDouble("skydefendersave.spawn.y");
            double z = main.getConfig().getDouble("skydefendersave.spawn.z");
            player.teleport(new Location(w, x, y, z));

        }
    }


    @EventHandler
    public void onPlayerDrinkMilk(PlayerItemConsumeEvent e) {
        if (e.getItem() == null) return;
        if (e.getItem().getType() != Material.MILK_BUCKET) return;

        List<PotionEffect> reapply = new ArrayList<>();
        label:
        for (PotionEffect pe : e.getPlayer().getActivePotionEffects()) {
            if (Kit.hasKit(e.getPlayer())) {
                switch (Kit.getPlayerKit(e.getPlayer()).getId()) {
                    case "berserker":

                        if (pe.getType().equals(PotionEffectType.INCREASE_DAMAGE) && pe.getAmplifier() == 0) {
                            reapply.add(pe);

                            break label;
                        }
                        break;
                    case "assassin":

                        if (pe.getType().equals(PotionEffectType.INVISIBILITY) && pe.getAmplifier() == 1) {
                            reapply.add(pe);
                            break label;
                        }
                        break;
                    case "tank":

                        if (pe.getType().equals(PotionEffectType.DAMAGE_RESISTANCE) && pe.getAmplifier() == 0) {
                            reapply.add(pe);
                            break label;
                        }
                        break;
                    case "archer":

                        if (pe.getType().equals(PotionEffectType.SPEED) && pe.getAmplifier() == 0) {
                            reapply.add(pe);
                            break label;
                        }
                        break;
                    case "elementaliste":
                        if (pe.getType().equals(PotionEffectType.FIRE_RESISTANCE) && pe.getAmplifier() == 0) {
                            reapply.add(pe);
                            break label;
                        }
                        break;
                }
            }

        }
        if (reapply.size() == 0) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                for (PotionEffect pe : reapply) e.getPlayer().addPotionEffect(pe);
            }
        }.runTaskLater(main, 2L);
    }

    public ItemStack getItem(Material material, String customName) {
        ItemStack it = new ItemStack(material, 1);
        ItemMeta itM = it.getItemMeta();
        if (customName != null)
            itM.setDisplayName(customName);
        it.setItemMeta(itM);

        it.setItemMeta(itM);
        return it;

    }
}
