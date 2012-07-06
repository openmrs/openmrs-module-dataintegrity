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
