package org.openmrs.module.dataintegrity;

import java.util.List;

import org.openmrs.api.APIException;
import org.openmrs.module.dataintegrity.db.DataIntegrityDAO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DataIntegrityService {
	
	public void setIdcardsDAO(DataIntegrityDAO dao);

    public void saveIdcardsTemplate(DataIntegrityTemplate dataIntegrityTemplate) throws APIException;

    public DataIntegrityTemplate getDataIntegrityTemplate(Integer templateId) throws APIException;

    public List<DataIntegrityTemplate> getAllDataIntegrityTemplates() throws APIException;

}
