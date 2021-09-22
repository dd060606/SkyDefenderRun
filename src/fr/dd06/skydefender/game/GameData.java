package fr.dd06.skydefender.game;

import fr.dd06.skydefender.GameTime;
import fr.dd06.skydefender.SkyDefenderRun;
import fr.dd06.skydefender.kits.Kit;
import fr.dd06.skydefender.pause.SaveConfig;
import fr.dd06.skydefender.scoreboards.CustomScoreBoard;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameData {


    private final SkyDefenderRun main;
    public ArrayList<UUID> players = new ArrayList<>();

    public HashMap<UUID, Integer> kills = new HashMap<>();

    public Map<UUID, CustomScoreBoard> boards = new HashMap<>();
    public Map<UUID, CustomScoreBoard> specboards = new HashMap<>();

    public ArrayList<UUID> defenders = new ArrayList<>();
    public ArrayList<UUID> attackers = new ArrayList<>();
    public ArrayList<UUID> spectators = new ArrayList<>();



    private boolean gameStarted = false;
    private boolean paused = false;
    private boolean pvp = false;
    private boolean godTime = false;
    private boolean assault = false;
    private boolean coordsMessage = false;
    private boolean borderActivated = false;


    private int timer = SkyDefenderRun.getInstance().getConfig().getInt("skydefenderconfig.starting.cooldown");
    private int minutesBeforeNextDay = SkyDefenderRun.getInstance().getConfig().getInt("skydefenderconfig.ingameconfig.minutesbeforenextday");
    private int daysBeforePvp = SkyDefenderRun.getInstance().getConfig().getInt("skydefenderconfig.ingameconfig.daysbeforepvp");
    private int daysCoordsMessage = SkyDefenderRun.getInstance().getConfig().getInt("skydefenderconfig.ingameconfig.dayscoordsmessage");
    private  int daysBeforeAssault = SkyDefenderRun.getInstance().getConfig().getInt("skydefenderconfig.ingameconfig.daysbeforeassault");
    private int minutesBannerCooldown = SkyDefenderRun.getInstance().getConfig().getInt("skydefenderconfig.ingameconfig.minutesbannercooldown");
    private int daysBeforeBorder= SkyDefenderRun.getInstance().getConfig().getInt("skydefenderconfig.ingameconfig.border.daysbeforeborder");

    public GameData(SkyDefenderRun skyDefenderRun) {
        this.main = skyDefenderRun;
    }

    public void updateScoreboards(UUID uuid) {
        if (boards.containsKey(uuid)) {
            CustomScoreBoard scoreboard = boards.get(uuid);
            scoreboard.updateTitle("§bSkyDefender");

            scoreboard.updateLine(0, "§e ");
            scoreboard.updateLine(1, "§e ");
            scoreboard.updateLine(2, "§9Joueurs : " + this.players.size());

            scoreboard.updateLine(3, "§eJour : " + GameTime.jour);
            scoreboard.updateLine(4, "§6Temps : " + GameTime.minutes + ":" + GameTime.secondes);
            if (kills.get(uuid) == null) {
               kills.put(uuid, 0);
                scoreboard.updateLine(5, "§cKills : " +kills.get(uuid));
            } else {
                scoreboard.updateLine(5, "§cKills : " + kills.get(uuid));
            }
            if(pvp) {
                scoreboard.updateLine(6, "§6PvP : §aOn");

            }
            else {
                scoreboard.updateLine(6, "§6PvP : §cOff");

            }

            if(assault) {
                scoreboard.updateLine(7, "§6Assaut : §aOn");

            }
            else {
                scoreboard.updateLine(7, "§6Assaut : §cOff");

            }
            if (this.defenders.contains(uuid)) {
                scoreboard.updateLine(8, "§7Equipe : §bDéfenseurs");
            } else if (this.attackers.contains(uuid)) {
                scoreboard.updateLine(8, "§7Equipe : §cAttaquants");
            } else if (this.spectators.contains(uuid)) {
                scoreboard.updateLine(8, "§7Equipe : §5Spectateurs");
            }


            if(BannerAttack.attacking) {
                scoreboard.updateLine(9, "§cAttaque : " + BannerAttack.minutes + "mins | "+BannerAttack.secondes + "secs/" + minutesBannerCooldown + "mins");
            }

            if(Kit.hasKit(Bukkit.getPlayer(uuid))) {

                scoreboard.updateLine(10, "§dKit : §c" + Kit.getPlayerKit(Bukkit.getPlayer(uuid)).getName());
            }
        }
    }

    public void updateSpectatorsBoards(UUID uuid) {
        if (specboards.containsKey(uuid)) {
            CustomScoreBoard scoreboard = specboards.get(uuid);
            scoreboard.updateTitle("§bSkyDefender");

            scoreboard.updateLine(0, "§e ");
            scoreboard.updateLine(1, "§e ");
            scoreboard.updateLine(2, "§9Joueurs : " + this.players.size());
            scoreboard.updateLine(3, "§eJour : " + GameTime.jour);
            scoreboard.updateLine(4, "§6Temps : " + GameTime.minutes + ":" + GameTime.secondes);

            if(pvp) {
                scoreboard.updateLine(5, "§6PvP : §aOn");

            }
            else {
                scoreboard.updateLine(5, "§6PvP : §cOff");

            }

            if(assault) {
                scoreboard.updateLine(6, "§6Assaut : §aOn");

            }
            else {
                scoreboard.updateLine(6, "§6Assaut : §cOff");

            }
            scoreboard.updateLine(7, "§7Equipe : §5Spectateurs");


            if(BannerAttack.attacking) {
                scoreboard.updateLine(8, "§cAttaque : " + BannerAttack.minutes + "mins | "+BannerAttack.secondes + "secs/" +minutesBannerCooldown + "mins");
            }

        }
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public int getMinutesBeforeNextDay() {
        return minutesBeforeNextDay;
    }

    public boolean isPvpEnabled() {
        return pvp;
    }

    public void setPvp(boolean enabled) {
        this.pvp = enabled;
    }

    public boolean isBorderActivated() { return borderActivated;}

    public void setBorderActivated(boolean enabled) { this.borderActivated = enabled;}

    public boolean isGodTime() {
        return godTime;
    }
    public void setGodTime(boolean enabled) {
        this.godTime = enabled;
    }


    public int getDaysBeforePvp() {
        return daysBeforePvp;
    }
    public int getDaysBeforeBorder() {
        return daysBeforeBorder;
    }


    public void setAssaultEnabled(boolean enabled) {
        this.assault = enabled;
    }

    public boolean isAssaultEnabled() {
        return assault;
    }

    public int getDaysBeforeAssault() {
        return daysBeforeAssault;
    }

    public int getMinutesBannerCooldown() { return minutesBannerCooldown; }


    public int getDaysCoordsMessage() { return daysCoordsMessage;}

    public boolean isPaused() {
        return paused;
    }
    public void setPaused(boolean enabled) {
        SaveConfig.reloadSaveConfig();
        SaveConfig.getSaveConfig().set("save.paused", enabled);
        SaveConfig.saveSaveConfig();

        paused = enabled;
    }
}

