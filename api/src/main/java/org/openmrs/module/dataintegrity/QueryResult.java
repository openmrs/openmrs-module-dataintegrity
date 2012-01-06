package org.openmrs.module.dataintegrity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryResult extends ArrayList<Object> {

	private static final long serialVersionUID = 1L;

	/**
	 * default constructor
	 */
	public QueryResult() {
		super();
	}

	/**
	 * convenience constructor for building a copy
	 * 
	 * @param res
	 */
	public QueryResult(QueryResult res) {
		super();
		this.addAll(res);
	}

	/**
	 * convenience constructor for building a copy
	 * 
	 * @param res
	 */
	public QueryResult(Object[] res) {
		super();
		this.addAll(Arrays.asList(res));
	}

	/**
	 * convenience constructor for building a copy
	 * 
	 * @param res
	 */
	public QueryResult(List<Object> res) {
		super();
		this.addAll(res);
	}

	/**
	 * required for hibernate class
	 * 
	 * @param value
	 * @return
	 */
	public static QueryResult valueOf(String value) {
		return IntegrityCheckUtil.deserializeResult(value);
	}
	
}
