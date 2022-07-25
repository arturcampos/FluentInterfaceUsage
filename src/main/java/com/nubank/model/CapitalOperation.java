package com.nubank.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used for receive a capital gain operation
 * An CapitalOperation is required to be BUY or SELL OperationType
 *
 */
public class CapitalOperation {

    @JsonProperty("operation")
    private OperationType operation;

    @JsonProperty("unit-cost")
    private double unitCost;

    @JsonProperty("quantity")
    private Integer quantity;

    public CapitalOperation(OperationType operation, double unitCost, Integer quantity){
        this.operation = operation;
        this.unitCost = unitCost;
        this.quantity = quantity;
    }

    /**
     * Default constructor is obligatory when using Jackson java library
     */
    public CapitalOperation(){}

    public OperationType getOperation() {
        return operation;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
