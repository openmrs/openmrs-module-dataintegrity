package org.openmrs.module.dataintegrity;

import java.util.ArrayList;
import java.util.List;

public class QueryResults extends ArrayList<Object[]> {

	private static final long serialVersionUID = 1L;

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
	 * required for hibernate class
	 * 
	 * @param value
	 * @return
	 */
	public static QueryResults valueOf(String value) {
		return IntegrityCheckUtil.deserialize(value);
	}
	
}
