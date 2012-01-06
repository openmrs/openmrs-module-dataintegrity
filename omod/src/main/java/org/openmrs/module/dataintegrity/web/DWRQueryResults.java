/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.dataintegrity.web;

import java.util.ArrayList;
import java.util.List;
import org.openmrs.module.dataintegrity.QueryResults;

/**
 *
 * @author jkeiper
 */
public class DWRQueryResults {

        private List<String> columns;
        private ArrayList<Object[]> data;
        
        public DWRQueryResults() {
                // pass;
        }
        
        public DWRQueryResults(QueryResults qr) {
                this.setColumns(qr.getColumns());
                this.data = new ArrayList<Object[]>();
                this.data.addAll(qr);
        }

        public List<String> getColumns() {
                return columns;
        }

        public void setColumns(List<String> columns) {
                this.columns = columns;
        }

        public ArrayList<Object[]> getData() {
                return data;
        }

        public void setData(ArrayList<Object[]> data) {
                this.data = data;
        }
        
        
}
