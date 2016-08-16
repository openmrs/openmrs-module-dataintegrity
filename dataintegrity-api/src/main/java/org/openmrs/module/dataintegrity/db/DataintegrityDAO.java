package org.openmrs.module.dataintegrity.db;

import java.util.List;

public interface DataintegrityDAO {

	List<DataintegrityRule> getRules();

	void saveResults(List<DataintegrityResult> results);

	void clearAllResults();
}
