package qaf.example.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Properties;
/*
import org.apache.commons.json.JSONArray;
import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;
*/
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import util.Reporter;

import com.qmetry.qaf.automation.core.ConfigurationManager;


public class Utils {

	

	

	private static Logger log = Logger.getLogger(Logger.class.getName());

	
	

	public String callYantriksAPIStoreFilter(String apiUrl, String httpMethod, String body) {
		long startTime = System.currentTimeMillis();

		log.info("Enter callYantriksAPI");
		if (false) {  //add check for missing parameter.
			log.info("Mandatory parameters are missing");
			return "";
		}
		
		
		String timeout = "30000";

		String outputStr = "";
		URL url = null;
		try {			
			url = new URL(apiUrl);
			log.info("URL is:" + url.toString());
			if (log.isDebugEnabled()) {
				log.debug("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
				log.debug("HTTP url : " + url);
				log.debug("Body: " + body);
				log.debug("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			}

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod(httpMethod);
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");

			if ((timeout)!="")
				conn.setConnectTimeout(Integer.parseInt(timeout));

			if (httpMethod.equals("POST")) {
				OutputStream os = conn.getOutputStream();
				os.write(body.getBytes());
				os.flush();
			}
			long endTime = System.currentTimeMillis();
			if (conn.getResponseCode() != 200) {

				log.info("API Failed : HTTP error code : " + conn.getResponseCode());
			}
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String outputLine = null;
			while ((outputLine = br.readLine()) != null) {
				outputStr = outputStr.concat(outputLine);
			}

			if (outputStr == null) {

				log.info("API Failed : Could not get any response : " + conn.getResponseCode());
			}
			log.info("Output from Server ....");
			log.info(outputStr);

			if (outputStr != null) {
				/*
				String statusCode = Utils.getJsonAttribute(outputStr, "\"status\":");
				String statusMessage = Utils.getJsonAttribute(outputStr, "\"message\":");
				// log.info("Status Code is: "+statusCode + " Status message is:
				// "+statusMessage);
				long totalTime = endTime - startTime;
				log.info("URL: " + url + ", StatusCode: " + statusCode + ", ResponseTime: " + totalTime);

				// If the response status code is 500, create an alert with ErrorCode and Error
				// description
				if (statusCode.equals("500") || statusCode.equals("350") || statusCode.equals("400")) {
					log.info("Status Code from the Response is: " + statusCode);
					log.info("Status message from the Response is: " + statusMessage);
					//throw new YFSException();
				}
				*/
			}

			conn.disconnect();

		} catch (Exception e) {
			log.error("Error : " + e.getMessage() + " URL: " + apiUrl + " Input: " + body);
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		log.info("Time Taken: " + (endTime - startTime));
		log.info("Exit callYantriksAPI");

		return outputStr;
	}

	
	public static String getJsonAttribute(String jsonStr, String attr) {
		// return null if attribute is not found.
		if (jsonStr.indexOf(attr) == -1)
			return null;

		// get substring starting after the attribute.
		jsonStr = jsonStr.substring(jsonStr.indexOf(attr) + attr.length());

		// format ending - get substring till , if it is not last attribute. If it is
		// last attribute, get substring till }.
		jsonStr = jsonStr.indexOf(",") > -1 ? jsonStr.substring(0, jsonStr.indexOf(","))
				: jsonStr.substring(0, jsonStr.indexOf("}"));
		return jsonStr.trim();
	}
	
	
	
	

}
