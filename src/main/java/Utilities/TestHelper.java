package Utilities;

import io.restassured.path.json.JsonPath;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

public class TestHelper {


    ///////////////-----testGetAssignPutDetails   start-----/////////////////////
    public static boolean findInvoiceInResponse(String responseBody, String invoiceNumber) throws InterruptedException {
        int maxRetries = 5;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            if (responseBody.contains(invoiceNumber)) {
                return true;
            }

            retryCount++;
            System.out.println("Invoice number not found in the response. Retrying (" + retryCount + "/" + maxRetries + ")...");
            Thread.sleep(5000);
        }

        System.out.println("Failed to find the invoice number after " + maxRetries + " retries.");
        return false;
    }

    public static List<Map<String, Object>> getPutDetailsList(String responseBody) {
        JsonPath jsonPath = new JsonPath(responseBody);
        return jsonPath.getList("data.put");
    }

    public static Map<String, Object> findMatchingPutDetail(List<Map<String, Object>> putDetailsList, String invoiceNumber) {
        for (Map<String, Object> putDetail : putDetailsList) {
            String grnInvoiceNo = putDetail.get("invoiceNo").toString();
            if (invoiceNumber.equals(grnInvoiceNo)) {
                return putDetail;
            }
        }
        return null;
    }

    ///////////-----------testGetAssignPutDetails end -----//////////
    ////////// -------testGetGrnDetailsId  start----------//////////////
    public static String findGrnDetailsId(String responseBody, int grnNumber) {
        JSONObject responseJson = new JSONObject(responseBody);
        JSONArray grnArray = responseJson.getJSONObject("data").getJSONArray("grn");

        for (int i = 0; i < grnArray.length(); i++) {
            JSONObject grn = grnArray.getJSONObject(i);
            int grnNum = grn.getInt("grnNumber");

            if (grnNum == grnNumber) {
                JSONArray grnDetailsArray = grn.getJSONArray("grnDetails");

                for (int j = 0; j < grnDetailsArray.length(); j++) {
                    JSONObject grnDetail = grnDetailsArray.getJSONObject(j);
                    return String.valueOf(grnDetail.getInt("grnDetailsId"));
                }
            }
        }
        return null;
    }

    //////////---------testGetGrnDetailsId end-----------///////////

    //////////////-----------testGetGrnConfirmationId  start----------//////////////
    public static List<Map<String, Object>> extractGrnDetailsList(String responseBody) {
        JsonPath jsonPath = new JsonPath(responseBody);
        return jsonPath.getList("data.grnDetails");
    }

    public static Map<String, Object> findMatchingGrnDetail(List<Map<String, Object>> grnDetailsList, int grnNumberValue) {
        for (Map<String, Object> grnDetail : grnDetailsList) {
            int grnNumberFromResponse = Integer.parseInt(grnDetail.get("grnNumber").toString());

            if (grnNumberFromResponse == grnNumberValue) {
                return grnDetail;
            }
        }
        return null;
    }
    ////////////----testGetGrnConfirmationId    end-------///// -- Inbound flow end--//////

    //////////////////////////////////--OutBound flow start--////////////////////////////////////////
    // /////////-----  testGetPickDetails  start------///////////

    public static boolean findOrderReferenceInResponse(String responseBody, String orderRef) throws InterruptedException {
        int maxRetries = 5;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            if (responseBody.contains(orderRef)) {
                return true;
            }

            retryCount++;
            System.out.println("orderReference number not found in the response. Retrying (" + retryCount + "/" + maxRetries + ")...");
            Thread.sleep(5000);
        }

        System.out.println("Failed to find the orderReference number after " + maxRetries + " retries.");
        return false;
    }

    public static List<Map<String, Object>> getPickDetailsList(String responseBody) {
        JsonPath jsonPath = new JsonPath(responseBody);
        return jsonPath.getList("data.pick");
    }

    public static Map<String, Object> findMatchingPickDetail(List<Map<String, Object>> pickDetailsList, String orderRef) {
        for (Map<String, Object> pickDetail : pickDetailsList) {
            String orderReference = pickDetail.get("orderReference").toString();
            if (orderReference.equals(orderRef)) {
                return pickDetail;
            }
        }
        return null;
    }
    // /////////-----  testGetPickDetails  end------///////////

       /////-----testGetRelocationDetails Start-----//////////

    public static List<Map<String, Object>> extractItemDetailsList(String responseBody) {
        JsonPath jsonPath = new JsonPath(responseBody);
        return jsonPath.getList("data.relocationDetails");
    }

    public static Map<String, Object> findMatchingItemDetail(List<Map<String, Object>> itemDetailsList, int itemCodeValue) {
        for (Map<String, Object> itemDetail : itemDetailsList) {
            int itemCodeFromResponse = Integer.parseInt(itemDetail.get("locationCode").toString());

            if (itemCodeFromResponse == itemCodeValue) {
                return itemDetail;
            }
        }
        return null;
    }
    /////-----testGetRelocationDetails end-----//////////

    /////-----getForRelocationDetails start (mid bound) ----////////////
    public static void processApiResponse(Response response, String fromLocationCode) {
        try {
            if (response.getStatusCode() == 200) {
                String responseBody = response.getBody().asString();
                JsonPath jsonPath = new JsonPath(responseBody);

                List<Integer> liIds = jsonPath.getList("data.relocationDetails.liId");
                List<String> lpnNos = jsonPath.getList("data.relocationDetails.lpnNo");
                List<String> uidNos = jsonPath.getList("data.relocationDetails.uidNo");
                List<String> locationCodes = jsonPath.getList("data.relocationDetails.locationCode");

                // Find the index of fromLocationCode
                int index = locationCodes.indexOf(fromLocationCode);

                if (index >= 0 && index < liIds.size()) {
                    String liId = String.valueOf(liIds.get(index));
                    String lpnNo = lpnNos.get(index);
                    String uidNo = uidNos.get(index);

                    System.out.println("liID: " + liId);
                    System.out.println("lpnNo: " + lpnNo);
                    System.out.println("uidNo: " + uidNo);
                } else {
                    System.out.println("fromLocationCode not found in response.");
                }
            } else {
                System.out.println("HTTP request failed with status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // get for relocation details End

    // get for relocation assign start
    public static void getRelocationDetailsByRelocationId(Response response,String relocationId) {
       try {
            if (response.getStatusCode() == 200) {
                String responseBody = response.getBody().asString();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode relocationDetails = rootNode.get("data").get("relocationDetails");

                for (JsonNode relocationDetail : relocationDetails) {
                    int relocationIds = relocationDetail.get("relocationId").asInt();

                    if (String.valueOf(relocationIds).equals(relocationId)) {
                        // Extract and print details for the desired relocationId
                        String relocationCode = relocationDetail.get("relocationCode").asText();
                        String tranId = String.valueOf(relocationDetail.get("tranId").asInt());
                        String liId = String.valueOf(relocationDetail.get("liId").asInt());
                        System.out.println("TranId: " + tranId);
                        System.out.println("li Id: " + liId);
                    }
                }
            } else {
                System.out.println("HTTP request failed with status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get for relocation assign end

    // test getOrderProcss details ----outbound ////

    public static Map<String, String> getOrderDetailsForProcess(Response response, String orderRef) {
        Map<String, String> orderDetails = new HashMap<>();

        // Check if the response contains orderReference "orderReferenceNumber"
        String responseBody = response.getBody().asString();
        if (responseBody.contains(orderRef)) {
            // Parse the response JSON
            JsonPath jsonPath = new JsonPath(responseBody);

            // Retrieve the array of orderId, orderCode, etc. values
            List<Integer> orderIds = jsonPath.getList("data.order.orderId");
            List<String> orderCodes = jsonPath.getList("data.order.orderCode");
            List<String> orderTypes = jsonPath.getList("data.order.orderType");

            // Retrieve the index of the orderReference "orderReferenceNumber"
            int index = jsonPath.getList("data.order.orderReference").indexOf(orderRef);

            // Check if the index is valid and update the corresponding orderId, orderCode, etc.
            if (index >= 0 && index < orderIds.size()) {
                orderDetails.put("orderId", String.valueOf(orderIds.get(index)));
                orderDetails.put("orderCode", orderCodes.get(index));
                orderDetails.put("orderType", orderTypes.get(index));
            }
        }
        return orderDetails;
    }

    // test getOrderProcss details  end ----outbound

}
