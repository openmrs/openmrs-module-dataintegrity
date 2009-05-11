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
