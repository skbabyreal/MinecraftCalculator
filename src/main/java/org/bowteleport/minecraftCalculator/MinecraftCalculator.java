package org.bowteleport.minecraftCalculator;

import org.bowteleport.minecraftCalculator.command.CalculatorCommand;
import org.bowteleport.minecraftCalculator.listener.CalculatorInventoryListener;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinecraftCalculator extends JavaPlugin {

    String title = "+==============================================+\n" +
                   "|           _            _       _             |\n" +
                   "|  ___ __ _| | ___ _   _| | __ _| |_ ___  _ __ |\n" +
                   "| / __/ _` | |/ __| | | | |/ _` | __/ _ \\| '__||\n" +
                   "| (_| (_| | | (__| |_| | | (_| | || (_) | |   |\n" +
                   "| \\___\\__,_|_|\\___|\\__,_|_|\\__,_|\\__\\___/|_|   |\n" +
                   "+==============================================+";
    String cyan = "\u001B[36m";
    String reset = "\u001B[0m";

    @Override
    public void onEnable() {
        System.out.println(cyan + title + reset);
        System.out.println(cyan + "Developed by @skbaby" + reset);
        saveDefaultConfig();

        PluginCommand command = getCommand("calculator");
        if (command == null) {
            throw new IllegalStateException("Command 'calculator' is missing from plugin.yml");
        }
        command.setExecutor(new CalculatorCommand(this));
        getServer().getPluginManager().registerEvents(new CalculatorInventoryListener(), this);
    }

    public String getMessage(String path) {
        String message = getConfig().getString(
                "messages." + path,
                "&cMessaggio mancante: " + path
        );

        return colorize(message);
    }

    public String getResultMessage(String result) {
        String message = getConfig().getString(
                "messages.results-chat.format",
                "&aRisultato: &f{result}"
        );

        return colorize(message.replace("{result}", result));
    }    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
