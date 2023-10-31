package Utilities;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static io.restassured.RestAssured.baseURI;

public class BaseClass {
    public static String dir = System.getProperty("user.dir");
    public static String downloadFilepath = dir + "/temp";
    protected static Utility util = null;

    public static String sheetName = null;
    public static String fileName = null;
    public static String authToken = null;
    public static String warehouseId = null;
    public static String itemCode = null;
    public static String uomId = null;
    public static String ruleCode = null;
    public static String qty = null;
    public static String serialNumber = "";
    public static String orderRef = "";
    public static String userId = "";
    public static String toLocationCode = "";
    public static String fromLocationCode = "";
    public static String username = "";
    public static String displayName = "";
    public static String relocateQty = "";
    public static String orderQty = "";


    public static String baseURL = null;

    /* WMS application properties */
//    public static Map<String, String> wmsHeaders = null;


    @BeforeMethod
    public void beforeMethod(Method method) {
        ExtentTestManager.startTest(method.getName());


    }

    @AfterMethod
    public void tearDownAfterEveryMethod(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            ExtentTestManager.getTest().log(LogStatus.SKIP, "Test skipped " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            ExtentTestManager.getTest().log(LogStatus.PASS, "Test passed");
        }
        ExtentManager.getReporter().endTest(ExtentTestManager.getTest());
        ExtentManager.getReporter().flush();
    }

    public static void initialiseProperties_Wms() throws Exception {
        util = new Utility();
        util.setPropertyFile(dir + "/src/main/resources/WMS.properties");
        baseURL = util.gettingValueOfProperty("WmsBaseUrl");
        authToken = util.gettingValueOfProperty("AuthorizationToken");
        warehouseId = util.gettingValueOfProperty("WarehouseId");
        itemCode = util.gettingValueOfProperty("itemCode");
        uomId = util.gettingValueOfProperty("uomId");
        qty = util.gettingValueOfProperty("Quantity");
        serialNumber = util.gettingValueOfProperty("SerialNumber");
        orderRef = util.gettingValueOfProperty("orderReferenceNumber");
        ruleCode = util.gettingValueOfProperty("RuleCode");
        toLocationCode = util.gettingValueOfProperty("toLocationCode");
        fromLocationCode = util.gettingValueOfProperty("fromLocationCode");
        relocateQty = util.gettingValueOfProperty("quantityToBeRelocated");
        orderQty = util.gettingValueOfProperty("orderQuantity");
    }


    protected Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + authToken);
        headers.put("Content-Type", "application/json");
//        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        headers.put("Accept", "application/json, text/plain, */*");
        return headers;
    }
    protected Map<String, String> getHeadersForTest() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + authToken);
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        headers.put("Accept", "application/json, text/plain, */*");
        return headers;
    }

    public void testGetWarehouse(){
        String url = "/user/warehouse/";
        baseURI=baseURL;
        Response response = RestAssuredAPIUtility.getCall(url, getHeaders());
        String responseBody = response.getBody().asString();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Navigate to the "warehouseId" array
            JsonNode warehouseArray = rootNode.path("data").path("warehouse");
            for (JsonNode warehouse : warehouseArray) {
                String warehouseIds = String.valueOf(warehouse.path("warehouseId"));
                if (warehouseIds.equals(warehouseId)) {
                    userId = String.valueOf(warehouse.path("userId"));
                    username = warehouse.path("username").asText();
                    displayName = warehouse.path("displayName").asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
