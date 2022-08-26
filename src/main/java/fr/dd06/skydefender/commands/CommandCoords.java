package fr.dd06.skydefender.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCoords implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(s.equalsIgnoreCase("coords")) {
            if(commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage("Coordonnées : " + "X: " + (int) player.getLocation().getX() + " Y: " +(int) player.getLocation().getY() + " Z : " +  (int)player.getLocation().getZ());
            }
            return true;
        }
        return false;
    }
}
