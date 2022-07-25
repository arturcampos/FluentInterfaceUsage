package com.nubank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubank.model.CapitalOperation;
import com.nubank.model.TaxOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * OperationService is who coordinates the whole process of reading a JSON input,
 * selecting the right operation and executing the correct operation.
 *
 * This class also is responsible for the output format.
 */
public class OperationService {


    private Iterator<CapitalOperation> capitalGCapitalOperationIterator;

    private final ObjectMapper mapper;

    private List<TaxOperation> taxOperationList;

    public OperationService(){
        this.mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    }
    public OperationService(ObjectMapper mapper){
        this.mapper = mapper;
    }

    /**
     * Read the json and convert to an Iterator of CapitalGain
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

    /**
     * Execute capital gains operations base on the CapitalGain Iterator present in the instance of the object
     *
     * @return OperationService following a FluentInterface design.
     */
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

        this.taxOperationList = operationTaxList;
        return this;
    }

    /**
     * Print to the stdout in json format, the tax list processed
     *
     */
    public void print() throws JsonProcessingException {
        System.out.println(mapper.writeValueAsString(this.taxOperationList));
    }

    /**
     * Retrieve the objectMapper present in the instance
     *
     * @return ObjectMapper from jackson specification
     */
    public ObjectMapper getObjectMapper(){
        return this.mapper;
    }

    public void setCapitalGCapitalOperationIterator(Iterator<CapitalOperation> iterator){
        this.capitalGCapitalOperationIterator = iterator;
    }

    public List<TaxOperation> getTaxOperationList(){
        return this.taxOperationList;
    }





}
