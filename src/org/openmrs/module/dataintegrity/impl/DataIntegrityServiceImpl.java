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

	public void deleteDataIntegrityCheckTemplate(Integer templateId) {
		this.dao.deleteDataIntegrityCheckTemplate(templateId);
	}
}
