package Utilities;

import com.wmsApiPayload.ApiPayload;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;


public class RuleConfig extends BaseClass {
    private RestAssuredAPIUtility api;
    private TestDataProvider dataProvider = new TestDataProvider();
    private String sheetName = "APITestCases";
    private Sheet sheet;
    private String activeRuleId;

    @BeforeClass
    public void setup() throws Exception {
        api = new RestAssuredAPIUtility();
        fileName = dir + "/testData/APITestData.xlsx";
        BaseClass.sheetName = sheetName;
        System.out.println("XML FILE:-  " + fileName);
        initialiseProperties_Wms();
        RestAssured.useRelaxedHTTPSValidation();
        sheet = ExcelReaderUtil.getSheet(fileName, sheetName);
        dataProvider.loadTestData(sheet);
        testGetWarehouse();
    }


    @Test
    public void testSelectedWarehouse() throws IOException {
        String targetUseCase = "testSelectedWarehouse";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.selectWarehousePayload(testData.getPayload(), warehouseId);

        Response response= api.postCall(testData.getUrl(), getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }
    @Test
    public void testGetCheckRule(){
        String url = "/rule/check";
        Map<String, String> queryParams = ApiPayload.getCheckRule(ruleCode);

        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams, response);
    }
    @Test
    public void testGetAllActiveRule(){
        String url = "/rule/getactive/A";
        Map<String, String> queryParams = ApiPayload.getCheckRule(ruleCode);

        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams, response);
    }
    @Test
    public void testGetActiveRuleAtWarehouse(){
        String url = "/warehouserulemap/getByWhId/"+warehouseId+"";
        Map<String, String> queryParams = ApiPayload.warehouseActiveRule("A");

        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams, response);
        activeRuleId = response.jsonPath().getString("data.warehouseRuleMap.ruleId");
        System.out.println("At Warehouse Id : "+warehouseId+" || Active Rule Ids are : "+activeRuleId);
    }

    @Test
    public void testPutConfirm(){
        String targetUseCase = "testRuleUpdate";
        //here we have to provide which needs to be enable and + disable
        // we will need to send new list to be only active at last
        String ruleIdList = activeRuleId+"[2,45,46,48,53,55,56,34,6]";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.ruleUpdatePayload(testData.getPayload(),warehouseId,"");

        Response response = api.postCall(testData.getUrl(),getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }
}
