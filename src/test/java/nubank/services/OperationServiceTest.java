package nubank.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubank.model.CapitalOperation;
import com.nubank.model.OperationType;
import com.nubank.model.TaxOperation;
import com.nubank.service.OperationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@DisplayName("OperationService Tests")
public class OperationServiceTest {

    private OperationService operationService = new OperationService();

    private final String capitalOperations = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},\n" +
            "{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 5000},\n" +
            "{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000}]";

    @BeforeEach
    public void setup() {
        operationService = new OperationService();
    }

    @Test
    @DisplayName("Should read an account json correctly")
    public void shouldReadJsonStringAndConvert() {
        Assertions.assertNotNull(operationService.readAndConvertJsonFromString(capitalOperations));
    }

    @Test
    @DisplayName("Should accept mapper as a config")
    public void shouldAcceptMapperAsConfig() {
        ObjectMapper mapper = new ObjectMapper();
        OperationService operationService = new OperationService(mapper);
        Assertions.assertNotNull(operationService.getObjectMapper());
    }

    @Test
    @DisplayName("Should execute operation without taxes")
    public void shouldExecuteOperationWithoutTaxes(){
        operationService.setCapitalGCapitalOperationIterator(setUpCapitalOperationList());
        List<TaxOperation> taxOperationList = operationService.executeOperation().getTaxOperationList();
        Assertions.assertEquals(3, taxOperationList.size());

        taxOperationList.forEach(taxOperation -> Assertions.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), taxOperation.getTax()));
    }

    @Test
    @DisplayName("Should execute operation with taxes")
    public void shouldExecuteOperation(){
        operationService.setCapitalGCapitalOperationIterator(setUpCapitalOperationsTaxTest());
        List<TaxOperation> taxOperationList = operationService.executeOperation().getTaxOperationList();
        Assertions.assertEquals(9, taxOperationList.size());

        Assertions.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), taxOperationList.get(0).getTax());
        Assertions.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), taxOperationList.get(1).getTax());
        Assertions.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), taxOperationList.get(2).getTax());
        Assertions.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), taxOperationList.get(3).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(3000).setScale(2, RoundingMode.UNNECESSARY), taxOperationList.get(4).getTax());
        Assertions.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), taxOperationList.get(5).getTax());
        Assertions.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), taxOperationList.get(6).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(3700).setScale(2, RoundingMode.UNNECESSARY), taxOperationList.get(7).getTax());
        Assertions.assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY), taxOperationList.get(8).getTax());

    }
    /*@Test
    @DisplayName("Should execute operation if exists")
    public void shouldExecuteOperationIfNotEmpty() {
        Account account = setUpAccountOperation(true, 100);
        Optional<Account> accountResult = operationService
                .setOperation(account)
                .executeOperation();
        Assertions.assertTrue(accountResult.isPresent());
    }

    @Test
    @DisplayName("Should not execute operation if its null")
    public void shouldNotExecuteIfOperationIsNotSet() {
        Optional<Account> accountResult = operationService.executeOperation();
        Assertions.assertTrue(accountResult.isEmpty());
    }

    @Test
    @DisplayName("Should give account as an argument when executing an operation")
    public void shouldPassAccountWhenExecutingOperation() {
        Account account = setUpAccountOperation(true, 100);
        Optional<Account> accountResult = operationService
                .setOperation(account)
                .executeOperation();
        Assertions.assertTrue(accountResult.isPresent());
    }

    @Test
    @DisplayName("Should not update current account if there is violations")
    public void shouldNotUpdateAccountIfOperationViolationsIsNotEmpty() {
        Account accountOperation = setUpAccountOperation(true, 1000);
        accountOperation.setViolations(new HashSet<String>(Collections.singleton("account-already-initialized")));
        operationService.updateAccount(accountOperation);
        Assertions.assertNotEquals(operationService.getAccount(), accountOperation);
    }

    @Test
    @DisplayName("Should update current account if there is not violations")
    public void shouldUpdateAccountIfOperationViolationsIsEmpty() {

        Account accountOperation = setUpAccountOperation(true, 1000);
        operationService
                .setOperation(accountOperation)
                .executeOperation()
                .ifPresent(operationService::updateAccount);

        Account accountBeforeOperation = operationService.getAccount();


        Transaction transactionOperation = setUpTransaction("Uber Eats", 25, LocalDateTime.now());
        operationService
                .setOperation(transactionOperation)
                .executeOperation()
                .ifPresent(operationService::updateAccount);

        Account accountAfterOperation = operationService.getAccount();

        Assertions.assertNotEquals(accountBeforeOperation, accountAfterOperation);
        Assertions.assertNotEquals(
                accountBeforeOperation.getAccountDetails().getAvailableLimit(),
                accountAfterOperation.getAccountDetails().getAvailableLimit());

    }

    @Test
    @DisplayName("Should report status")
    public void shouldReportStatus() {

        Account accountOperation = setUpAccountOperation(true, 1000);
        OperationService reportReturn = operationService
                .reportStatus(accountOperation);

        Assertions.assertNotNull(reportReturn);
    }

    private Account setUpAccountOperation(boolean active, Integer limit) {
        AccountDetails accountDetails = new AccountDetails(active, limit);
        return new Account(accountDetails);
    }

    private Transaction setUpTransaction(String merchant, Integer Amount, LocalDateTime time) {
        TransactionDetails transactionDetails = new TransactionDetails("Uber Eats", 25, LocalDateTime.now());
        Transaction transaction = new Transaction(transactionDetails);
        return transaction;
    }*/

    private Iterator<CapitalOperation> setUpCapitalOperationList(){
            List<CapitalOperation> operationsList = new ArrayList<>();

            operationsList.add(new CapitalOperation(OperationType.BUY, 10.00d, 100));
            operationsList.add(new CapitalOperation(OperationType.SELL, 15.00d, 50));
            operationsList.add(new CapitalOperation(OperationType.BUY, 15.00d, 50));

            return operationsList.iterator();

    }

    private Iterator<CapitalOperation> setUpCapitalOperationsTaxTest(){

        List<CapitalOperation> operationsList = new ArrayList<>();
        operationsList.add(new CapitalOperation(OperationType.BUY, 10.00d, 10000));
        operationsList.add(new CapitalOperation(OperationType.SELL, 2.00d, 5000));
        operationsList.add(new CapitalOperation(OperationType.SELL, 20.00d, 2000));
        operationsList.add(new CapitalOperation(OperationType.SELL, 20.00d, 2000));
        operationsList.add(new CapitalOperation(OperationType.SELL, 25.00d, 1000));
        operationsList.add(new CapitalOperation(OperationType.BUY, 20.00d, 10000));
        operationsList.add(new CapitalOperation(OperationType.SELL, 15.00d, 5000));
        operationsList.add(new CapitalOperation(OperationType.SELL, 30.00d, 4350));
        operationsList.add(new CapitalOperation(OperationType.SELL, 30.00d, 650));

        return operationsList.iterator();
    }

}