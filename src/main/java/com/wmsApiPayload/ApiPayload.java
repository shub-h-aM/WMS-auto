package com.wmsApiPayload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiPayload {
    public static String selectWarehousePayload(String payload, String warehouseId) {
        // Prepare the payload with necessary replacements
        payload = payload.replace("{warehouseId}", warehouseId);
        return payload;
    }

    public static String preGrnPayload(String payload, String invoiceNumber, String currentDate, String vehicleNumber) {
        // Prepare the payload with necessary replacements
        payload = payload.replace("{invoiceNo}", invoiceNumber);
        payload = payload.replace("{invoiceDate}", currentDate);
        payload = payload.replace("{vehicleNo}", vehicleNumber);
        payload = payload.replace("{ibpgDate}", currentDate);
        return payload;
    }

    public static String preGrnDetailsPayload(String payload, String ibRef, String pgNumber, String lpnNumber, String qty,
                                              String uomId, String itemCode, String currentDate) {
        // Prepare the payload with necessary replacements
        payload = payload.replace("{ibRef}", ibRef);
        payload = payload.replace("{ibpgNumber}", pgNumber);
        payload = payload.replace("{lpnNo}", lpnNumber);
        payload = payload.replace("{ibQty}", qty);
        payload = payload.replace("{uomId}", uomId);
        payload = payload.replace("{itemCode}", itemCode);
        payload = payload.replace("{ibDate}", currentDate);
        return payload;
    }

    public static String grnCreationPayload(String payload, String currentDate, String invoiceNumber, String vehicleNumber) {
        payload = payload.replace("{grnDate}", currentDate);
        payload = payload.replace("{invoiceNo}", invoiceNumber);
        payload = payload.replace("{invoiceDate}", currentDate);
        payload = payload.replace("{vehicleNo}", vehicleNumber);
        return payload;
    }

    public static String grnCreateDetailsPayload(String payload, String currentDate, String grnNumber, String ibPreGrnDetailsId,
                                                 String itemCode, String ibRef, String uomId, String qty, String lpnNumber) {
        payload = payload.replace("{grnNumber}", grnNumber);
        payload = payload.replace("{ibPreGrnDetailsId}", ibPreGrnDetailsId);
        payload = payload.replace("{ibRef}", ibRef);
        payload = payload.replace("{ibDate}", currentDate);
        payload = payload.replace("{itemCode}", itemCode);
        payload = payload.replace("{uomId}", uomId);
        payload = payload.replace("{grnQty}", qty);
        payload = payload.replace("{lpnNo}", lpnNumber);
        return payload;
    }

    public static String grnCreationCompletePayload(String payload, String grnNumber) {
        payload = payload.replace("{grnNumber}", grnNumber);
        return payload;
    }

    //this below method is used for query param
    public static Map<String, String> getIbpgNumberQueryParam(String pgNumber) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("ibpgNumber", pgNumber);
        return queryParams;
    }

    public static Map<String, String> getDetailsByFromToDate(String currentDate) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("fromDate", currentDate);
        queryParams.put("toDate", currentDate);
        return queryParams;
    }
    public static Map<String, String> getCheckRule(String ruleName) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("ruleCode", ruleName);
        return queryParams;
    }
    public static Map<String, String> warehouseActiveRule(String ruleStatus) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("active", ruleStatus);
        return queryParams;
    }

    public static String ibAssignPayload(String payload, String taskId, String userId) {
        payload = payload.replace("{taskId}", taskId);
        payload = payload.replace("{assignedTo}", userId);
        return payload;
    }

    public static Map<String, String> getGrnDetails(String currentDate) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("grnDateFrom", currentDate);
        queryParams.put("grnDateTo", currentDate);
        return queryParams;
    }
    public static String grnDetailsUpdate(String payload, String grnNumber, String itemCode, String qty,String lpnNumber,
                                          String  uidNumber, String ibRef, String grnDetailsId, String uomId,String currentDate) {
        payload = payload.replace("{grnNumber}", grnNumber);
        payload = payload.replace("{itemCode}", itemCode);
        payload = payload.replace("{grnQty}", qty);
        payload = payload.replace("{confirmQty}", qty);
        payload = payload.replace("{lpnNo}", lpnNumber);
        payload = payload.replace("{uidNo}", uidNumber);
        payload = payload.replace("{ibRef}", ibRef);
        payload = payload.replace("{grnDetailsId}", grnDetailsId);
        payload = payload.replace("{ibCode}", ibRef);
        payload = payload.replace("{uomId}", uomId);
        payload = payload.replace("{ibDate}", currentDate);
        return payload;
    }
    public static String grnConfirm(String payload, String grnNumber) {
        payload = payload.replace("{grnNumber}", grnNumber);
        return payload;
    }

    public static Map<String, Object> getGrnDetailsConf(String grnNumber) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("grnNumber", grnNumber);
        queryParams.put("itemStatusId", "0");
//        queryParams.put("grnNumber", (Object) new int[]{Integer.parseInt(grnNumber)});
//        queryParam.put("itemStatusId", 0);
        return queryParams;
    }

    public static String generatePut(String payload, String grnNumber, String grnDetailsId, String grnConfirmationId,String pendingQty) {
        payload = payload.replace("{grnNumber}", grnNumber);
        payload = payload.replace("{grnDetailsId}", grnDetailsId);
        payload = payload.replace("{grnConfirmationId}", grnConfirmationId);
        payload = payload.replace("{pendingQty}", pendingQty);
        return payload;
    }



    public static String putAssign(String payload, String putId, String putDetailsId,
                                   String putTransId, String userId) {
        payload = payload.replace("{putId}", putId);
        payload = payload.replace("{putDetailsId}", putDetailsId);
        payload = payload.replace("{putTransId}", putTransId);
        payload = payload.replace("{putterId}", userId);
        return payload;
    }

    public static String putConfirm(String payload, String putId, String putDetailsId,
                                    String putTransId, String userId, String confirmQty, String IBliId, String lpnNumber) {
        payload = payload.replace("{putId}", putId);
        payload = payload.replace("{putDetailsId}", putDetailsId);
        payload = payload.replace("{putTransId}", putTransId);
        payload = payload.replace("{putterId}", userId);
        payload = payload.replace("{confirmQty}", confirmQty);
        payload = payload.replace("{liId}", IBliId);
        payload = payload.replace("{lpnNo}", lpnNumber);
        return payload;
    }

    public static String orderProcessPayload(String payload, String orderId){
        payload = payload.replace("{orderId}", orderId);
        return payload;
    }
    public static String orderLoadPayload(String payload, String orderId,String currentDate){
        payload = payload.replace("{orderId}", orderId);
        payload = payload.replace("{scheduledArrivalDate}", currentDate);
        payload = payload.replace("{fromDate}", currentDate);
        payload = payload.replace("{toDate}", currentDate);
        return payload;
    }
    public static String orderAddPayload(String payload, String orderId,String orderDetailId){
        payload = payload.replace("{orderId}", orderId);
        payload = payload.replace("{orderDetailId}", orderDetailId);
        return payload;
    }
    public static String waveReleaseAndAssignPayload(String payload, String pickId,String pickDetailsId,String pickTransId,
                                            String pickerId){
        payload = payload.replace("{pickId}", pickId);
        payload = payload.replace("{pickDetailsId}", pickDetailsId);
        payload = payload.replace("{pickTransId}", pickTransId);
        payload = payload.replace("{pickerId}", pickerId);
        return payload;
    }
    public static String pickConfirmPayload(String payload, String pickId,String pickDetailsId,String pickTransId,
                                            String userId,String confQuantity,String parentId,String liId){
        payload = payload.replace("{pickId}", pickId);
        payload = payload.replace("{pickDetailsId}", pickDetailsId);
        payload = payload.replace("{pickTransId}", pickTransId);
        payload = payload.replace("{pickerId}", userId);
        payload = payload.replace("{confirmQty}", confQuantity);
        payload = payload.replace("{parentId}", parentId);
        payload = payload.replace("{liId}", liId);
        return payload;
    }
    public static String assignSorterPayload(String payload, String pickId,String pickDetailsId,String pickTransId,
                                            String pickerId,String confQuantity,String parentId,String liId){
        payload = payload.replace("{pickId}", pickId);
        payload = payload.replace("{pickDetailsId}", pickDetailsId);
        payload = payload.replace("{pickTransId}", pickTransId);
        payload = payload.replace("{sorterId}", pickerId);
        payload = payload.replace("{confirmQty}", confQuantity);
        payload = payload.replace("{kitDetailId}", parentId);
        payload = payload.replace("{liId}", liId);
        return payload;
    }
    public static String sortConfirmPayload(String payload, String pickId,String pickDetailsId,String pickTransId,
                                            String sorterId,String confQuantity,String kitId,String liId,String kitDetailId){
        payload = payload.replace("{pickId}", pickId);
        payload = payload.replace("{pickDetailsId}", pickDetailsId);
        payload = payload.replace("{pickTransId}", pickTransId);
        payload = payload.replace("{sorterId}", sorterId);
        payload = payload.replace("{confirmQty}", confQuantity);
        payload = payload.replace("{kitId}", kitId);
        payload = payload.replace("{kitDetailId}", kitDetailId);
        payload = payload.replace("{liId}", liId);
        return payload;
    }
    public static String printShipLabelPayload(String payload, String orderRef,String pickDetailsId){
        payload = payload.replace("{orderReference}", orderRef);
        payload = payload.replace("{pickDetailsId}", pickDetailsId);
        return payload;
    }
    public static Map<String, String> getRelocationDetailsOfItem(String itemCode) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("itemCode", itemCode);
        return queryParams;
    }

        public static String relocationDetailsPayload(String payload, String relocationId,String liId,String relocateQty,String toLocationCode,String itemCode){
        payload = payload.replace("{relocationId}", relocationId);
        payload = payload.replace("{liId}", liId);
        payload = payload.replace("{qty}", relocateQty);
        payload = payload.replace("{toLocationCode}", toLocationCode);
        payload = payload.replace("{itemCode}", itemCode);
        return payload;
    }
    public static String relocationCompPayload(String payload, String relocationId){
        payload = payload.replace("{relocationId}", relocationId);
        return payload;
    }
    public static String relocationAssignPayload(String payload, String tranId,String userId){
        payload = payload.replace("{tranId}", tranId);
        payload = payload.replace("{adjusterId}", userId);
        return payload;
    }
    public static String assignConfirmPayload(String payload, String relocationId,String tranId,String liId,String relocateQty,String toLocationCode){
        payload = payload.replace("{relocationId}", relocationId);
        payload = payload.replace("{tranId}", tranId);
        payload = payload.replace("{liId}", liId);
        payload = payload.replace("{qty}", relocateQty);
        payload = payload.replace("{toLocationCode}", toLocationCode);
        return payload;
    }
    public static String saveBoxPayload(String payload, String itemCode,String pickQuantity,String orderId){
        payload = payload.replace("{itemCode}", itemCode);
        payload = payload.replace("{packedQuantity}", pickQuantity);
        payload = payload.replace("{orderId}", orderId);
        return payload;
    }
    public static String packCompletePayload(String payload){
        return payload;
    }
    public static String shipmentCompletePayload(String payload, String shipmentId){
        payload = payload.replace("{shipmentId}", shipmentId);
        return payload;
    }
    public static String ruleUpdatePayload(String payload, String warehouseId,String ruleIdList){
        payload = payload.replace("{warehouseId}", warehouseId);
        payload = payload.replace("{ruleId}", ruleIdList);
        return payload;
    }
}
