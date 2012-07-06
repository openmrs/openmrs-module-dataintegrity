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

package org.openmrs.module.dataintegrity;

import java.util.HashMap;
import java.util.Map;

public class DataIntegrityConstants {

	public static final Integer MAX_RECORDS = 250000;
	public static final String NONE = "none";
	public static final String CHECK_LANGUAGE_SQL = "sql";
	
	public static final String FAILURE_TYPE_NUMBER = "number";
	public static final String FAILURE_TYPE_COUNT = "count";
	public static final String FAILURE_TYPE_STRING = "string";
	public static final String FAILURE_TYPE_BOOLEAN = "boolean";
	public static final String FAILURE_OPERATOR_LESS_THAN = "less than";
	public static final String FAILURE_OPERATOR_GREATER_THAN = "greater than";
	public static final String FAILURE_OPERATOR_EQUALS = "equals";
	public static final String FAILURE_OPERATOR_NOT_EQUALS = "not equals";
	public static final String FAILURE_OPERATOR_CONTAINS = "contains";
	public static final String FAILURE_OPERATOR_NOT_CONTAINS = "not contains";
	public static final String RESULTS_LANGUAGE_SQL = "sql";
	public static final String RESULTS_LANGUAGE_SAME = "same";
	public static final String DATA_INTEGRITY_ERROR_STACK_TRACE = "stacktrace";
	public static final String DATA_INTEGRITY_ERROR = "error";
	public static final String PRIV_MANAGE_INTEGRITY_CHECKS = "Manage Integrity Checks";
	public static final String PRIV_RUN_INTEGRITY_CHECKS = "Run Integrity Checks";
	public static final String PRIV_VIEW_INTEGRITY_CHECKS = "View Integrity Checks";
	public static final String PRIV_VIEW_INTEGRITY_CHECK_RESULTS = "View Integrity Check Results";
	public static final String PRIV_RUN_INTEGRITY_CHECK_REPAIRS = "Run Integrity Check Repairs";
	public static final String TEMPLATE_FOLDER = "/org/openmrs/module/dataintegrity/templates/";
	public static final String TEMPLATE_EMAIL_HTML = "email-html.vm";
	public static final String TEMPLATE_EMAIL_PLAIN = "email-plain.vm";
	public static final String SCHEDULED_CHECKS_PROPERTY = "checkIds";
	public static final Integer TEST_QUERY_LIMIT = 2;

	public static final Integer RESULT_STATUS_NEW = 0;
	public static final Integer RESULT_STATUS_IGNORED = 1;
	public static final Integer RESULT_STATUS_VOIDED = 2;
	
	public static final String[] COLUMN_DATATYPES = new String[] {
		"Person", "Patient", "Observation", "Concept", "Encounter", "User", "Date", "Yes/No"
	};
	
}
