package org.bowteleport.minecraftCalculator.gui;

import org.bowteleport.minecraftCalculator.calculator.Calculator;
import org.bowteleport.minecraftCalculator.calculator.Operation;

public final class CalculatorButton {

    private final ButtonType type;
    private final String displayName;
    private final int number;
    private final Operation operation;

    public CalculatorButton(ButtonType type, String displayName, int number, Operation operation) {
        this.type = type;
        this.displayName = displayName;
        this.number = number;
        this.operation = operation;
    }

    public void execute(Calculator calculator) {
        switch (type) {
            case NUMBER -> calculator.inputNumber(number);
            case DECIMAL -> calculator.inputDecimal();
            case OPERATION -> calculator.inputOperation(operation);
            case EQUALS -> calculator.calculate();
            case CLEAR -> calculator.clear();
            case BACKSPACE -> calculator.backspace();
        }
    }

    public ButtonType getType() {
        return type;
    }

    public String getDisplayName() {
        return displayName;
    }
}
