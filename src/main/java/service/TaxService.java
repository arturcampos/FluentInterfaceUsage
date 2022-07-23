package service;

import model.CapitalOperation;
import model.TaxOperation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TaxService {

    private static final Double TAX_FREE_VALUE = 20000d;

    private static final Double TAX_RATE = 0.2;

    private Double weightedAverage = 0.00d;

    private Double currentWeightedAverage = 0.00d;

    private Integer currentStocksQuantity = 0;

    private Double expense = 0.00d;

    private Double calculateWeightedAverage(Integer currentStocksQuantity, Double currentWeightedAverage,
                                            Integer operationStocksQuantity, Double operationUnitCost){

        return ((currentStocksQuantity * currentWeightedAverage)
                + (operationStocksQuantity * operationUnitCost))
                / (currentStocksQuantity + operationStocksQuantity);

    }

    private Double calculateExpense(Double weightedAverage, Integer operationStocksQuantity, Double operationUnityCost){
        return (weightedAverage * operationStocksQuantity) - (operationUnityCost * operationStocksQuantity);
    }

    private Double calculateProfit(Double operationUnityCost, Integer operationStocksQuantity, Double weightedAverage){
        return (operationUnityCost * operationStocksQuantity) - (weightedAverage * operationStocksQuantity);
    }

    public TaxOperation doBuy(CapitalOperation capitalOperation){
        this.weightedAverage = calculateWeightedAverage(currentStocksQuantity, currentWeightedAverage,
                capitalOperation.getQuantity(),capitalOperation.getUnitCost());

        this.currentStocksQuantity += capitalOperation.getQuantity();
        this.currentWeightedAverage = this.weightedAverage;

        return new TaxOperation();

    }

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
