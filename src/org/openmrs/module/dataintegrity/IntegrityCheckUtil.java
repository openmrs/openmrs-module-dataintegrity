package org.openmrs.module.dataintegrity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.api.APIException;
import org.openmrs.util.OpenmrsUtil;

public class IntegrityCheckUtil {
	public static File getExportIntegrityCheckFile() throws Exception {
		File file = null;
		try {
			File folder = getTemporaryCheckRepository();
			file = new File(folder.getAbsolutePath() + File.separator + "export_checks_temp.xml");
			return file;
		} catch (Exception ex) {
			throw new Exception("Cannot create export file");
		}
	}
	
	public static File getZippedExportIntegrityCheckFile() throws Exception {
		File file = null;
		try {
			File folder = getTemporaryCheckRepository();
			file = new File(folder.getAbsolutePath() + File.separator + "export_checks_temp.zip");
			return file;
		} catch (Exception ex) {
			throw new Exception("Cannot create zip file");
		}
	}
	
	public static File uploadIntegrityCheckFile(InputStream inputStream, String filename) throws Exception {
		FileOutputStream outputStream = null;
		File file = null;
		try {
			File folder = getTemporaryCheckRepository();
			
			// check if module filename is already loaded
			if (OpenmrsUtil.folderContains(folder, filename))
				throw new Exception(filename + " already exists");
			
			file = new File(folder.getAbsolutePath() + File.separator + filename);
			
			outputStream = new FileOutputStream(file);
			OpenmrsUtil.copyFile(inputStream, outputStream);
		}
		catch (FileNotFoundException e) {
			throw new Exception("Can't create check file for " + filename, e);
		}
		catch (IOException e) {
			throw new Exception("Can't create check file for " + filename, e);
		}
		finally {
			try {
				inputStream.close();
			}
			catch (Exception e) { /* pass */}
			try {
				outputStream.close();
			}
			catch (Exception e) { /* pass */}
		}
		
		return file;
	}
	
	private static File getTemporaryCheckRepository() throws Exception {
		
		String folderName = "dataintegrity";
		
		// try to load the repository folder straight away.
		File folder = new File(folderName);
		
		// if the property wasn't a full path already, assume it was intended to be a folder in the 
		// application directory
		if (!folder.exists()) {
			folder = new File(OpenmrsUtil.getApplicationDataDirectory(), folderName);
		}
		
		// now create the modules folder if it doesn't exist
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		if (!folder.isDirectory())
			throw new Exception("Check repository is not a directory at: " + folder.getAbsolutePath());
		
		return folder;
	}
	
	public static String getModifiedCheckCode(String checkCode, String parameterList, String parameterValuesList) {
		String[] parameters = parameterList.split(";");
		if (parameterValuesList != null) {
			String[] parameterValuesArray = parameterValuesList.split(";");
			for (int i=0; i<parameters.length; i++) {
				checkCode = checkCode.replace(parameters[i], parameterValuesArray[i]);
			}
		}
		return checkCode;
	}
	
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
		JSONArray newValue = new JSONArray();
		for (Object[] oArr: value) {
			JSONArray jArr = new JSONArray();
			for (Object obj: oArr)
				jArr.put(obj == null ? JSONObject.NULL : obj.toString());
			newValue.put(jArr);
		}
		JSONObject results = new JSONObject();
		results.put("results", newValue);
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
}
