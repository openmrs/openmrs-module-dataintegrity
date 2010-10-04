package org.openmrs.module.dataintegrity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class DataIntegrityXmlParser {
	private Document doc;
	
	public DataIntegrityXmlParser(File xmlFile) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.doc = builder.parse(xmlFile);
		} catch (Exception ex) {
			
		}
	}
	
	public List<IntegrityCheck> getChecksToUpload() {
		List<IntegrityCheck> checksToUpload = new ArrayList<IntegrityCheck>();
		
		
		return checksToUpload;
	}
	
	

}
