package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.CapitalOperation;
import model.TaxOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OperationService {

    private static final Double TAX_FREE_VALUE = 20000d;

    private static final Double TAX_RATE = 0.2;

    private Iterator<CapitalOperation> capitalGCapitalOperationIterator;

    private final ObjectMapper mapper;

    Double weightedAverage = 0.00d;

    Double currentWeightedAverage = 0.00d;

    Integer currentStocksQuantity = 0;

    Double expense = 0.00d;

    private List<TaxOperation> taxList = new ArrayList<>();

    public OperationService(ObjectMapper mapper){
        this.mapper = mapper;
    }

    /**
     * Read the json and convert to a primary Map, it's used later to check certain keys to know the right operation
     * to instantiate
     *
     * @param json the json input as a String
     * @return OperationService following a FluentInterface design.
     */
    public OperationService readAndConvertJsonFromString(String json) {

        try {
            capitalGCapitalOperationIterator = mapper.readerFor(CapitalOperation.class).readValues(json);
        } catch (IOException e) {
            System.out.println("Error parsing JSON");
        }
        return this;
    }

    public OperationService executeOperation() {

        List<TaxOperation> operationTaxList = new ArrayList<>();

        while (this.capitalGCapitalOperationIterator.hasNext()) {

            CapitalOperation capitalOperation = capitalGCapitalOperationIterator.next();

            switch (capitalOperation.getOperation()) {
                case BUY:
                    doBuy(capitalOperation);
                    operationTaxList.add(new TaxOperation());
                    break;
                case SELL:
                    operationTaxList.add(doSell(capitalOperation));
                    break;
                default:
                    throw new RuntimeException("Operation not allowed");
            }
        }
        this.taxList = operationTaxList;
        return this;
    }

    public void print(){
        System.out.println(this.taxList);
    }


    private Double calculateWeightedAverage(int currentStocksQuantity, Double currentWeightedAverage,
                                            int operationStocksQuantity, Double operationUnitCost){

        return ((currentStocksQuantity * currentWeightedAverage)
                + (operationStocksQuantity * operationUnitCost))
                / (currentStocksQuantity + operationStocksQuantity);

    }

    private Double calculateExpense(Double weightedAverage, int operationStocksQuantity, Double operationUnityCost){
      return  (weightedAverage * operationStocksQuantity) - (operationUnityCost * operationStocksQuantity);
    }

    private Double calculateProfit(Double operationUnityCost, int operationStocksQuantity, Double weightedAverage){

        return (operationUnityCost * operationStocksQuantity) - (weightedAverage * operationStocksQuantity);
    }

    private void doBuy(CapitalOperation capitalOperation){
        weightedAverage = calculateWeightedAverage(currentStocksQuantity, currentWeightedAverage,
                capitalOperation.getQuantity(),capitalOperation.getUnitCost());

        currentStocksQuantity += capitalOperation.getQuantity();
        currentWeightedAverage = weightedAverage;
    }

    private TaxOperation doSell(CapitalOperation capitalOperation){
        double profit = 0.00d;
        TaxOperation tax;
        currentStocksQuantity -= capitalOperation.getQuantity();

        if (capitalOperation.getUnitCost() < weightedAverage) {
            expense += calculateExpense(weightedAverage, capitalOperation.getQuantity(), capitalOperation.getUnitCost());
            tax = new TaxOperation();

        } else {

            double totalOperationValue = (capitalOperation.getQuantity() * capitalOperation.getUnitCost());
            profit += calculateProfit(capitalOperation.getUnitCost(), capitalOperation.getQuantity(), weightedAverage);

            if (profit > expense) profit -= expense;
            else {
                expense -= profit;
                totalOperationValue = profit;
            }

            tax = (totalOperationValue <= TAX_FREE_VALUE) ?  new TaxOperation() : new TaxOperation(profit * TAX_RATE);
        }

        return tax;
    }


}
