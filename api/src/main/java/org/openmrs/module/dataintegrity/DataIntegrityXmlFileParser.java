package org.openmrs.module.dataintegrity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DataIntegrityXmlFileParser {
	private Document doc;
	
	public DataIntegrityXmlFileParser(File xmlFile) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.doc = builder.parse(xmlFile);
		} catch (Exception e) {
			
		}
	}
	
	public List<IDataIntegrityCheckUpload> getChecksToAdd() throws Exception
	{
		try {
			List<IDataIntegrityCheckUpload> checks = new ArrayList<IDataIntegrityCheckUpload>();
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			String xpathStr = "//check";
			XPathExpression expr = xpath.compile(xpathStr);
			Object result = expr.evaluate(this.doc, XPathConstants.NODESET);	
			NodeList integrityChecks = (NodeList)result;
			for (int i=0; i<integrityChecks.getLength(); i++) {
				Element check = (Element) integrityChecks.item(i);
				if (check.getAttribute("type").toLowerCase().equals("sql")) {
					IDataIntegrityCheckUpload sqlUpload = new DataIntegritySqlUpload(check);
					checks.add(sqlUpload);
				}
			}
			return checks;
		} catch (Exception e) {
			throw new Exception("Error in parsing the XML file. " + e.getMessage());
		}
	}
	
	
	

}
