package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TaxOperation {

    private final double tax;

    public TaxOperation(double tax){
        this.tax = tax;
    }

    public TaxOperation(){
        this.tax = 0.00d;
    }

    @Override
    public String toString() {
        return "{\"tax\": "+BigDecimal.valueOf(tax).setScale(2, RoundingMode.UNNECESSARY)+"}";
    }
}
