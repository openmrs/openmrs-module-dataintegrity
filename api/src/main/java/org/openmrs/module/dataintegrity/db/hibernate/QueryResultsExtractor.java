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
