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
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.module.dataintegrity.DataIntegrityTemplate;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;

public class DataIntegrityServiceImpl implements DataIntegrityService {

	private DataIntegrityDAO dao;
	
	public List<DataIntegrityTemplate> getAllDataIntegrityTemplates()
			throws APIException {
		return this.dao.getAllDataIntegrityTemplates();
	}

	public DataIntegrityTemplate getDataIntegrityTemplate(Integer templateId)
			throws APIException {
		return this.dao.getDataIntegrityTemplate(templateId);
	}

	public void saveIdcardsTemplate(DataIntegrityTemplate dataIntegrityTemplate)
			throws APIException {
		this.dao.saveDataIntegrityTemplate(dataIntegrityTemplate);
	}

	public void setIdcardsDAO(DataIntegrityDAO dao) {
		this.dao = dao;
	}
	
	public DataIntegrityDAO getDataIntegrityDAO() {
		return this.dao;
	}
}
