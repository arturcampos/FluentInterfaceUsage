package com.nubank.service;

import com.nubank.model.CapitalOperation;
import com.nubank.model.TaxOperation;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 *  This class executes the operations following their type:
 *  BUY - Calculate weightedAverage and tax related to the operation
 *  SELL - Calculate profit, expense and taxes related to the operation
 *  */
public class TaxService {

    private static final Double TAX_FREE_VALUE = 20000d;

    private static final Double TAX_RATE = 0.2;

    private Double weightedAverage = 0.00d;

    private Double currentWeightedAverage = 0.00d;

    private Integer currentStocksQuantity = 0;

    private Double expense = 0.00d;

    /**
     * Calculate the weightedAverage cost base on some information
     *
     * @param currentStocksQuantity current quantity of stocks
     * @param currentWeightedAverage current weighted average cost
     * @param operationStocksQuantity the quantity of stocks on the current operation being processed
     * @param operationUnitCost the unitCost of a stock
     * @return the new weighted average cost
     */
    private Double calculateWeightedAverage(Integer currentStocksQuantity, Double currentWeightedAverage,
                                            Integer operationStocksQuantity, Double operationUnitCost){

        return ((currentStocksQuantity * currentWeightedAverage)
                + (operationStocksQuantity * operationUnitCost))
                / (currentStocksQuantity + operationStocksQuantity);

    }

    /**
     * Calculate the expense cost base on some information
     *
     * @param weightedAverage weighted average cost of the stocks
     * @param operationStocksQuantity the quantity of stocks on the current operation being processed
     * @param operationUnitCost the unitCost of a stock
     * @return the expense value of the operation
     */
    private Double calculateExpense(Double weightedAverage, Integer operationStocksQuantity, Double operationUnitCost){
        return (weightedAverage * operationStocksQuantity) - (operationUnitCost * operationStocksQuantity);
    }

    /**
     * Calculate the expense cost base on some information
     *
     * @param weightedAverage weighted average cost of the stocks
     * @param operationStocksQuantity the quantity of stocks on the current operation being processed
     * @param operationUnitCost the unitCost of a stock
     * @return the profit value of the operation
     */
    private Double calculateProfit(Double operationUnitCost, Integer operationStocksQuantity, Double weightedAverage){
        return (operationUnitCost * operationStocksQuantity) - (weightedAverage * operationStocksQuantity);
    }

    /**
     * Execute BUY operation base on the capital gain operation in the input
     *
     * @param capitalOperation
     * @return TaxOperation containing the tax of the operation (Buying always has 0.00 tax).
     */
    public TaxOperation doBuy(CapitalOperation capitalOperation){
        this.weightedAverage = calculateWeightedAverage(currentStocksQuantity, currentWeightedAverage,
                capitalOperation.getQuantity(),capitalOperation.getUnitCost());

        this.currentStocksQuantity += capitalOperation.getQuantity();
        this.currentWeightedAverage = this.weightedAverage;

        return new TaxOperation();

    }

    /**
     * Execute SELL operation base on the capital gain operation in the input
     *
     * @param capitalOperation
     * @return TaxOperation containing the tax of the operation.
     */
    public TaxOperation doSell(CapitalOperation capitalOperation){
        double profit = 0.00d;
        currentStocksQuantity -= capitalOperation.getQuantity();

        if (capitalOperation.getUnitCost() < weightedAverage) {
            expense += calculateExpense(weightedAverage, capitalOperation.getQuantity(), capitalOperation.getUnitCost());
        } else {

            double totalOperationValue = (capitalOperation.getQuantity() * capitalOperation.getUnitCost());
            profit += calculateProfit(capitalOperation.getUnitCost(), capitalOperation.getQuantity(), weightedAverage);

            if (profit > expense) profit -= expense;
            else {
                expense -= profit;
                totalOperationValue = profit;
            }

            if(totalOperationValue > TAX_FREE_VALUE)
                return new TaxOperation(BigDecimal.valueOf(profit * TAX_RATE).setScale(2, RoundingMode.UNNECESSARY));
        }

        return new TaxOperation();

    }
}
