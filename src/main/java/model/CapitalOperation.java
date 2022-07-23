package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

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

    //
    public CapitalOperation(){}

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
