package fr.dd06.skydefender.event;

import fr.dd06.skydefender.SkyDefenderRun;
import fr.dd06.skydefender.game.GameData;
import fr.dd06.skydefender.utils.BlockLoots;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class LivingEvents implements Listener {

    private SkyDefenderRun main;

    public LivingEvents(SkyDefenderRun main) {
        this.main = main;
    }


    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!main.getGame().isGameStarted() || main.getGame().isPaused()) {
            Entity victime = e.getEntity();
            if (victime instanceof Player) {

                e.setCancelled(true);
            }
        }
        if(main.getGame().isGameStarted() && main.getGame().isGodTime()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent e) {

        if (!main.getGame().isGameStarted() || main.getGame().isPaused()) {
            Entity victime = e.getEntity();
            Entity damager = e.getDamager();
            if (victime instanceof Player) {
                e.setCancelled(true);
            }
            if (damager instanceof Player) {
                e.setCancelled(true);
            }
        } else {
            Entity victime = e.getEntity();
            Entity damager = e.getDamager();

            if (main.getGame().isPvpEnabled()) {
                if (victime instanceof Player) {
                    Player player = (Player) victime;

                    if (damager instanceof Player) {
                        Player pDamager = (Player) damager;
                        if (main.getGame().defenders.contains(pDamager.getUniqueId())
                                && main.getGame().defenders.contains(victime.getUniqueId())) {
                            pDamager.sendMessage("§cVous ne pouvez pas frapper un autre défenseur !");
                            e.setCancelled(true);
                        }
                    }

                    /*
                    if (player.getHealth() <= e.getDamage()) {
                        if (damager instanceof Player) {
                            Player killer = (Player) damager;

                            if (!main.getGame().kills.containsKey(killer.getUniqueId())) {
                                main.getGame().kills.put(killer.getUniqueId(), 0);
                            }

                            main.getGame().kills.put(killer.getUniqueId(), main.getGame().kills.get(killer.getUniqueId()) + 1);

                        }

                        e.getDamager().sendMessage("§bVous avez éliminé(e) §c" + victime.getName());
                    }

                    if (damager instanceof Arrow) {
                        Arrow arrow = (Arrow) damager;
                        if (arrow.getShooter() instanceof Player) {
                            Player killer = (Player) arrow.getShooter();
                            killer.playSound(killer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,10, 5);

                            if (!main.getGame().kills.containsKey(killer.getUniqueId())) {
                                main.getGame().kills.put(killer.getUniqueId(), 0);
                            }

                            main.getGame().kills.put(killer.getUniqueId(), main.getGame().kills.get(killer.getUniqueId()) + 1);

                        }
                    }

                     */
                }
            } else {
                if (victime instanceof Player) {
                    if (damager instanceof Player) {
                        e.setCancelled(true);
                        damager.sendMessage("§cLe PvP n'est pas encore activé !");
                    }

                }

            }
        }

    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {

        if (main.getGame().isGameStarted()) {
            if (BlockLoots.entities.contains(event.getEntityType())) {
                event.getDrops().clear();
                BlockLoots.dropLootFromEntity(event.getEntity());


            }
        }


    }
}
