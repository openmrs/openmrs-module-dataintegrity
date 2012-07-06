package org.openmrs.module.dataintegrity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openmrs.api.APIException;

public class QueryResult extends HashMap<String, Object> {

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
		this.putAll(res);
	}

	/**
	 * convenience constructor for building a copy
	 * 
	 * @param res
	 */
	public QueryResult(Map res) {
		super();
		this.putAll(res);
	}

	/**
	 * convenience constructor for building a copy
	 * 
	 * @param res
	 */
	public QueryResult(List<String> columns, Object[] data) {
		super();

		if (columns == null || data == null)
			return;
		
		if (columns.size() != data.length)
			throw new APIException("cannot create QueryResult from mismatched column and data");
		
		int i = 0;
		for (String column: columns)
			this.put(column, data[i++]);
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
