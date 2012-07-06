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

package org.openmrs.module.dataintegrity;

import java.util.ArrayList;
import java.util.List;

public class QueryResults extends ArrayList<Object[]> {

	private static final long serialVersionUID = 1L;

	private List<String> columns;

	/**
	 * default constructor
	 */
	public QueryResults() {
		super();
	}

	/**
	 * convenience constructor for building a copy
	 * 
	 * @param res
	 */
	public QueryResults(QueryResults res) {
		super();
		this.setColumns(res.getColumns());
		this.addAll(res);
	}

	/**
	 * convenience constructor for building a copy
	 * 
	 * @param res
	 */
	public QueryResults(List<Object[]> res) {
		super();
		this.addAll(res);
	}

	/**
	 * convenience constructor for creating the results with a list of columns
	 * 
	 * @param columns
	 * @param records
	 */
	public QueryResults(List<String> columns, List<Object[]> records) {
		super();
		this.setColumns(columns);
		this.addAll(records);
	}

	/**
	 * @return the columns
	 */
	public List<String> getColumns() {
		if (columns == null)
			columns = new ArrayList<String>();
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	/**
	 * public access to column count; determined from either the column list or
	 * the data array
	 * 
	 * @return number of columns
	 */
	public int getColumnCount() {
		// first try the column list; could still be empty
		int count = columns == null ? 0 : columns.size();
		if (count > 0)
			return count;
		
		// next try the first element of the data
		return this.size() > 0 ? this.get(0).length : 0;
	}
	
	/**
	 * required for hibernate class
	 * 
	 * @param value
	 * @return
	 */
	public static QueryResults valueOf(String value) {
		return IntegrityCheckUtil.deserialize(value);
	}
	
}
