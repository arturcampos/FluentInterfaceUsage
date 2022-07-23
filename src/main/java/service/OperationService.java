package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.CapitalOperation;
import model.TaxOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OperationService {


    private Iterator<CapitalOperation> capitalGCapitalOperationIterator;

    private final ObjectMapper mapper;

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
        TaxService taxService = new TaxService();

        while (this.capitalGCapitalOperationIterator.hasNext()) {

            CapitalOperation capitalOperation = capitalGCapitalOperationIterator.next();

            switch (capitalOperation.getOperation()) {
                case BUY:
                    operationTaxList.add(taxService.doBuy(capitalOperation));
                    break;
                case SELL:
                    operationTaxList.add(taxService.doSell(capitalOperation));
                    break;
                default:
                    throw new RuntimeException("Operation not allowed");
            }
        }
        this.taxList = operationTaxList;
        return this;
    }

    public void print() throws JsonProcessingException {
        System.out.println(mapper.writeValueAsString(this.taxList));
    }





}
