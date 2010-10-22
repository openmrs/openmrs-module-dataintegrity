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

package org.openmrs.module.dataintegrity.db;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.dataintegrity.IntegrityCheckResults;
import org.openmrs.module.dataintegrity.IntegrityCheck;

public interface DataIntegrityDAO {

	public void setSessionFactory(SessionFactory sessionFactory);

	public IntegrityCheck saveIntegrityCheck(IntegrityCheck integrityCheck)
			throws DAOException;

	public IntegrityCheck getIntegrityCheck(Integer checkId)
			throws DAOException;

	public List<IntegrityCheck> getAllIntegrityChecks() throws DAOException;

	public void deleteIntegrityCheck(IntegrityCheck integrityCheck)
			throws DAOException;

	public SessionFactory getSessionFactory();

	public void repairDataIntegrityCheckViaScript(IntegrityCheck template)
			throws DAOException;

	public IntegrityCheckResults saveResults(IntegrityCheckResults results);

	public IntegrityCheckResults getResults(Integer resultsId);

	public void deleteResults(IntegrityCheckResults results);

	public IntegrityCheckResults getResultsForIntegrityCheck(
			IntegrityCheck integrityCheck);
}
