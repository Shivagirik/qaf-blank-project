package qaf.example.steps;

import static com.qmetry.qaf.automation.step.CommonStep.click;
import static com.qmetry.qaf.automation.step.CommonStep.sendKeys;
import static org.testng.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.testng.Reporter;

import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.util.*;

import qaf.example.util.LogReporter;
import qaf.example.util.PropertyLoader;
import qaf.example.yih.CreateItemInYIH;
import qaf.example.yih.CreateItemInYIH2;


public class StepsLibrary {
	

	static LogReporter logUtils = new LogReporter("");
	
	/**
	 * @param searchTerm
	 *            : search term to be searched
	 */
	@QAFTestStep(description = "search for {0}")
	public static void searchFor(String searchTerm) {
		sendKeys(searchTerm, "input.search");
		click("button.search");
	}
	
	@QAFTestStep(description = "Print message {0}")
	public static void test(String message) {
		
		//Reporter.log("This is testmessage");
		logUtils.info(message);
		com.qmetry.qaf.automation.util.Reporter.log(" This QAF demo msg display");
	
	}
	
	@QAFTestStep(description = "Test for rest test using util object")
	public static void getValueFromPropertFile() {
		
		logUtils.info("Printing value for property file...");
		
		//PropertyLoader pl= new PropertyLoader() ;
		//log.info(pl.getPropertyValue("application","env.baseurl"));
		
		CreateItemInYIH yih= new CreateItemInYIH();
		Object[] itemList= {"38010330174"};
		//Reporter.log("38010330174");
		//com.qmetry.qaf.automation.util.Reporter.logWithScreenShot("asd");
		
		yih.createItemInYIH(itemList);
	
	}
	
	@QAFTestStep(description = "Test for rest test using jayway.restassured.response")
	public static void testjayway() {
		
		logUtils.info("Printing value for property file...");
		
		
		//PropertyLoader pl= new PropertyLoader() ;
		//log.info(pl.getPropertyValue("application","env.baseurl"));
		Object[] itemList= {"16042516271"};
		CreateItemInYIH2 yih= new CreateItemInYIH2();
		
		//Reporter.log("16042516271");
		//com.qmetry.qaf.automation.util.Reporter.logWithScreenShot("asd");
		
		yih.createItemInYIH2(itemList);
	
	}
}
