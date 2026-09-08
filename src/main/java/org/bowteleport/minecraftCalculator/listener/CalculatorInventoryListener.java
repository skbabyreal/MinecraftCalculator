package org.bowteleport.minecraftCalculator.listener;

import org.bowteleport.minecraftCalculator.gui.CalculatorGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public final class CalculatorInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof CalculatorGUI calculatorGUI)) {
            return;
        }

        event.setCancelled(true);
        if (event.getClickedInventory() != event.getInventory()) {
            return;
        }

        calculatorGUI.handleButtonClick(event.getRawSlot());
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof CalculatorGUI) {
            event.setCancelled(true);
        }
    }
}
