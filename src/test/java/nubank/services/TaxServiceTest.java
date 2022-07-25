package nubank.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubank.model.CapitalOperation;
import com.nubank.model.OperationType;
import com.nubank.model.TaxOperation;
import com.nubank.service.OperationService;
import com.nubank.service.TaxService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@DisplayName("TaxService Tests")
public class TaxServiceTest {

    private TaxService taxService = new TaxService();


    @BeforeEach
    public void setup() {
        taxService = new TaxService();
    }

    @Test
    @DisplayName("Should buy and return 0 taxes")
    public void shouldBuyingAndReturnTaxEqualsZero() {

        TaxOperation taxOperation = taxService.doBuy(setUpBuyingCapitalOperation());
        Assertions.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), taxOperation.getTax());
    }

    @Test
    @DisplayName("Should sell and return taxes")
    public void shouldSellAndReturnTaxes() {

        TaxOperation taxOperation = taxService.doSell(setUpSellingCapitalOperation());
        Assertions.assertEquals(BigDecimal.valueOf(4400).setScale(2, RoundingMode.UNNECESSARY), taxOperation.getTax());
    }

    @Test
    @DisplayName("Should sell with profit and then with expense")
    public void shouldSellWithProfitAndThenExpense(){

        TaxOperation taxOperation = taxService.doSell(setUpSellingCapitalOperationWithValue(11000, 2.00d));
        Assertions.assertEquals(BigDecimal.valueOf(4400).setScale(2, RoundingMode.UNNECESSARY), taxOperation.getTax());

        taxOperation = taxService.doSell(setUpSellingCapitalOperationWithValue(4500, 1.00d));
        Assertions.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), taxOperation.getTax());

        taxOperation = taxService.doSell(setUpSellingCapitalOperationWithValue(4500, 1.00d));
        Assertions.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), taxOperation.getTax());
    }

    private CapitalOperation setUpBuyingCapitalOperation(){

            return new CapitalOperation(OperationType.BUY, 10.00d, 100);
    }

    private CapitalOperation setUpSellingCapitalOperation(){

        return new CapitalOperation(OperationType.SELL, 2.00d, 11000);
    }

    private CapitalOperation setUpSellingCapitalOperationWithValue(int quantity, double unitCost){

        return new CapitalOperation(OperationType.SELL, unitCost, quantity);
    }

}