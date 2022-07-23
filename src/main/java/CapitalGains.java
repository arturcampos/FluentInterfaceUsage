
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.OperationService;

import java.io.*;


public class CapitalGains {



    public static void main(String... args) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        OperationService operationService = new OperationService(mapper);
        InputStreamReader isReader = new InputStreamReader(System.in);
        BufferedReader bufReader = new BufferedReader(isReader);
        StringBuilder operationStr = new StringBuilder();
        String inputStr;
        while ((inputStr = bufReader.readLine()) != null) {
            operationStr.append(inputStr);
            if(operationStr.toString().startsWith("[") && operationStr.toString().endsWith(("]"))) {
                operationService
                        .readAndConvertJsonFromString(operationStr.toString())
                        .executeOperation()
                        .print();
                operationStr = new StringBuilder();
            }
        }
    }
}
