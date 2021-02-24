package fr.dd06.skydefender.pause;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveConfig {
	private static File saveConfigFile;

	private static FileConfiguration saveConfig;

	public static void reloadSaveConfig() {
		saveConfigFile = new File(Bukkit.getServer().getPluginManager().getPlugin("SkyDefenderRun").getDataFolder(),
				"save.yml");

		if (!saveConfigFile.exists()) {

			try {
				saveConfigFile.createNewFile();
				FileWriter fileWriter = new FileWriter(saveConfigFile);
				BufferedWriter writer = new BufferedWriter(fileWriter);
				writer.write("save:");
				writer.newLine();
				writer.write("  paused: false");
				writer.newLine();
				writer.write("  players:");
				writer.newLine();
				writer.write("  time:");
				writer.newLine();
				writer.write("    days: 0");
				writer.newLine();
				writer.write("    minutes: 0");
				writer.newLine();
				writer.write("    secondes: 0");
				writer.newLine();
				writer.write("  assault:");
				writer.newLine();
				writer.write("    enabled: false");
				writer.newLine();
				writer.write("    minutes: 0");
				writer.newLine();
				writer.write("    secondes: 0");
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		saveConfig = YamlConfiguration.loadConfiguration(saveConfigFile);
	}

	public static FileConfiguration getSaveConfig() {
		if (saveConfig == null) {
			reloadSaveConfig();
		}
		return saveConfig;
	}

	public static void saveSaveConfig() {
		if (saveConfig == null) {
			reloadSaveConfig();
		}
		try {
			saveConfig.save(saveConfigFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
