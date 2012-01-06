/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.dataintegrity.web;

import java.util.ArrayList;
import java.util.List;
import org.openmrs.module.dataintegrity.IntegrityCheckColumn;

/**
 *
 * @author jkeiper
 */
public class DWRIntegrityCheckColumns {

	private List<IntegrityCheckColumn> columns;
	
	public DWRIntegrityCheckColumns(List<IntegrityCheckColumn> columns) {
		this.columns = new ArrayList<IntegrityCheckColumn>();
		this.columns.addAll(columns);
	}

	public List<IntegrityCheckColumn> getColumns() {
		if (columns == null)
			columns = new ArrayList<IntegrityCheckColumn>();
		return columns;
	}

	public void setColumns(List<IntegrityCheckColumn> columns) {
		this.columns = columns;
	}
	
}
