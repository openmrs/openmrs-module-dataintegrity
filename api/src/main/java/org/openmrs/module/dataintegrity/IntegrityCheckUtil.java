package org.openmrs.module.dataintegrity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.api.APIException;

public class IntegrityCheckUtil {
	
	public static String getWebAppUrl(HttpServletRequest request) {
		String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		return url;
	}

	/**
	 * cast a QueryResults object as a JSON string
	 * 
	 * @param value
	 * @return
	 */
	public static String serialize(QueryResults value) {
		JSONObject results = new JSONObject();

		// columns
		JSONArray cols = new JSONArray();
		for (String column: value.getColumns())
			cols.put(column == null ? JSONObject.NULL : column);
		results.put("columns", cols);

		// data
		JSONArray newValue = new JSONArray();
		for (Object[] oArr: value) {
			JSONArray jArr = new JSONArray();
			for (Object obj: oArr)
				jArr.put(obj == null ? JSONObject.NULL : obj.toString());
			newValue.put(jArr);
		}
		results.put("results", newValue);

		return results.toString();
	}

	public static String serializeResult(QueryResult value) {
		JSONObject results = new JSONObject();

		// data
		for (Entry<String, Object> e: value.entrySet())
			results.put(e.getKey(), e.getValue() == null ? JSONObject.NULL : e.getValue().toString());

		return results.toString();
	}

	/**
	 * use the JSON automatic deserialization to do our work
	 * 
	 * @param value
	 * @return
	 */
	public static QueryResults deserialize(String value) {
		JSONObject jValue = null;
		try {
			jValue = new JSONObject(value);
		} catch (ParseException e) {
			throw new APIException("", e);
		}
		if (!jValue.has("results"))
			return null;
		
		QueryResults results = new QueryResults();

		// columns
		if (jValue.has("columns")) {
			List<String> newCols = new ArrayList<String>();
			JSONArray cols = jValue.getJSONArray("columns");
			for (int i=0; i<cols.length(); i++)
				newCols.add(cols.getString(i));
			results.setColumns(newCols);
		}
		
		// data
		JSONArray newValue = jValue.getJSONArray("results");
		for (int i=0; i<newValue.length(); i++) {
			JSONArray jArr = newValue.getJSONArray(i);
			List<Object> lObj = new ArrayList<Object>();
			for (int j=0; j<jArr.length(); j++)
				lObj.add(jArr.get(j));
			results.add(lObj.toArray());
		}
		return results;
	}

	/**
	 * use the JSON automatic deserialization to do our work
	 * 
	 * @param value
	 * @return
	 */
	public static QueryResult deserializeResult(String value) {
		JSONObject jValue = null;
		try {
			jValue = new JSONObject(value);
		} catch (ParseException e) {
			throw new APIException("could not parse JSON from value: " + value, e);
		}
		
		// convert JSONObject to Map
		QueryResult res = new QueryResult();
		Iterator<String> keys = jValue.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			res.put(key, jValue.get(key));
		}
		
		return res;
	}
}
