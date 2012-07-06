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

package org.openmrs.module.dataintegrity.db.hibernate;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.openmrs.module.dataintegrity.DataIntegrityConstants;
import org.openmrs.module.dataintegrity.QueryResults;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * used to extract results sets to a QueryResults object
 */
public class QueryResultsExtractor implements ResultSetExtractor {

        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                QueryResults results = new QueryResults();
                List<String> columns = new ArrayList<String>();
                
                // get the columns
                ResultSetMetaData md = rs.getMetaData();
                int columnCount = md.getColumnCount();
                for (int i=1; i <= columnCount; i++)
                        columns.add(md.getColumnLabel(i));
                results.setColumns(columns);

                // get the failed records
                while (rs.next() && results.size() <= DataIntegrityConstants.MAX_RECORDS) {
                        // TODO create batch execution to handle large amounts of data instead of capping at MAX_RECORDS
                        Object[] objectArray = new Object[columnCount];
                        for (int i=0; i < columnCount; i++)
                                objectArray[i] = rs.getObject(i+1);
                        results.add(objectArray);
                }
                
		// return null if empty
		if (results.isEmpty() && columns.isEmpty())
			return null;

                return results;
        }
        
}
