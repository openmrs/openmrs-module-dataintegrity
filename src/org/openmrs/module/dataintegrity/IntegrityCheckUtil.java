package org.openmrs.module.dataintegrity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
				checkCode.replace(parameters[i], parameterValuesArray[i]);
			}
		}
		return checkCode;
	}
}
