package qaf.example.yih;

import qaf.example.util.PropertyLoader;
import qaf.example.util.Utils;

import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.qmetry.qaf.automation.util.Reporter;

public class CreateItemInYIH {
	Logger log = Logger.getLogger(Logger.class.getName());
	
	static String protocol = "https";
	static String host;
	static {		
		PropertyLoader pl= new PropertyLoader() ;		
		host = pl.getPropertyValue("application","env.yihbaselongurl");
	}
	String port = "";
	String urlFile = "/master/itemDetails/JCP/";
	String apiUrl="";
	
	public void createItemInYIH(Object[] itemList){
		String createItemRequest = formInputToCreateItem(itemList);
		callYIHCreateItem(createItemRequest);
		
	}

	private void callYIHCreateItem(String createItemRequest) {
		Utils utl=new Utils();
		
		
		if (port!="") {
			apiUrl = protocol + "://" + host + port + urlFile;
		} else {
			apiUrl = protocol + "://" + host + urlFile;
		}			
		Reporter.log("createItemYIH()::HTTP Body for Item create is:" + createItemRequest);
		String output = utl.callYantriksAPIStoreFilter(apiUrl, "POST", createItemRequest);
		String key = utl.getJsonAttribute(output, "\"key\":");
		Reporter.log("createItemYIH()::Item is created successfully with KEY : ");
		Reporter.log("msg");
		Reporter.log(key);
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
		
		Reporter.log(createItem.toString());
		return createItem.toString();
		
	}

}
