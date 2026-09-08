package org.bowteleport.minecraftCalculator.calculator;

public final class Calculator {

    private String currentInput = "";
    private double previousValue;
    private Operation operation;
    private CalculatorState state = CalculatorState.EMPTY;
    private String errorMessage = "";

    public void inputNumber(int number) {
        if (number < 0 || number > 9) {
            throw new IllegalArgumentException("Number must be between 0 and 9");
        }

        if (state == CalculatorState.RESULT || state == CalculatorState.ERROR) {
            clear();
        }
        if (state == CalculatorState.OPERATION_SELECTED) {
            currentInput = "";
            state = CalculatorState.INPUT_SECOND_NUMBER;
        }
        if (state == CalculatorState.EMPTY) {
            state = CalculatorState.INPUT_FIRST_NUMBER;
        }
        currentInput += number;
    }

    public void inputDecimal() {
        if (state == CalculatorState.RESULT || state == CalculatorState.ERROR) {
            clear();
        }
        if (state == CalculatorState.OPERATION_SELECTED) {
            currentInput = "0";
            state = CalculatorState.INPUT_SECOND_NUMBER;
        } else if (state == CalculatorState.EMPTY) {
            currentInput = "0";
            state = CalculatorState.INPUT_FIRST_NUMBER;
        }
        if (!currentInput.contains(".")) {
            currentInput += ".";
        }
    }

    public void inputOperation(Operation newOperation) {
        if (newOperation == null) {
            return;
        }
        if (state == CalculatorState.INPUT_FIRST_NUMBER && hasParsableInput()) {
            previousValue = parseInput();
            operation = newOperation;
            currentInput = "";
            state = CalculatorState.OPERATION_SELECTED;
        } else if (state == CalculatorState.OPERATION_SELECTED) {
            operation = newOperation;
        } else if (state == CalculatorState.RESULT && hasParsableInput()) {
            previousValue = parseInput();
            operation = newOperation;
            currentInput = "";
            state = CalculatorState.OPERATION_SELECTED;
        }
    }

    public void calculate() {
        if (state != CalculatorState.INPUT_SECOND_NUMBER
                || operation == null
                || !hasParsableInput()) {
            state = CalculatorState.ERROR;
            errorMessage = "invalid-calculation";
            return;
        }

        double secondValue = parseInput();
        if (operation == Operation.DIVISION && secondValue == 0) {
            state = CalculatorState.ERROR;
            errorMessage = "division-by-zero";
            return;
        }

        double result = switch (operation) {
            case ADDITION -> previousValue + secondValue;
            case SUBTRACTION -> previousValue - secondValue;
            case MULTIPLICATION -> previousValue * secondValue;
            case DIVISION -> previousValue / secondValue;
        };

        if (!Double.isFinite(result)) {
            state = CalculatorState.ERROR;
            errorMessage = "invalid-calculation";
            return;
        }

        currentInput = format(result);
        state = CalculatorState.RESULT;
        errorMessage = "";
    }

    public void clear() {
        currentInput = "";
        previousValue = 0;
        operation = null;
        state = CalculatorState.EMPTY;
        errorMessage = "";
    }

    public void backspace() {
        if (currentInput.isEmpty()) {
            return;
        }
        currentInput = currentInput.substring(0, currentInput.length() - 1);
        if (currentInput.isEmpty()) {
            state = state == CalculatorState.INPUT_SECOND_NUMBER
                    ? CalculatorState.OPERATION_SELECTED
                    : CalculatorState.EMPTY;
        }
    }

    public String getDisplay() {
        return currentInput.isEmpty() ? "0" : currentInput;
    }

    public CalculatorState getState() {
        return state;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private boolean hasParsableInput() {
        if (currentInput.isBlank() || currentInput.equals(".")) {
            return false;
        }
        try {
            Double.parseDouble(currentInput);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private double parseInput() {
        return Double.parseDouble(currentInput);
    }

    private String format(double value) {
        if (value == (long) value) {
            return Long.toString((long) value);
        }
        return Double.toString(value);
    }
}
