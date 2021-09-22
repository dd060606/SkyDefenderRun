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
import org.bukkit.block.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
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
            if (player.getInventory().getItemInMainHand().getType().equals(Material.WHITE_BANNER) || player.getInventory().getItemInOffHand().getType().equals(Material.WHITE_BANNER ) ) {
                event.setCancelled(true);
                return;
            }
            if (player.getGameMode().equals(GameMode.CREATIVE)) {
                if (event.getBlock().getType().equals(Material.WHITE_BANNER)) {
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
            if (blockbreaked.getType().equals(Material.WHITE_BANNER)) {
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

            int xbanner = (int) main.getConfig().getDouble("skydefendersave.banner.x");
            int ybanner = (int) main.getConfig().getDouble("skydefendersave.banner.y");
            int zbanner = (int) main.getConfig().getDouble("skydefendersave.banner.z");

            if(blockbreaked.getX() == xbanner && blockbreaked.getY() == ybanner -1 && blockbreaked.getZ() == zbanner) {
                event.setCancelled(true);
            }

            if((blockbreaked.getType().equals( Material.ACACIA_LOG)) || (blockbreaked.getType().equals(Material.BIRCH_LOG)) || (blockbreaked.getType().equals(Material.OAK_LOG)) || (blockbreaked.getType().equals(Material.DARK_OAK_LOG) ) || (blockbreaked.getType().equals(Material.JUNGLE_LOG)) || (blockbreaked.getType().equals(Material.SPRUCE_LOG)) )  {
                Vector<Block> tree = getTree(blockbreaked);
                for(Block currentTreeBlock : tree) {
                    if( (currentTreeBlock.getType().equals(Material.ACACIA_LEAVES)) || (currentTreeBlock.getType().equals(Material.BIRCH_LEAVES)) || (currentTreeBlock.getType().equals(Material.OAK_LEAVES) ) || (currentTreeBlock.getType().equals(Material.DARK_OAK_LEAVES)) || (currentTreeBlock.getType().equals(Material.JUNGLE_LEAVES)) || (currentTreeBlock.getType().equals(Material.SPRUCE_LEAVES)) ) {
                        Random random = new Random();
                        if(random.nextInt(15) == 1) {
                            currentTreeBlock.getLocation().getWorld().dropItemNaturally(currentTreeBlock.getLocation(), new ItemStack(Material.APPLE));
                        }
                    }
                    currentTreeBlock.breakNaturally();
                }
            }
            if (BlockLoots.blocks.contains(blockbreaked.getType())) {

                event.setDropItems(false);
                BlockLoots.dropLootFromBlock(blockbreaked, breaker);
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


    @EventHandler
    public void onStartBreaking(BlockDamageEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(main.getGame().isGameStarted()) {
            if(block.getType().equals(Material.OBSIDIAN )) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 8 * 20, 2));
            }
        }

    }


    @EventHandler
    public void onExplosion(EntityExplodeEvent  event) {

        if(main.getGame().isGameStarted()) {
            List<Block> blockListCopy = new ArrayList<Block>();
            blockListCopy.addAll(event.blockList());

            main.reloadConfig();
            int xtp1 = (int) main.getConfig().getDouble("skydefendersave.tp1.x");
            int ytp1 = (int) main.getConfig().getDouble("skydefendersave.tp1.y");
            int ztp1 = (int) main.getConfig().getDouble("skydefendersave.tp1.z");

            int xtp2 = (int) main.getConfig().getDouble("skydefendersave.tp2.x");
            int ytp2 = (int) main.getConfig().getDouble("skydefendersave.tp2.y");
            int ztp2 = (int) main.getConfig().getDouble("skydefendersave.tp2.z");

            int xbanner = (int) main.getConfig().getDouble("skydefendersave.banner.x");
            int ybanner = (int) main.getConfig().getDouble("skydefendersave.banner.y");
            int zbanner = (int) main.getConfig().getDouble("skydefendersave.banner.z");


            for (Block block : blockListCopy) {


                if (block.getType().equals(Material.REDSTONE_BLOCK)) {
                    if(block.getX() == xtp1 && block.getY() == ytp1 && block.getZ() == ztp1) {
                        event.blockList().remove(block);

                    }
                    if(block.getX() == xtp2 && block.getY() == ytp2 && block.getZ() == ztp2) {
                        event.blockList().remove(block);

                    }
                    if(block.getX() == xbanner && block.getY() == ybanner && block.getZ() == zbanner) {
                        event.blockList().remove(block);

                    }
                    if(block.getX() == xbanner && block.getY() == ybanner -1 && block.getZ() == zbanner) {
                        event.blockList().remove(block);
                    }
                }
            }
        }

    }

    public Vector<Block> getTree(Block startBlock) {
        Vector<Material> allowedBlocks = new Vector<Material>();
        allowedBlocks.add(Material.ACACIA_LOG);
        allowedBlocks.add(Material.BIRCH_LOG);
        allowedBlocks.add(Material.DARK_OAK_LOG);
        allowedBlocks.add(Material.JUNGLE_LOG);
        allowedBlocks.add(Material.OAK_LOG);
        allowedBlocks.add(Material.SPRUCE_LOG);
        allowedBlocks.add(Material.ACACIA_LEAVES);
        allowedBlocks.add(Material.AZALEA_LEAVES);
        allowedBlocks.add(Material.BIRCH_LEAVES);
        allowedBlocks.add(Material.DARK_OAK_LEAVES);
        allowedBlocks.add(Material.JUNGLE_LEAVES);
        allowedBlocks.add(Material.OAK_LEAVES);
        allowedBlocks.add(Material.SPRUCE_LEAVES);
        return getNextBlock(startBlock, allowedBlocks);
    }

    public Vector<Block> getNextBlock(Block startBlock, Vector<Material> allowedBlocks) {

        Vector<Block> blocks = new Vector<>();
        for(int x=-2; x<4; x++) {
            for(int y=-5; y<10; y++) {
                for(int z=-2; z<4; z++) {
                    Block block = startBlock.getLocation().add(x, y, z).getBlock();
                    if(block!=null && !blocks.contains(block) && allowedBlocks.contains(block.getType())) {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

}
