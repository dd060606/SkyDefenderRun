package fr.dd06.skydefender.event;


import fr.dd06.skydefender.SkyDefenderRun;
import fr.dd06.skydefender.game.AutoStart;
import fr.dd06.skydefender.game.BannerAttack;
import fr.dd06.skydefender.game.SkyDefenderEnd;
import fr.dd06.skydefender.kits.Kit;
import fr.dd06.skydefender.scoreboards.CustomScoreBoard;
import fr.dd06.skydefender.utils.BlockLocationChecker;
import fr.dd06.skydefender.utils.BlockLoots;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class BlockEvents implements Listener {
    private SkyDefenderRun main;

    public BlockEvents(SkyDefenderRun skyDefenderRun) {
        this.main = skyDefenderRun;
    }









    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {

        if (!main.getGame().isGameStarted() || main.getGame().isPaused() == true) {
            Player player = event.getPlayer();
            if (player.getInventory().getItemInHand().getType() == Material.BANNER)
                event.setCancelled(true);
            if (player.getGameMode().equals(GameMode.CREATIVE)) {
                if (event.getBlock().getType() == Material.BANNER) {
                    event.setCancelled(true);
                }
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }

        }

    }


    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player breaker = event.getPlayer();
        Block blockbreaked = event.getBlock();
        Location locBlockBreaked = blockbreaked.getLocation();

        if (main.getGame().isGameStarted()) {
            if (main.getGame().isPaused()) {
                event.setCancelled(true);
            }
            if (blockbreaked.getType().equals(Material.STANDING_BANNER)) {
                Location bannerLoc = new Location(
                        Bukkit.getWorld(main.getConfig().getString("skydefendersave.banner.world")),
                        main.getConfig().getDouble("skydefendersave.banner.x"),
                        main.getConfig().getDouble("skydefendersave.banner.y"),
                        main.getConfig().getDouble("skydefendersave.banner.z"));

                if (BlockLocationChecker.blockLocationCheck(locBlockBreaked, bannerLoc)) {
                    if (main.getGame().defenders.contains(breaker.getUniqueId())) {
                        breaker.sendMessage("§cSeulement les attaquants peuvent détruire la bannière !");
                        event.setCancelled(true);

                    } else if (main.getGame().attackers.contains(breaker.getUniqueId())) {
                        if (main.getGame().defenders.size() <= 0) {
                            breaker.sendMessage(
                                    "§e[SkyDefenderRun] : §a" + breaker.getName() + " vient de détruire la bannière !");
                            SkyDefenderEnd skydefenderEnd = new SkyDefenderEnd(main);
                            skydefenderEnd.winTheGame(breaker);

                        } else {

                            if (BannerAttack.attacking) {
                                if (BannerAttack.minutes == main.getGame().getMinutesBannerCooldown()) {
                                    breaker.sendMessage(
                                            "§e[SkyDefenderRun] : §a" + breaker.getName() + " vient de détruire la bannière !");
                                    SkyDefenderEnd skydefenderEnd = new SkyDefenderEnd(main);
                                    skydefenderEnd.winTheGame(breaker);
                                    event.setCancelled(false);
                                } else {
                                    breaker.sendMessage("§cLe compteur est à " + BannerAttack.minutes + " mins et " + BannerAttack.secondes + " secs sur " + main.getGame().getMinutesBannerCooldown() + " minutes");
                                    event.setCancelled(true);

                                }
                            } else {

                                if (main.getGame().isAssaultEnabled()) {
                                    BannerAttack attack = new BannerAttack(main);
                                    attack.startAttack();
                                    event.setCancelled(true);

                                } else {
                                    breaker.sendMessage("§cLes assauts ne sont pas encore activés");
                                    event.setCancelled(true);


                                }

                            }
                        }
                    } else {
                        event.setCancelled(false);
                    }
                }
            }
            if (blockbreaked.getType().equals(Material.REDSTONE_BLOCK)) {
                Location tp1Loc = new Location(Bukkit.getWorld(main.getConfig().getString("skydefendersave.tp1.world")),
                        main.getConfig().getDouble("skydefendersave.tp1.x"),
                        main.getConfig().getDouble("skydefendersave.tp1.y"),
                        main.getConfig().getDouble("skydefendersave.tp1.z"));
                Location tp2Loc = new Location(Bukkit.getWorld(main.getConfig().getString("skydefendersave.tp2.world")),
                        main.getConfig().getDouble("skydefendersave.tp2.x"),
                        main.getConfig().getDouble("skydefendersave.tp2.y"),
                        main.getConfig().getDouble("skydefendersave.tp2.z"));

                if (BlockLocationChecker.blockLocationCheck(locBlockBreaked, tp1Loc)
                        || BlockLocationChecker.blockLocationCheck(locBlockBreaked, tp2Loc)) {
                    breaker.sendMessage("§cVous ne pouvez pas casser le téléporteur !");
                    event.setCancelled(true);
                } else {
                    event.setCancelled(false);
                }

            }

            if (blockbreaked.getType().equals(Material.LOG)) {
                Location blockLoc = blockbreaked.getLocation();
                World world = blockbreaked.getWorld();
                BlockLoots.breakTree(blockbreaked);


            }
            if (BlockLoots.blocks.contains(blockbreaked.getType())) {

                event.setDropItems(false);
                BlockLoots.dropLootFromBlock(blockbreaked);
            }
        } else {

            if (main.getGame().isPaused()) {
                event.setCancelled(true);
            }
            if (breaker.getGameMode().equals(GameMode.CREATIVE)) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        }
    }







}
