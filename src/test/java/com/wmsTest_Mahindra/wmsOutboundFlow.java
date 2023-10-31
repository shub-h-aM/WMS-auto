package com.wmsTest_Mahindra;

import Utilities.*;
import com.wmsApiPayload.ApiPayload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class wmsOutboundFlow extends BaseClass {
    private RestAssuredAPIUtility api;
    private String sheetName = "APITestCases";
    private Sheet sheet;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String currentDate = dateFormat.format(new Date());
    // Declare the global variables here
    private String sorterId;
    private String parentId;
    private String orderId;
    private String orderCode;
    private String orderType;
    private String orderDetailId;
    private String pickId;
    private String pickCode;
    private String pickDetailsId;
    private String pickTransId;
    private String pickerId;
    private String liId;
    private String kitId;
    private String kitCode;
    private String kitDetailId;
    private String confQuantity;
    private String shipmentId;
    private String shipmentCode;
    private String pickQuantity;
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
    public void testGetCheckRule(){
        String url = "/rule/check";
        Map<String, String> queryParams = ApiPayload.getCheckRule(ruleCode);

        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams, response);
        String enableRule = response.jsonPath().getString("data.rule.enableRule");
        System.out.println("Enable Rule : "+enableRule);
    }

    @Test()
    public void testOrderUpload() {
        String url = "/order/upload";
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        File filePath = new File(dir + "/FileTobeUpload/Order.xlsx");

        Response response = RestAssuredAPIUtility.postCallWithMultipartData(url,contentType,filePath);
        RestAssuredAPIUtility.logResponseForPostCallMultipart(ExtentTestManager.getTest(),filePath,response);
        System.out.println("Response body: " + response.getBody().asString());
    }
    @Test
    public void testGetOrderProcess() {
        String url = "/orderDetails/getForProcess";
        Map<String, String> queryParams = ApiPayload.getDetailsByFromToDate(currentDate);

        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams, response);

        // Call the helper method to get order details for process and update global variables
        Map<String, String> orderDetails = TestHelper.getOrderDetailsForProcess(response, orderRef);

        // Update global variables with order details
        orderId = orderDetails.get("orderId");
        orderType = orderDetails.get("orderType");
        orderCode = orderDetails.get("orderCode");
        // Check if the response contains orderReference orderReferenceNumber
//        if (responseBody.contains(orderRef)) {
//            // Parse the response JSON
//            JsonPath jsonPath = new JsonPath(responseBody);
//
//            // Retrieve the array of orderId and orderCode, etc values
//            List<Integer> orderIds = jsonPath.getList("data.order.orderId");
//            List<String> orderCodes = jsonPath.getList("data.order.orderCode");
//            List<String> orderTypes = jsonPath.getList("data.order.orderType");
//
//            // Retrieve the index of the orderReference "orderReferenceNumber"
//            int index = jsonPath.getList("data.order.orderReference").indexOf(orderRef);
//
//            // Check if the index is valid and retrieve the corresponding orderId and orderCode & etc
//            if (index >= 0 && index < orderIds.size()) {
//                orderId = String.valueOf(orderIds.get(index));
//                orderCode = orderCodes.get(index);
//                orderType = orderTypes.get(index);
//            }
//        }
    }

    @Test()
    public void testOrderProcess() {
        testGetOrderProcess();
        String targetUseCase = "testOrderProcess";

        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.orderProcessPayload(testData.getPayload(),orderId);

        Response response= api.postCall(testData.getUrl(), getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);

    }


    @Test()
    public void testGetWaveCreation() {
        String url = "/waveCreation/getForWaveFilter";
        Map<String, String> queryParams = ApiPayload.getDetailsByFromToDate(currentDate);

        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams, response);

    }


    @Test()
    public void testLoadWaveCreation() {
        testGetWaveCreation();
        String targetUseCase = "testLoadWaveCreation";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.orderLoadPayload(testData.getPayload(),orderId,currentDate);

        Response response= api.postCall(testData.getUrl(), getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);

        String responseBody =response.getBody().asString();

        // Check if the response contains orderReference orderReferenceNumber
        if (responseBody.contains(orderRef)) {
            // Parse the response JSON
            JsonPath jsonPath = new JsonPath(responseBody);

            // Retrieve the array of orderDetails
            List<Integer> detailsId = jsonPath.getList("data.orderDetails.orderDetailId");

            // Retrieve the index of the orderReference "orderReferenceNumber"
            int index = jsonPath.getList("data.orderDetails.orderReference").indexOf(orderRef);

            // Check if the index is valid and retrieve the corresponding orderDetailId
            if (index >= 0 && index < detailsId.size()) {
                orderDetailId = String.valueOf(detailsId.get(index));

                System.out.println("Order Details Id :" + orderDetailId);
            }
        }
    }

    @Test()
    public void testAddWaveCreation() {
        String targetUseCase = "testAddWaveCreation";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.orderAddPayload(testData.getPayload(),orderId,orderDetailId);

        Response response= api.postCall(testData.getUrl(), getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }

    @Test
    public void testWaveCreationGenerateWave() {
        String targetUseCase = "testGenerateWave";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.orderLoadPayload(testData.getPayload(),orderId,currentDate);

        Response response= api.postCall(testData.getUrl(), getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }

    int count =0;
    @Test()
    public void testGetWaveProcess() throws InterruptedException {
        count++;
        Thread.sleep(5000);
        String url = "/waveCreation/getPending";
        //get data of queryParam from ApiPayload
        Map<String, String> queryParams =ApiPayload.getDetailsByFromToDate(currentDate);

        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams,response);

        String results = response.jsonPath().getString("data.agtWaveTranDetails.result");
        String result = response.jsonPath().getString("data.agtWaveTranDetails.orderId");

        //retry to get put process details(api is getting longer time to get response)
        System.out.println(results + " " + count);

        if (!(result == null) || (!result.equals(orderId) || count == 14)) {
            return;
        }
        testGetWaveProcess();
    }

    @Test
    public void testGetPickDetails() throws InterruptedException {
        String url = "/assignPicker/getForRelease";
        //get data of queryParam from ApiPayload
        Thread.sleep(5000);
        Map<String, String> queryParams =ApiPayload.getDetailsByFromToDate(currentDate);

        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        String responseBody =response.getBody().asString();

        boolean foundOrderRef = TestHelper.findOrderReferenceInResponse(responseBody, orderRef);

        if (foundOrderRef) {
            List<Map<String, Object>> pickDetailsList = TestHelper.getPickDetailsList(responseBody);
            Map<String, Object> matchingPickDetail = TestHelper.findMatchingPickDetail(pickDetailsList, orderRef);

            if (matchingPickDetail != null) {
                extractPickDetails(matchingPickDetail);
            } else {
                System.out.println("Failed to find the matching pick detail.");
            }
        } else {
            System.out.println("Failed to find the order Reference number in the response.");
        }
    }

    private void extractPickDetails(Map<String, Object> pickDetail) {
        // Extract the necessary details from the putDetail map
        pickId = String.valueOf(pickDetail.get("pickId"));
        pickCode = String.valueOf(pickDetail.get("pickCode"));
        pickTransId = String.valueOf(pickDetail.get("pickTransId"));
        pickDetailsId = String.valueOf(pickDetail.get("pickDetailsId"));
        pickerId = String.valueOf(pickDetail.get("pickerId"));
        parentId = String.valueOf(pickDetail.get("parentId"));
        confQuantity = String.valueOf(pickDetail.get("qty"));
        liId = String.valueOf(pickDetail.get("liId"));
        pickQuantity = String.valueOf(pickDetail.get("qty"));
    }


    @Test()
    public void testWaveRelease() throws InterruptedException {
        count++;
        if (pickId ==null || count==5){
            testGetPickDetails();
        }
        String targetUseCase = "testWaveRelease";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.waveReleaseAndAssignPayload(testData.getPayload(),pickId,pickDetailsId,pickTransId,pickerId);

        Response response= api.postCall(testData.getUrl(), getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }

    @Test()
    public void testAssignPicker() {
        String targetUseCase = "testPickAssign";

        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.waveReleaseAndAssignPayload(testData.getPayload(),pickId,pickDetailsId,pickTransId,pickerId);

        Response response= api.postCall(testData.getUrl(), getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }

    @Test()
    public void testAssignPickConfirm() {
        String targetUseCase = "testPickConfirm";

        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.pickConfirmPayload(testData.getPayload(),pickId,pickDetailsId,pickTransId,
                userId,confQuantity,parentId,liId);
        Response response= api.postCall(testData.getUrl(), getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }

    @Test()
    public void testGetForSorting() {
        String url = "/sorting/getForSorting";
        //get data of queryParam from ApiPayload
        Map<String, String> queryParams =ApiPayload.getDetailsByFromToDate(currentDate);

        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams,response);

        String responseBody = response.getBody().asString();

        // Check if the response contains orderReference orderReferenceNumber
        if (responseBody.contains(orderRef)) {
            // Parse the response JSON
            JsonPath jsonPath = new JsonPath(responseBody);

            // Retrieve the array of kitId and kitCode, etc values
            List<Integer> kitIds = jsonPath.getList("data.sorting.kitId");
            List<String> kitCodes = jsonPath.getList("data.sorting.kitCode");
            List<String> kitDetailIds = jsonPath.getList("data.sorting.kitDetailId");
            List<String> sorterIds = jsonPath.getList("data.sorting.sorterId");

            // Retrieve the index of the orderReference "orderReferenceNumber"
            int index = jsonPath.getList("data.sorting.orderReference").indexOf(orderRef);

            // Check if the index is valid and retrieve the corresponding kitId and kitCode & etc
            if (index >= 0 && index < kitIds.size()) {
                kitId = String.valueOf(kitIds.get(index));
                kitCode = kitCodes.get(index);
                kitDetailId = kitDetailIds.get(index);
                sorterId = sorterIds.get(index);
            }
        }
    }

    @Test()
    public void testSorterAssign() {
        testGetForSorting();                       //calling method to get data
        String targetUseCase = "testSorterAssign";

        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.assignSorterPayload(testData.getPayload(),pickId,pickDetailsId,pickTransId,
                pickerId,confQuantity, parentId, liId);
        Response response= api.postCall(testData.getUrl(), getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);

    }

    @Test()
    public void testSorterConfirm() {
        String targetUseCase = "testSortConfirm";

        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.sortConfirmPayload(testData.getPayload(),pickId, pickDetailsId, pickTransId,
                 sorterId, confQuantity, kitId, liId, kitDetailId);

            Response response= api.postCall(testData.getUrl(), getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);

    }

    @Test
    public void testGetPackingByOrderId(){
        String url = "/pack/get-order/"+orderId+"/packing";        //245 is orderId
        Response response = RestAssuredAPIUtility.getCall(url, getHeaders());
        RestAssuredAPIUtility.logResponseForGetCall(ExtentTestManager.getTest(), response);
    }

    @Test
    public void testGetPackedByOrderId(){
        String url = "/pack/get-order/"+orderId+"/packed";        //245 is orderId
        Response response = RestAssuredAPIUtility.getCall(url, getHeaders());
        RestAssuredAPIUtility.logResponseForGetCall(ExtentTestManager.getTest(), response);
        String packingDate = response.jsonPath().getString("data.packingDate");
        String boxDetails = response.jsonPath().getString("data.boxDetails");
        System.out.println("Packing Date : "+packingDate);
        System.out.println("Packed Box Details : "+boxDetails);
    }

    @Test()
    public void testPrintShipLabel() {
        String targetUseCase = "testPrintShipLabel";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.printShipLabelPayload(testData.getPayload(),orderRef, pickDetailsId);
        Response response= api.postCall(testData.getUrl(), getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }
    @Test()
    public void testPackSaveBox() {
        String targetUseCase = "testPackSaveBox";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.saveBoxPayload(testData.getPayload(),itemCode, pickQuantity,orderId);
        Response response= api.postCall(testData.getUrl(), getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }

    @Test
    public void testPackComplete(){
        String urls= "/pack/complete/"+orderId+"";
        String targetUseCase ="testPackComplete";
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.packCompletePayload(testData.getPayload());
        Response response= api.postCall(urls, getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }

    @Test
    public void testGetShipment(){
        String url = "/shipment/getShipListing";
        Map<String, String> queryParams =ApiPayload.getDetailsByFromToDate(currentDate);
        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        shipmentId =response.jsonPath().getString("data.obShipment.shipmentId");
        shipmentCode =response.jsonPath().getString("data.obShipment.shipmentCode");
        String responseBody2 =response.jsonPath().getString("data.obShipment.orderReference");
    }
    @Test
    public void testShipmentComplete(){
        testGetShipment();
        String targetUseCase ="testShipmentComplete";
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.shipmentCompletePayload(testData.getPayload(),shipmentId);
        Response response= api.postCall(testData.getPayload(), getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }
    @Test
    public void testPrintAllRelevantData(){
        System.out.println("UserName : " +username);
        System.out.println("User Id : "+userId);
        System.out.println("User Display Name : "+displayName);
        System.out.println("Order ID: " + orderId);
        System.out.println("Order Code: " + orderCode);
        System.out.println("Order Type: " + orderType);
        System.out.println("Order Details Id :" + orderDetailId);
        System.out.println("Pick ID: " + pickId);
        System.out.println("Pick Code: " + pickCode);
        System.out.println("Pick Quantity: " + pickQuantity);
        System.out.println("Pick Trans ID: " + pickTransId);
        System.out.println("Pick Details ID: " + pickDetailsId);
        System.out.println("Picker ID: " + pickerId);
        System.out.println("Parent ID: " + parentId);
        System.out.println("Confirm Quantity: " + confQuantity);
        System.out.println("Li ID: " + liId);
        System.out.println("Kit ID: " + kitId);
        System.out.println("Kit Code: " + kitCode);
        System.out.println("Kit Details ID: " + kitDetailId);
        System.out.println("Sorter ID: " + sorterId);
        System.out.println("Shipment ID: " + shipmentId);
        System.out.println("Shipment Code: " + shipmentCode);

    }




}
