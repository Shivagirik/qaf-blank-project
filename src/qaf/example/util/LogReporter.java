package qaf.example.util;

import org.apache.log4j.Logger;

import com.qmetry.qaf.automation.util.Reporter;

public  class LogReporter {
	
	private String getClassName;
	private Logger log;
	public LogReporter(String className){
		 if(className.isEmpty()||className==null){
			 this.getClassName="Test Automation";
			 this.log= Logger.getLogger("Test Automation");
		 }
		 else
		 {
			 this.getClassName=className;
			 this.log= Logger.getLogger(className);
		 }
		 
	}
	
	
	
	public void info(String message){
		Reporter.log("["+getClassName+"]-"+message);
		log.info(message);
	}
	
	public void debug(String message){
		Reporter.log("["+getClassName+"]-"+message);
		log.debug(message);
	}
	
	public void error(String message){
		Reporter.log("["+getClassName+"]-"+message);
		log.error(message);
	}
		
}
