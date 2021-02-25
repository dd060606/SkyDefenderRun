package fr.dd06.skydefender.game;

import fr.dd06.skydefender.GameTime;
import fr.dd06.skydefender.SkyDefenderRun;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class BannerAttack extends BukkitRunnable {


    public static int secondes = 0;

    public static int minutes = 0;

    public static boolean attacking = false;

    private SkyDefenderRun main;

    public BannerAttack(SkyDefenderRun main) {
        this.main = main;
    }

    public void setTime( int minutes, int secondes) {

        BannerAttack.minutes = minutes;
        BannerAttack.secondes = secondes;
    }


    @Override
    public void run() {
        if (secondes == 59) {
            secondes = 0;
            minutes++;
        }
        if(minutes == main.getGame().getMinutesBannerCooldown()) {
            this.cancel();
        }

        secondes++;



        if (main.getGame().isPaused()) {
            this.cancel();
        }
    }

    public void startAttack() {
        attacking = true;

        Bukkit.broadcastMessage("§e[SkyDefenderRun] : §cLe château est attaqué, les défenseurs ont " + main.getGame().getMinutesBannerCooldown()+
                " minutes pour défendre leur bannière !");

        this.runTaskTimer(main, 0, 20);

    }
}
