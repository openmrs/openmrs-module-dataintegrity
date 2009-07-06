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

package org.openmrs.module.dataintegrity.impl;

import java.util.List;

import org.openmrs.api.APIException;
import org.openmrs.module.dataintegrity.DataIntegrityCheckResultTemplate;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.module.dataintegrity.DataIntegrityCheckTemplate;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;

public class DataIntegrityServiceImpl implements DataIntegrityService {

	private DataIntegrityDAO dao;
	
	public List<DataIntegrityCheckTemplate> getAllDataIntegrityCheckTemplates()
			throws APIException {
		return this.dao.getAllDataIntegrityCheckTemplates();
	}

	public DataIntegrityCheckTemplate getDataIntegrityCheckTemplate(Integer templateId)
			throws APIException {
		return this.dao.getDataIntegrityCheckTemplate(templateId);
	}

	public void saveDataIntegrityCheckTemplate(DataIntegrityCheckTemplate dataIntegrityTemplate)
			throws APIException {
		this.dao.saveDataIntegrityCheckTemplate(dataIntegrityTemplate);
	}

	public void setDataIntegrityDAO(DataIntegrityDAO dao) {
		this.dao = dao;
	}
	
	public DataIntegrityDAO getDataIntegrityDAO() {
		return this.dao;
	}

	public void deleteDataIntegrityCheckTemplate(DataIntegrityCheckTemplate template) {
		this.dao.deleteDataIntegrityCheckTemplate(template);
	}

	public DataIntegrityCheckResultTemplate runIntegrityCheck(DataIntegrityCheckTemplate template) {
		DataIntegrityCheckResultTemplate resultTemplate = new DataIntegrityCheckResultTemplate();
		/*List<Object[]> failedRecords = this.dao.executeSQLQuery(template.getIntegrityCheckSql());
		boolean checkPassed = (failedRecords.size() <= template.getIntegrityCheckScore()) ? true : false;
		resultTemplate.setCheckId(template.getIntegrityCheckId());
		resultTemplate.setCheckName(template.getIntegrityCheckName());
		resultTemplate.setFailedRecords(failedRecords);
		resultTemplate.setCheckPassed(checkPassed);
		resultTemplate.setFailedRecordCount(failedRecords.size());
		
		//Getting the column count
		if (failedRecords.size() > 0) {
			try {
				Object[] records = failedRecords.get(0);
				resultTemplate.setColumnCount(records.length);
			} catch (Exception e) {
				resultTemplate.setColumnCount(1);
			}
		} else {
			resultTemplate.setColumnCount(0);
		}*/
		
		return resultTemplate;
	}
}
