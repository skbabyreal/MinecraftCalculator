package org.bowteleport.minecraftCalculator.gui;

import org.bowteleport.minecraftCalculator.MinecraftCalculator;
import org.bowteleport.minecraftCalculator.calculator.Calculator;
import org.bowteleport.minecraftCalculator.calculator.CalculatorState;
import org.bowteleport.minecraftCalculator.calculator.Operation;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public final class CalculatorGUI implements InventoryHolder {

    private final MinecraftCalculator plugin;
    private final Player player;
    private final Calculator calculator = new Calculator();
    private final Map<Integer, CalculatorButton> buttons = new HashMap<>();
    private Inventory inventory;

    public CalculatorGUI(MinecraftCalculator plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        createButtons();
    }

    public void open() {
        int size = plugin.getConfig().getInt("gui.size", 36);
        if (size < 9 || size > 54 || size % 9 != 0) {
            plugin.getLogger().warning("gui.size must be a multiple of 9 between 9 and 54. Using 36.");
            size = 36;
        }

        String title = plugin.colorize(plugin.getConfig().getString("gui.title", "&8Calculator"));
        inventory = plugin.getServer().createInventory(this, size, title);
        setupBackground();
        setupButtons();
        setupDisplay();
        player.openInventory(inventory);
    }

    private void createButtons() {
        addNumberButton("0", 0);
        addNumberButton("1", 1);
        addNumberButton("2", 2);
        addNumberButton("3", 3);
        addNumberButton("4", 4);
        addNumberButton("5", 5);
        addNumberButton("6", 6);
        addNumberButton("7", 7);
        addNumberButton("8", 8);
        addNumberButton("9", 9);
        addButton("decimal", new CalculatorButton(ButtonType.DECIMAL, ".", -1, null));
        addButton("equals", new CalculatorButton(ButtonType.EQUALS, "=", -1, null));
        addButton("clear", new CalculatorButton(ButtonType.CLEAR, "C", -1, null));
        addButton("backspace", new CalculatorButton(ButtonType.BACKSPACE, "<-", -1, null));
        addButton("addition", operationButton("+", Operation.ADDITION));
        addButton("subtraction", operationButton("-", Operation.SUBTRACTION));
        addButton("multiplication", operationButton("*", Operation.MULTIPLICATION));
        addButton("division", operationButton("/", Operation.DIVISION));
    }

    private void addNumberButton(String key, int number) {
        addButton(key, new CalculatorButton(ButtonType.NUMBER, Integer.toString(number), number, null));
    }

    private CalculatorButton operationButton(String name, Operation operation) {
        return new CalculatorButton(ButtonType.OPERATION, name, -1, operation);
    }

    private void addButton(String key, CalculatorButton button) {
        int slot = plugin.getConfig().getInt("gui.button-slots." + key, -1);
        if (slot >= 0 && slot < 54) {
            buttons.put(slot, button);
        }
    }

    private void setupBackground() {
        ItemStack background = new ItemStack(material("gui.background-material", Material.GRAY_STAINED_GLASS_PANE));
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItem(slot, background);
        }
    }

    private void setupButtons() {
        for (Map.Entry<Integer, CalculatorButton> entry : buttons.entrySet()) {
            if (entry.getKey() >= inventory.getSize()) {
                continue;
            }
            inventory.setItem(entry.getKey(), createButtonItem(entry.getValue()));
        }
    }

    private void setupDisplay() {
        updateDisplay();
    }

    public void updateDisplay() {
        int slot = plugin.getConfig().getInt("gui.display-slot", 4);
        if (slot < 0 || slot >= inventory.getSize()) {
            return;
        }

        String display = calculator.getDisplay();
        if (calculator.getState() == CalculatorState.ERROR) {
            display = plugin.getMessage(calculator.getErrorMessage());
        }

        ItemStack item = new ItemStack(material("gui.materials.display", Material.BLACK_CONCRETE));
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(plugin.colorize("&f" + display));
            item.setItemMeta(meta);
        }
        inventory.setItem(slot, item);
    }

    private ItemStack createButtonItem(CalculatorButton button) {
        String materialPath = switch (button.getType()) {
            case NUMBER -> "gui.materials.number";
            case DECIMAL -> "gui.materials.decimal";
            case OPERATION -> "gui.materials.operation";
            case EQUALS -> "gui.materials.equals";
            case CLEAR -> "gui.materials.clear";
            case BACKSPACE -> "gui.materials.backspace";
        };

        ItemStack item = new ItemStack(material(materialPath, Material.STONE));
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(plugin.colorize("&f" + button.getDisplayName()));
            item.setItemMeta(meta);
        }
        return item;
    }

    private Material material(String path, Material fallback) {
        String value = plugin.getConfig().getString(path, fallback.name());
        Material material = Material.matchMaterial(value);
        if (material == null) {
            plugin.getLogger().warning("Invalid material '" + value + "' at " + path + ". Using " + fallback.name() + ".");
            return fallback;
        }
        return material;
    }

    public void handleButtonClick(int slot) {
        CalculatorButton button = buttons.get(slot);
        if (button == null) {
            return;
        }
        button.execute(calculator);
        updateDisplay();

        if (button.getType() == ButtonType.EQUALS && calculator.getState() == CalculatorState.RESULT
                && plugin.getConfig().getBoolean("messages.results-chat.enabled", true)) {
            player.sendMessage(plugin.getResultMessage(calculator.getDisplay()));
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
