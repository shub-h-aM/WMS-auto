package com.wmsTest_Mahindra;

import Utilities.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wmsApiPayload.ApiPayload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class wmsMidboundFlow extends BaseClass{
    private RestAssuredAPIUtility api;
    private String sheetName = "APITestCases";
    private String relocationId = "";
    private String relocationCode = "";
    private String tranId = "";
    private String liId = "";
    private Sheet sheet;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String currentDate = dateFormat.format(new Date());
    private TestDataProvider dataProvider = new TestDataProvider();

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
    public void getForRelocationDetails(){
        String url = "/relocationDetails/getForReloc";
        // Make the API call and process the response using the helper method
        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), Collections.singletonMap("itemCode", itemCode));
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), getHeaders(), response);

        TestHelper.processApiResponse(response, fromLocationCode);

//            if (response.getStatusCode() == 200) {
//                String responseBody = response.getBody().asString();
//                JsonPath jsonPath = new JsonPath(responseBody);
//
//                List<Integer> liIds = jsonPath.getList("data.relocationDetails.liId");
//                List<String> lpnNos = jsonPath.getList("data.relocationDetails.lpnNo");
//                List<String> uidNos = jsonPath.getList("data.relocationDetails.uidNo");
//                List<String> locationCodes = jsonPath.getList("data.relocationDetails.locationCode");
//
//                // Find the index of fromLocationCode
//                int index = locationCodes.indexOf(fromLocationCode);
//
//                if (index >= 0 && index < liIds.size()) {
//                    liId = String.valueOf(liIds.get(index));
//                    String lpnNo = lpnNos.get(index);
//                    String uidNo = uidNos.get(index);
//
//                    System.out.println("liID: " + liId);
//                    System.out.println("lpnNo: " + lpnNo);
//                    System.out.println("uidNo: " + uidNo);
//                } else {
//                    System.out.println("fromLocationCode not found in response.");
//                }
//            } else {
//                System.out.println("HTTP request failed with status code: " + response.getStatusCode());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            // Handle exceptions or log them as needed
//        }
    }

    @Test
    public void testCreateReloaction() throws IOException {
        String url = "/relocation/create";
        // Test code
        Response response= api.postCallWithoutBody(url, getHeaders());
        RestAssuredAPIUtility.logResponseForPostCallWithoutBody(ExtentTestManager.getTest(), response);
        relocationId =response.jsonPath().getString("data.relocationId");
        relocationCode =response.jsonPath().getString("data.relocationCode");
        System.out.println("Relocation Id : "+relocationId);
        System.out.println("Relocation code : "+relocationCode);
    }
    @Test
    public void testCreateRelocationDetails(){
        String targetUseCase = "testCreateRelocationDetails";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.relocationDetailsPayload(testData.getPayload(),relocationId,liId,relocateQty,toLocationCode,itemCode);
        Response response = api.postCall(testData.getUrl(),getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }
    @Test
    public void testRelocationComp(){
        String url = "/relocation/comp";
        // Test code
        Response response = api.postCallRequestForText(url,relocationId);
        RestAssuredAPIUtility.logResponseForPostCallRequestForText(ExtentTestManager.getTest(), response);
    }

    @Test
    public void getForRelocationAssign(){
        String url = "/relocationDetails/getForAssign";
        Map<String, String> queryParams = ApiPayload.getDetailsByFromToDate(currentDate);
        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams, response);

        // Call the helper method to get relocation details by relocationId
        TestHelper.getRelocationDetailsByRelocationId(response,relocationId);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            JsonNode rootNode = objectMapper.readTree(responseBody);
//            JsonNode relocationDetails = rootNode.get("data").get("relocationDetails");
//
//            for (JsonNode relocationDetail : relocationDetails) {
//                int relocationIds = relocationDetail.get("relocationId").asInt();
//
//                if (String.valueOf(relocationIds).equals(relocationId)) {
//                    // Extract and print details for the desired relocationId
//                    String relocationCode = relocationDetail.get("relocationCode").asText();
//                    tranId = String.valueOf(relocationDetail.get("tranId").asInt());
//                    liId = String.valueOf(relocationDetail.get("liId").asInt());
//                    System.out.println("TranId: " + tranId);
//                    System.out.println("li Id: " + liId);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void testRelocationAssign(){
        String targetUseCase = "testRelocationAssign";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.relocationAssignPayload(testData.getPayload(),tranId,userId);
        Response response = api.postCall(testData.getUrl(),getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);

    }

    @Test
    public void getForAssignConfirm(){
        String url = "/relocationDetails/getForConfirm";
        Map<String, String> queryParams = ApiPayload.getDetailsByFromToDate(currentDate);

        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams, response);

    }

    @Test
    public void testAssignConfirm(){
        String targetUseCase = "testAssignConfirm";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.assignConfirmPayload(testData.getPayload(),relocationId, tranId, liId, relocateQty, toLocationCode);
        Response response = api.postCall(testData.getUrl(),getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }

    @Test
    public void testGetAllDetailsMethod(){
        System.out.println("Relocation Id : "+relocationId);
        System.out.println("Relocation code : "+relocationCode);
        System.out.println("TranId: " + tranId);
        System.out.println("Li Id : "+liId);
    }

}
