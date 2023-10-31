package com.wmsTest_Mahindra;

import Utilities.*;
import com.wmsApiPayload.ApiPayload;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class wmsInboundFlow extends BaseClass {

    private RestAssuredAPIUtility api;
    private TestDataProvider dataProvider = new TestDataProvider();
    private String sheetName = "APITestCases";
    private Sheet sheet;
    private String pendingQty;
    private static String locationCode;
    public static String getLocationCode() {
        return locationCode;
    }
    public static void setLocationCode(String code) {
        locationCode = code;
    }
    Utility.RandomVehicleNumberGenerator generator = new Utility.RandomVehicleNumberGenerator();

    // Generate a random vehicle number
    String vehicleNumber = generator.generateRandomVehicleNumber();
    Random random = new Random();
    int invoiceNo = random.nextInt(90000000) + 10000000;
    int lpnNo =random.nextInt(9000) + 1000;
    int ibRefNo = random.nextInt(9000000) + 1000000;
    int uidNo = random.nextInt(900000) + 100000;
    String invoiceNumber = "INV" + invoiceNo;
    String ibRef = "IBR" + ibRefNo;
    String lpnNumber = "LPN" + lpnNo;
    String uidNumber = "UID" + uidNo;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String currentDate = dateFormat.format(new Date());
    // Declare the global variables here
    private String pgNumber;
    private String pgCode;
    private String ibPreGrnDetailsId;
    private String grnNumber;
    private String ibGrnCode;
    private String taskId;
    private String taskCode;
    private String grnDetailsId;
    private String grnConfirmationId;
    private String putId;
    private String putDetailsId;
    private String putTransId;
    private String confirmQty;
    private String ibParentId;
    private String IBliId;



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
    public void testCreatePreGrn() {
        String targetUseCase = "testPreGrnCreate";

        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.preGrnPayload(testData.getPayload(), invoiceNumber, currentDate, vehicleNumber);

        Response response= api.postCall(testData.getUrl(),getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);

        //   Add your code to process the response here
        pgCode = response.jsonPath().getString("data.pgCode");
        pgNumber = response.jsonPath().getString("data.ibpgNumber");
    }

    @Test
    public void testCreatePreGrnDetails() {
        String targetUseCase = "testPreGrnCreateDetails";

        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.preGrnDetailsPayload(testData.getPayload(),ibRef ,pgNumber, lpnNumber, qty, uomId, itemCode, currentDate);

        Response response= api.postCall(testData.getUrl(),getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }

    @Test
    public void testGrnCreation(){
        String targetUseCase = "testGrnCreate";

        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.grnCreationPayload(testData.getPayload(),currentDate, invoiceNumber, vehicleNumber);

        Response response = api.postCall(testData.getUrl(),getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
        //Extract GrnNumber and GrnCode
        grnNumber = response.jsonPath().getString("data.grnNumber");
        ibGrnCode = response.jsonPath().getString("data.grnCode");
    }

    @Test
    public void testGetPreGrnDetailsId(){
        String url = "/ibPreGrnDetails/getByIBPGNo";
        //get data of queryParam from ApiPayload
        Map<String, String> queryParams =ApiPayload.getIbpgNumberQueryParam(pgNumber);

        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams,response);

        String preGrnDetailsId = response.jsonPath().getString("data.ibPreGrnDetails.ibPreGrnDetailsId");
        ibPreGrnDetailsId = preGrnDetailsId.replaceAll("\\[|\\]", "");
    }
    @Test
    public void testGrnCreateDetails(){
        testGetPreGrnDetailsId();
        String targetUseCase = "testGrnCreateDetails";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.grnCreateDetailsPayload(testData.getPayload(), currentDate, grnNumber, ibPreGrnDetailsId, itemCode, ibRef, uomId, qty, lpnNumber);

        Response response= api.postCall(testData.getUrl(),getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }



    @Test
    public void testGrnCreateComplete(){
        String targetUseCase = "testGrnCreationComplete";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.grnCreationCompletePayload(testData.getPayload(), grnNumber);

        Response response= api.postCall(testData.getUrl(),getHeaders(), payload);
        // Logging response and other details
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }

    @Test
    public void testGetIbAssignDetails(){
        String url = "/ibTask/getForAssign";
        //get data of queryParam from ApiPayload
        Map<String, String> queryParams =ApiPayload.getDetailsByFromToDate(currentDate);

        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams,response);
        //fetching response
        String ibTaskId = response.jsonPath().getList("data.task.taskId").toString();
        String taskCodes = response.jsonPath().getString("data.task.taskCode");

        taskId = ibTaskId.replaceAll("\\[|\\]", "");
        taskCode = taskCodes.replaceAll("\\[|\\]", "");
    }
    @Test
    public void testIbAssign(){
        testGetIbAssignDetails();
        String targetUseCase = "testIbAssign";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.ibAssignPayload(testData.getPayload(), taskId,userId);

        Response response= api.postCall(testData.getUrl(),getHeaders(), payload);
        // Logging response and other details
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }

    @Test
    public void testGetGrnDetailsId() {
        String url = "/grnDetails/getForConf";
        Map<String, String> queryParams = ApiPayload.getGrnDetails(currentDate);

        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams, response);

        String responseBody = response.getBody().asString();
        int grnNumb = Integer.valueOf(grnNumber); // Assuming grnNumber is defined

        grnDetailsId = TestHelper.findGrnDetailsId(responseBody, grnNumb);

        if (grnDetailsId != null) {
            System.out.println("grnDetailsId for grnNumber " + grnNumb + ": " + grnDetailsId);
        } else {
            System.out.println("grnDetailsId not found for grnNumber " + grnNumb);
        }
    }

    @Test
    public void testUpdateGrnDetails(){
        testGetGrnDetailsId();
        String targetUseCase = "testUpdateGrnDetails";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.grnDetailsUpdate(testData.getPayload(), grnNumber,itemCode, qty,lpnNumber,
                uidNumber, ibRef, grnDetailsId, uomId, currentDate);

        Response response= api.postCall(testData.getUrl(),getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);

    }

    @Test
    public void testGrnConfirm(){
        String targetUseCase = "testGrnConfirm";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.grnConfirm(testData.getPayload(), grnNumber);

        Response response = api.postCall(testData.getUrl(),getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }

    @Test
    public void testGetGrnConfirmationId() {
        String url = "/grnConfirmation/getPendingByGrn";
        String[] grnNumbers = new String[]{grnNumber};
        Map<String, Object> queryParams = ApiPayload.getGrnDetailsConf(Arrays.toString(grnNumbers));

        Response response = RestAssuredAPIUtility.getCallWithArrayQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams, response);

        String responseBody = response.getBody().asString();
        if (responseBody.contains(grnNumber)) {
            List<Map<String, Object>> grnDetailsList = TestHelper.extractGrnDetailsList(responseBody);
            Map<String, Object> matchingGrnDetail = TestHelper.findMatchingGrnDetail(grnDetailsList, Integer.parseInt(grnNumber));
            if (matchingGrnDetail != null) {
                pendingQty = String.valueOf(matchingGrnDetail.get("pendingQty"));
                grnConfirmationId = String.valueOf(matchingGrnDetail.get("grnConfirmationId"));
                System.out.println(grnConfirmationId);
            }
        }
    }

    @Test
    public void testGeneratePut(){
//        testGetGrnConfirmationId();
        String targetUseCase = "testGeneratePut";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.generatePut(testData.getPayload(), grnNumber, grnDetailsId, grnConfirmationId,pendingQty);

        Response response = api.postCall(testData.getUrl(),getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }
    int count = 0;
    @Test()
    public void testGetPutProcessDetails() throws IOException, InterruptedException {

        count++;
        Thread.sleep(5000);
        String url = "/generatePutDetails/getPending";
        //get data of queryParam from ApiPayload
        Map<String, String> queryParams =ApiPayload.getDetailsByFromToDate(currentDate);


        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        RestAssuredAPIUtility.logRequestAndResponseForGetCall(ExtentTestManager.getTest(), queryParams,response);

        String results = response.jsonPath().getString("data.generatePutDetails.result");
        String result = response.jsonPath().getString("data.generatePutDetails.grnNumber");

        //retry to get put process details(api is getting longer time to get response)
        System.out.println(results + " " + count);

        // Retry to get put process details
        if (result == null || (!result.equals(grnNumber) || count == 14)) {
            return;
        }
        testGetPutProcessDetails();
    }

    @Test
    public void testGetAssignPutDetails() throws InterruptedException {

        String url = "/assignPutter/getForAssign";
        //get data of queryParam from ApiPayload
        Thread.sleep(10000);
        Map<String, String> queryParams =ApiPayload.getDetailsByFromToDate(currentDate);

        Response response = RestAssuredAPIUtility.getCallWithQueryParams(url, getHeaders(), queryParams);
        String responseBody =response.getBody().asString();

        boolean foundInvoice = TestHelper.findInvoiceInResponse(responseBody, invoiceNumber);

        if (foundInvoice) {
            List<Map<String, Object>> putDetailsList = TestHelper.getPutDetailsList(responseBody);
            Map<String, Object> matchingPutDetail = TestHelper.findMatchingPutDetail(putDetailsList, invoiceNumber);

            if (matchingPutDetail != null) {
                extractPutDetails(matchingPutDetail);
            } else {
                System.out.println("Failed to find the matching put detail.");
            }
        } else {
            System.out.println("Failed to find the invoice number in the response.");
        }
    }

    private void extractPutDetails(Map<String, Object> putDetail) {
        // Extract the necessary details from the putDetail map
        putId = putDetail.get("putId").toString();
        putDetailsId = String.valueOf(putDetail.get("putDetailsId"));
        putTransId = String.valueOf(putDetail.get("putTransId"));
        confirmQty = String.valueOf(putDetail.get("qty"));
        ibParentId = String.valueOf(putDetail.get("parentId"));
        IBliId = String.valueOf(putDetail.get("liId"));
        locationCode = String.valueOf(putDetail.get("locationCode"));
    }

    @Test
    public void testPutAssign() throws InterruptedException {
        System.out.println("Put id"+ putId);
        count++;
        if (putId == null && count == 5) {
            return;
        }
        testGetAssignPutDetails();
        String targetUseCase = "testPutAssign";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.putAssign(testData.getPayload(), putId , putDetailsId, putTransId, userId);

        Response response = api.postCall(testData.getUrl(),getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }
    @Test
    public void testPutConfirm(){
        String targetUseCase = "testPutConfirm";
        // Test code
        TestData testData = dataProvider.getTestData(targetUseCase);
        String payload = ApiPayload.putConfirm(testData.getPayload(), putId , putDetailsId, putTransId, userId,confirmQty, IBliId, lpnNumber);

        Response response = api.postCall(testData.getUrl(),getHeaders(), payload);
        RestAssuredAPIUtility.logRequestAndResponseForPostCall(ExtentTestManager.getTest(), payload, response);
    }

    @Test
    public void testMethodToGetAllData(){
        System.out.println("UserName : " +username);
        System.out.println("User Id : "+userId);
        System.out.println("User Display Name : "+displayName);
        System.out.println("LPN / Pallet Number :  "+lpnNo);
        System.out.println("Vehicle Number :  " +vehicleNumber);
        System.out.println("Invoice Number :  " +invoiceNumber);
        System.out.println("Inbound Pre Grn Number:  "+pgNumber);
        System.out.println("Pre Grn Code: " + pgCode);
        System.out.println("Pre GRN Details Id: " + ibPreGrnDetailsId);
        System.out.println("GRN number : " +grnNumber);
        System.out.println("Grn Code : " + ibGrnCode);
        System.out.println("grnDetailsId : " + grnDetailsId);
        System.out.println("grnConfirmationId Code : " + grnConfirmationId);
        System.out.println("task Id : " +taskId);
        System.out.println("Task Code : "+taskCode);
        System.out.println("Put Id: " + putId);
        System.out.println("Put Details Id: " + putDetailsId);
        System.out.println("Put Trans Id: " + putTransId);
        System.out.println("Confirm Quantity: " + confirmQty);
        System.out.println("Parent Id: " + ibParentId);
        System.out.println("Li ID: " + IBliId);
        System.out.println("UID Number: " + uidNumber);

    }

}
