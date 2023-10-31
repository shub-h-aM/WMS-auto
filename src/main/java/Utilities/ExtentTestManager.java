package Utilities;

import java.util.HashMap;
import java.util.Map;
import com.relevantcodes.extentreports.*;
import static Utilities.BaseClass.*;


public class ExtentTestManager {
    static Map extentTestMap = new HashMap();
    static ExtentReports extent = ExtentManager.getReporter();

    public static synchronized ExtentTest getTest() {
        return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
    }

    public static synchronized void endTest() {
        extent.endTest((ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId())));
    }

    public static synchronized ExtentTest startTest(String testName) {
        return startTest(testName, "");
    }

    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = extent.startTest(testName, desc);
        test.assignAuthor("WMS Team User Name : "+username +" || User Id: "+userId);
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);

        return test;
    }
}
