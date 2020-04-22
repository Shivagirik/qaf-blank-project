package com.jcp.automation.somaete.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.yfs.japi.YFSException;

public class CompareXML {
	private static Stack<String> xpathTags = new Stack<>();

	public static void compareTwoDocs(Document inputDoc, Document expectedDoc) {

		if (!xpathTags.isEmpty()) {
			xpathTags.clear();
		}
		Element eleInputRoot = inputDoc.getDocumentElement();
		Element eleExpectedRoot = expectedDoc.getDocumentElement();
		compareXML(eleExpectedRoot, eleInputRoot);
	}

	private static void compareXML(Element eleExpectedRoot, Element eleInputRoot) {

		String eleExpectedEleName = eleExpectedRoot.getNodeName();
		xpathTags.push(eleExpectedEleName);
		String eleNodeName = eleInputRoot.getNodeName();
		if (!eleNodeName.equals(eleExpectedEleName)) {
			throw new YFSException("XMl Tags are not matching");
		} else {
			NamedNodeMap eleExpAttributes = eleExpectedRoot.getAttributes();
			for (int i = 0; i < eleExpAttributes.getLength(); i++) {
				Node attr = eleExpAttributes.item(i);
				String attrExpVal = attr.getNodeValue();
				String attrName = attr.getNodeName();
				String attrInputVal = eleInputRoot.getAttribute(attrName);
				if (!attrExpVal.equals(attrInputVal)) {
					String currentXpath = getCurrentXpath();
					throw new YFSException("Value from template at Xpath " + currentXpath + "/@" + attrName + " is "
							+ attrInputVal + " but not matching to expected value " + attrExpVal);
				}
			}
		}

		int noOfExpChild = SCXmlUtil.getChildrenList(eleExpectedRoot).size();
		int noOfInpChild = SCXmlUtil.getChildrenList(eleInputRoot).size();

		if (noOfExpChild != noOfInpChild) {
			throw new YFSException("Multiple Elements in Input/Template");
		}
		Iterator<Element> childCopyEles = SCXmlUtil.getChildren(eleExpectedRoot);
		Iterator<Element> childInputEles = SCXmlUtil.getChildren(eleInputRoot);
		while (childCopyEles.hasNext()) {
			Element eleChildNode = childCopyEles.next();
			Element eleChildInputNode = childInputEles.next();
			compareXML(eleChildNode, eleChildInputNode);
			xpathTags.pop();
		}
	}

	private static String getCurrentXpath() {
		StringBuilder sb = new StringBuilder();
		for (String tag : xpathTags) {
			sb.append("/").append(tag);
		}
		return sb.toString();
	}

}
