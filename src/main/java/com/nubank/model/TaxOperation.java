package com.nubank.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TaxOperation {

    @JsonProperty(value = "tax")
    private final BigDecimal tax;

    public TaxOperation(BigDecimal tax){
        this.tax = tax;
    }

    public TaxOperation(){
        this.tax = BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY);
    }

    public BigDecimal getTax(){
        return tax;
    }

}
