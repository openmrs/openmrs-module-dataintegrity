package org.openmrs.module.dataintegrity.extension.html;

import java.util.HashMap;
import java.util.Map;

import org.openmrs.api.context.Context;
import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.AdministrationSectionExt;

public class AdminList extends AdministrationSectionExt {
	
	public Extension.MEDIA_TYPE getMediaType() {
		return Extension.MEDIA_TYPE.html;
	}
	
	public String getTitle() {
		return "dataintegrity.title";
	}
	
	public String getRequiredPrivilege() {
		return "Run Data Integrity Tests";
	}
	
	public Map<String, String> getLinks() {
		
		Map<String, String> map = new HashMap<String, String>();
		
		if (Context.hasPrivilege("Run Data Integrity Tests")) {
			map.put("module/dataintegrity/viewHelloWorld.htm", "dataintegrity.view");
		}
		return map;
	}
}
