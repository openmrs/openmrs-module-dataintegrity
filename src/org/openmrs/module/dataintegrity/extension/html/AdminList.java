/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

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
		return "Manage Integrity Checks";
	}
	
	public Map<String, String> getLinks() {
		
		Map<String, String> map = new HashMap<String, String>();
		
		if (Context.hasPrivilege("Run Data Integrity Checks")) {
			map.put("module/dataintegrity/runSingleCheck.list", "dataintegrity.runSingleCheck.link");
			map.put("module/dataintegrity/runMultipleChecks.list", "dataintegrity.runMultipleChecks.link");
		} 
		if (Context.hasPrivilege("Manage Integrity Checks")) {
			map.put("module/dataintegrity/dataIntegrityChecks.list", "dataintegrity.manage.link");
			map.put("module/dataintegrity/transferCheck.list", "dataintegrity.upload.link");
		} 
		return map;
	}
}
