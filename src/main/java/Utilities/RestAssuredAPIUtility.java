package Utilities;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.io.File;
import java.util.Map;

import static Utilities.BaseClass.authToken;
import static Utilities.BaseClass.baseURL;

public class RestAssuredAPIUtility {
    public static Response postCall(String url, Map<String, String> headers, String payload) {
        String fullURL= baseURL +url;
        ValidatableResponse validatableResponse = RestAssured.given()
                .headers(headers)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post(fullURL)
                .then()
                ;

        validatableResponse.log().all(); // Log response details
        return validatableResponse.extract().response();
    }
    public static Response getCall(String url, Map<String, String> headers) {
        String fullURL= baseURL +url;
        return RestAssured.given()
                .headers(headers)
                .when()
                .get(fullURL)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .prettyPeek();
    }
    public static Response postCallWithoutBody(String url, Map<String, String> headers) {
        String fullURL= baseURL +url;
        return RestAssured.given()
                .headers(headers)
                .contentType(ContentType.JSON)
                .when()
                .post(fullURL)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .prettyPeek();
    }

    public static Response postCallWithQueryParams(String url, Map<String, String> headers, Map<String, String> queryParams) {
        String fullURL= baseURL +url;
        return RestAssured.given()
                .headers(headers)
                .queryParams(queryParams)
                .contentType(ContentType.JSON)
                .when()
                .post(fullURL)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .prettyPeek();
    }

    public static Response getCallWithQueryParams(String url, Map<String, String> headers, Map<String, String> queryParams) {
        String fullURL= baseURL +url;
        return RestAssured.given()
                .headers(headers)
                .queryParams(queryParams) // Add query parameters
                .when()
                .get(fullURL)
                .then()
                .extract()
                .response()
                .prettyPeek();
    }

    public static Response postCallRequestForText(String url, String relocationId) {
        String fullURL= baseURL +url;
        return RestAssured.given()
                .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .header("Authorization","Bearer "+authToken)
                .formParam("relocationId", relocationId)
                .when()
                .post(fullURL)
                .prettyPeek();
    }
    public static Response postCallWithMultipartData(String url, String contentType, File filePath) {
        String fullURL= baseURL +url;
        return RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .multiPart("file", filePath, contentType)
                .when()
                .post(fullURL)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .prettyPeek();
    }
    public static Response getCallWithArrayQueryParams(String url, Map<String, String> headers, Map<String, Object> queryParams) {
        String fullURL= baseURL +url;
        return RestAssured.given()
                .headers(headers)
                .queryParams(queryParams) // Add query parameters
                .when()
                .get(fullURL)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .prettyPeek();
    }
    public static Response putCall(String url, Map<String, String> headers, String payload) {
        String fullURL= baseURL +url;
        Response response = RestAssured.given()
                .headers(headers)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put(fullURL)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .prettyPeek();

        return response;
    }

    public static Response patchCall(String url, Map<String, String> headers, String payload) {
        String fullURL= baseURL +url;
        Response response = RestAssured.given()
                .headers(headers)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .patch(fullURL)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .prettyPeek();

        return response;
    }

    public static Response deleteCall(String url, Map<String, String> headers) {
        String fullURL= baseURL +url;
        Response response = RestAssured.given()
                .headers(headers)
                .when()
                .delete(fullURL)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .prettyPeek();

        return response;
    }
    public static void logRequestAndResponseForPostCall(ExtentTest extentTest, String payload, Response response) {
        extentTest.log(LogStatus.INFO, "Request Body: " + payload);
        extentTest.log(LogStatus.INFO, "Response Status Code: " + response.statusCode());
        extentTest.log(LogStatus.INFO, "Response Headers: " + response.getHeaders());
        extentTest.log(LogStatus.INFO, "Response Body: " + response.getBody().asString());
    }
    public static void logResponseForPostCallWithoutBody(ExtentTest extentTest, Response response) {
        extentTest.log(LogStatus.INFO, "Response Headers: " + response.getHeaders());
        extentTest.log(LogStatus.INFO, "Response Body: " + response.getBody().asString());
    }
    public static void logResponseForPostCallMultipart(ExtentTest extentTest,File filePath ,Response response) {
        extentTest.log(LogStatus.INFO, "Response filePath: " + filePath);
        extentTest.log(LogStatus.INFO, "Response Body: " + response.getBody().asString());
    }
    public static void logResponseForPostCallRequestForText(ExtentTest extentTest, Response response) {
        extentTest.log(LogStatus.INFO, "Response Headers: " + response.getHeaders());
        extentTest.log(LogStatus.INFO, "Response Body: " + response.getBody().asString());
    }
    public static void logResponseForGetCall(ExtentTest extentTest, Response response) {
        extentTest.log(LogStatus.INFO, "Response Headers: " + response.getHeaders());
        extentTest.log(LogStatus.INFO, "Response Body: " + response.getBody().asString());
    }
    public static void logRequestAndResponseForGetCall(ExtentTest extentTest, Object queryParams, Response response) {
        extentTest.log(LogStatus.INFO, "Request Body: " + queryParams);
        extentTest.log(LogStatus.INFO, "Response Headers: " + response.getHeaders());
        extentTest.log(LogStatus.INFO, "Response Body: " + response.getBody().asString());
    }
    public static void logRequestAndResponseForPutCall(ExtentTest extentTest, String payload, Response response) {
        extentTest.log(LogStatus.INFO, "Request Body: " + payload);
        extentTest.log(LogStatus.INFO, "Response Status Code: " + response.statusCode());
        extentTest.log(LogStatus.INFO, "Response Headers: " + response.getHeaders());
        extentTest.log(LogStatus.INFO, "Response Body: " + response.getBody().asString());
    }
    public static void logRequestAndResponseForPatchCall(ExtentTest extentTest, String payload, Response response) {
        extentTest.log(LogStatus.INFO, "Request Body: " + payload);
        extentTest.log(LogStatus.INFO, "Response Status Code: " + response.statusCode());
        extentTest.log(LogStatus.INFO, "Response Headers: " + response.getHeaders());
        extentTest.log(LogStatus.INFO, "Response Body: " + response.getBody().asString());
    }
    public static void logRequestAndResponseForDeleteCall(ExtentTest extentTest, String payload, Response response) {
        extentTest.log(LogStatus.INFO, "Request Body: " + payload);
        extentTest.log(LogStatus.INFO, "Response Status Code: " + response.statusCode());
        extentTest.log(LogStatus.INFO, "Response Headers: " + response.getHeaders());
        extentTest.log(LogStatus.INFO, "Response Body: " + response.getBody().asString());
    }

}

