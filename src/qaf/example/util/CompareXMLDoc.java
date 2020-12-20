package qaf.example.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.yfs.japi.YFSException;

public class CompareXMLDoc {
	private static Stack<String> xpathTags = new Stack<>();
	static String ignoreAttributeList;
	static String returnStatus="OK";
	private static LogReporter log = new LogReporter("com.jcp.automation.somaete.util.CompareXMLDoc");
	static FileInputStream inputStream = null;
	static Properties prop;
	static {
		prop = new Properties();
		try {
			File file = new File("resources/soma/SOMARegression/xmlValidation.properties");
			try {
				inputStream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (inputStream != null) {
				prop = new Properties();
				prop.load(inputStream);
				ignoreAttributeList = prop.getProperty("ignore.AttributeList");
				inputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static HashMap<String, String> ATTRIBUTE_TO_IGNORE;
	static {
		String [] iList= ignoreAttributeList.split(",");
			ATTRIBUTE_TO_IGNORE = new HashMap<>();
		
		for(int i=0;i<iList.length;i++){
			if(!ATTRIBUTE_TO_IGNORE.containsKey(iList[i])){
				
				ATTRIBUTE_TO_IGNORE.put(iList[i], "ignore");
			}
		}		
		
	}
	
	
	
	
	
	
	
	public static void compareTwoDocs(Document inputDoc, Document expectedDoc1,Document expectedDoc2,Document expectedDoc3) {

		if (!xpathTags.isEmpty()) {
			xpathTags.clear();
		}
		Element eleInputRoot = inputDoc.getDocumentElement();
		Element eleExpectedRoot1 = expectedDoc1.getDocumentElement();
		log.info("SOMAAutomationUtil :: Input for Matching :: " + SCXmlUtil.getString(inputDoc));
		log.info("SOMAAutomationUtil :: Expected for Matching expectedDoc1:: " + SCXmlUtil.getString(expectedDoc1));
		setReturnStatus("OK");
		String result1=compareXML(eleExpectedRoot1, eleInputRoot);
		
		if (!result1.equalsIgnoreCase("OK") && expectedDoc2 != null ){
			log.info("Actual xml is was not matching when compared with expectedDoc1. Trying to compare with next expected document.. ");
			log.info("SOMAAutomationUtil :: Expected for Matching expectedDoc2:: " + SCXmlUtil.getString(expectedDoc2));
			Element eleExpectedRoot2 = expectedDoc2.getDocumentElement();	
			setReturnStatus("OK");
			String result2=compareXML(eleExpectedRoot2, eleInputRoot);
			
			if (!result2.equalsIgnoreCase("OK") && expectedDoc3 != null ){
				log.info("Actual xml is was not matching when compared with expectedDoc2. Trying to compare with next expected document.. ");
				log.info("SOMAAutomationUtil :: Expected for Matching expectedDoc3:: " + SCXmlUtil.getString(expectedDoc3));
				Element eleExpectedRoot3 = expectedDoc3.getDocumentElement();
				setReturnStatus("OK");
				String result3=compareXML(eleExpectedRoot3, eleInputRoot);
				if(result3.equalsIgnoreCase("OK")){
					log.info("Xml Attribute and Value are as expected with expectedDoc3");
				}
				else{
					log.error(result3 + " in expectedDoc3");
					throw new YFSException(result3 + " in expectedDoc3");
				}
					
			}			
			else if(!result2.equalsIgnoreCase("OK") && expectedDoc3 == null){
				log.error(result2 + " in expectedDoc2");
				throw new YFSException(result2 + " in expectedDoc2");
			}
			else{
				log.info("Xml Attribute and Value are as expected with expectedDoc2");			
			}	
		}
		else if(!result1.equalsIgnoreCase("OK") && expectedDoc2 == null){
			log.error(result1 + " in expectedDoc1");
			throw new YFSException(result1 + " in expectedDoc1");
		}
		else{
			log.info("Xml Attribute and Value are as expected with expectedDoc1");
			
		}
		
	}
	
	private static String compareXML(Element eleExpectedRoot, Element eleInputRoot) {
		
		String eleExpectedEleName = eleExpectedRoot.getNodeName();
		xpathTags.push(eleExpectedEleName);
		String eleNodeName = eleInputRoot.getNodeName();
		if (!eleNodeName.equals(eleExpectedEleName)) {
			log.info("Something NOT as expected::XMl Tags are not matching @"+eleExpectedEleName);
			setReturnStatus("XMl Tags are not matching");
			return "XMl Tags are not matching";
		} else {
			NamedNodeMap eleExpAttributes = eleExpectedRoot.getAttributes();
			for (int i = 0; i < eleExpAttributes.getLength(); i++) {
				Node attr = eleExpAttributes.item(i);
				String attrExpVal = attr.getNodeValue();
				String attrName = attr.getNodeName();				
				String attrInputVal = eleInputRoot.getAttribute(attrName);				
				if(eleInputRoot.hasAttribute(attrName)){
					if (!attrExpVal.equals(attrInputVal) && !attrExpVal.equals("VerifyAttributeOnly")&&!ATTRIBUTE_TO_IGNORE.containsKey(attrName)) {
						String currentXpath = getCurrentXpath();
						log.info("Value from template at Xpath " + currentXpath + "/@" + attrName + " is "
								+ attrInputVal + " but not matching to expected value " + attrExpVal);
						setReturnStatus("Some Attribute/Values Not Matching");
						
					}
				}
				else{
					String currentXpath = getCurrentXpath();
					log.info("Attribute @ " + currentXpath + "/@" + attrName + " is NOT found");
					setReturnStatus("Some Attribute/Values Not Matching");
				}
			}
		}

		int noOfExpChild = SCXmlUtil.getChildrenList(eleExpectedRoot).size();
		int noOfInpChild = SCXmlUtil.getChildrenList(eleInputRoot).size();

		if (noOfExpChild != noOfInpChild) {
			//Exclusion list added..
			if(!eleExpectedRoot.getNodeName().equalsIgnoreCase("OMNIMessageList")){
				log.info("Something NOT as expected::Multiple Elements in Input/Template @node "+eleExpectedRoot.getNodeName());
				setReturnStatus("XMl Tags are not matching");
				return "XMl Tags are not matching";
			}
			else{
				return "OK";
			}
		}
		Iterator<Element> childCopyEles = SCXmlUtil.getChildren(eleExpectedRoot);
		Iterator<Element> childInputEles = SCXmlUtil.getChildren(eleInputRoot);
		while (childCopyEles.hasNext()) {
			Element eleChildNode = childCopyEles.next();
			Element eleChildInputNode = childInputEles.next();
			compareXML(eleChildNode, eleChildInputNode);
			xpathTags.pop();
		}
		
		String finalStatus=getReturnStatus();
		//setReturnStatus("OK");
		return finalStatus;
	}

	private static String getCurrentXpath() {
		StringBuilder sb = new StringBuilder();
		for (String tag : xpathTags) {
			sb.append("/").append(tag);
		}
		return sb.toString();
	}
	
	
	public static String getReturnStatus() {
	    return returnStatus;
	  }

	  public static void setReturnStatus(String newStatus) {
	    returnStatus = newStatus;
	  }

}
