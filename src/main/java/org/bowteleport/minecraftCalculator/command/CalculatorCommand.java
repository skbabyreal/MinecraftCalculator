package org.bowteleport.minecraftCalculator.command;

import org.bowteleport.minecraftCalculator.MinecraftCalculator;
import org.bowteleport.minecraftCalculator.gui.CalculatorGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CalculatorCommand implements CommandExecutor {

    private final MinecraftCalculator plugin;

    public CalculatorCommand(MinecraftCalculator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1 || (args.length == 1 && !args[0].equalsIgnoreCase("reload"))) {
            sender.sendMessage(plugin.getMessage("invalid-arguments"));
            return true;
        }

        if (args.length == 1) {
            if (!sender.hasPermission(plugin.getConfig().getString("permissions.reload", "minecraftcalculator.reload"))) {
                sender.sendMessage(plugin.getMessage("reload-no-permission"));
                return true;
            }
            plugin.reloadConfig();
            sender.sendMessage(plugin.getMessage("reload-success"));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getMessage("only-player"));
            return true;
        }

        String permission = plugin.getConfig().getString("permissions.use", "minecraftcalculator.use");
        if (!player.hasPermission(permission)) {
            player.sendMessage(plugin.getMessage("no-permission"));
            return true;
        }

        new CalculatorGUI(plugin, player).open();
        return true;
    }
}
