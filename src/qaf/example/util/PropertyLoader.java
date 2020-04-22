package qaf.example.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
	
	static String PROPERTIES_FILE_PATH = "resources//";
	String requiredPropertyFile;
	String propertyFilePath=PROPERTIES_FILE_PATH + requiredPropertyFile;
	String reqProperty;
	String reqPropertyValue;
	
	
	
	Properties prop;
	FileInputStream inputStream = null;
	 
	


	public String getPropertyValue(String reqpropertyFile,String reqproperty){
		
		if(reqpropertyFile.contains("application")){
			reqpropertyFile="application.properties";
		}
		else if(reqpropertyFile.contains("search")){
			reqpropertyFile="search.properties";
		}
		
		propertyFile(reqpropertyFile);
		reqroperty(reqproperty);
		
		prop = new Properties();
		try {
			File file = new File(propertyFilePath);
			try {
				inputStream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (inputStream != null) {
				prop = new Properties();
				prop.load(inputStream);
				reqPropertyValue = prop.getProperty(reqProperty);
				inputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return reqPropertyValue;
				
	}
	
	private void propertyFile(String propertyFile){
		this.requiredPropertyFile=propertyFile;		
		this.propertyFilePath=PROPERTIES_FILE_PATH + requiredPropertyFile;
	}
	
	private void reqroperty(String reqproperty){
		this.reqProperty=reqproperty;
	}

}
