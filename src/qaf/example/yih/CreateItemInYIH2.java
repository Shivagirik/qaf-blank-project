package qaf.example.yih;

import qaf.example.util.LogReporter;
import qaf.example.util.PropertyLoader;
import org.json.JSONArray;
import org.json.JSONObject;


import com.jayway.restassured.response.Response;
import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;


public class CreateItemInYIH2 {
	LogReporter logUtils = new LogReporter(getClass().getName());
	
	static String protocol = "https";
	static String host;
	static {		
		PropertyLoader pl= new PropertyLoader() ;		
		host = pl.getPropertyValue("application","env.yihbaselongurl");
	}
	String port = "";
	String urlFile = "/master/itemDetails/JCP/";
	String apiUrl="";
	
	public void createItemInYIH2(Object[] itemList){
		String createItemRequest = formInputToCreateItem(itemList);
		callYIHCreateItem(createItemRequest);
		
	}

	private void callYIHCreateItem(String createItemRequest) {

		
		if (port!="") {
			apiUrl = protocol + "://" + host + port + urlFile;
		} else {
			apiUrl = protocol + "://" + host + urlFile;
		}					
		Response resp=null;
		try
	    {
			logUtils.info("apiUrl -->"+ apiUrl);
			logUtils.info("apiUrl HTTP Body:" + createItemRequest);
			resp=given().
						contentType("application/json").
						accept("application/json").
						body(createItemRequest).
				 when().
						post(apiUrl);
	    }
		catch( Exception e )
	    {
			//logUtils.info(resp.asString());
			logUtils.error("Exception message: '{}'"+e.toString());
	    }
		logUtils.info("apiUrl response-->:");
		logUtils.info(resp.asString());
		int status;
		Object key;
		status=resp.then().contentType("application/json").extract().path("status");
		key=resp.then().contentType("application/json").extract().path("body.key");
		if(status==221){
			logUtils.info("Succefully created Item withe Key :" +key);
		}
	}

	private String formInputToCreateItem(Object[] itemList) {
		JSONArray createItem = new JSONArray();
		
		for(int i = 0; i<itemList.length;i++){
			JSONObject item = new JSONObject();
			item.put("productLine", ((String)itemList[i]).substring(0, 3));
			item.put("itemID", ((String)itemList[i]).replaceAll("-", ""));
			item.put("gtin", "");
			item.put("uom", "EACH");
			item.put("shippable", "true");
			item.put("bopus", "true");
			item.put("lotNumber", ((String)itemList[i]).substring(0, 7));
			item.put("isSubstitutionAllowed", "N");
			item.put("purchaseType", "Fashion");
			item.put("itemProcessingTime", "10");
			createItem.put(item);
		}
		return createItem.toString();
		
	}

}
