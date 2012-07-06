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

package org.openmrs.module.dataintegrity.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityConstants;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.WebConstants;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * To create Data Integrity Schedules and map their parameters
 */
public class IntegrityScheduleFormController extends SimpleFormController {
	
	private static final String prefix = "dataintegritySchedule";
	
	private static final String startTimePattern = "MM/dd/yyyy HH:mm:ss";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat(startTimePattern);
	
	private DataIntegrityService getDataIntegrityService() {
		return (DataIntegrityService) Context.getService(DataIntegrityService.class);
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		List<IntegrityCheck> checks = new ArrayList<IntegrityCheck>();
		if (Context.isAuthenticated()) {
			checks = getDataIntegrityService().getAllIntegrityChecks();
		}
		return checks;
	}
	
	/**
	 * accept new/edit task data
	 */
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
	                                BindException errors) throws Exception {
		HttpSession httpSession = request.getSession();
		
		SchedulerService ss = Context.getSchedulerService();
		String taskId = request.getParameter("taskId");
		String[] blockList = request.getParameterValues("checkIdsChk");
		String integrityChecks = StringUtils.join(blockList, ',');
		String taskName = prefix + "_" + request.getParameter("txtname").trim();
		
		TaskDefinition task = new TaskDefinition();
		boolean scheduleRunning = false;
		
		// shut down task if it is running
		if (!StringUtils.isEmpty(taskId)) {
			if (ss.getTask(Integer.valueOf(taskId)) != null) {
				task = ss.getTask(Integer.valueOf(taskId));
				if (task.getStarted()) {
					ss.shutdownTask(task);
					scheduleRunning = true;
				}
			}
		}
		
		// set task values
		task.setName(taskName);
		Map<String, String> properties = new HashMap<String, String>(0);
		properties.put(DataIntegrityConstants.SCHEDULED_CHECKS_PROPERTY, integrityChecks);
		task.setProperties(properties);
		task.setStartTimePattern(startTimePattern);
		Date startTime = sdf.parse(request.getParameter("startTime").trim());
		task.setStartTime(startTime);
		
		String repeatIntervalUnits = request.getParameter("repeatIntervalUnits");
		int repeatInterval = Integer.parseInt(request.getParameter("repeatInterval").trim());
		long interval = 0;
		if (OpenmrsUtil.nullSafeEquals(repeatIntervalUnits, "weeks")) {
			interval = repeatInterval * 7 * 24 * 3600;
		} else if (OpenmrsUtil.nullSafeEquals(repeatIntervalUnits, "days")) {
			interval = repeatInterval * 24 * 3600;
		} else if (OpenmrsUtil.nullSafeEquals(repeatIntervalUnits, "hours")) {
			interval = repeatInterval * 3600;
		} else if (OpenmrsUtil.nullSafeEquals(repeatIntervalUnits, "minutes")) {
			interval = repeatInterval * 60;
		} else if (OpenmrsUtil.nullSafeEquals(repeatIntervalUnits, "seconds")) {
			interval = repeatInterval;
		} else {
			// TODO set a default interval if the one provided doesn't match one we know
		}
		task.setRepeatInterval(interval);
		task.setStartOnStartup(false);
		task.setStarted(false);
		task.setDescription(request.getParameter("txtdescription").trim() + "");
		task.setTaskClass("org.openmrs.module.dataintegrity.RunScheduledIntegrityChecksTask");
		
		// save task
		ss.saveTask(task);
		
		// start task if it was previously running
		if (scheduleRunning)
			ss.scheduleTask(task);
		
		String success = request.getParameter("txtname") + " Task is saved";
		httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, success);
		
		return new ModelAndView(new RedirectView(getSuccessView()));
	}
	
	/**
	 * provide data for existing task
	 */
	protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
		String taskId = request.getParameter("taskId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startTimePattern", startTimePattern);
		if (taskId != null) {
			TaskDefinition task = Context.getSchedulerService().getTask(Integer.valueOf(taskId));
			map.put("taskId", taskId);
			int index = task.getName().indexOf("_");
			map.put("txtname", task.getName().substring(index + 1));
			map.put("txtdescription", task.getDescription());
			map.put("startTime", sdf.format(task.getStartTime()));
			long intervalTime = task.getRepeatInterval();
			if ((intervalTime % (7 * 24 * 3600)) == 0) {
				map.put("repeatInterval", (intervalTime / (7 * 24 * 3600)) + "");
				map.put("repeatIntervalUnits", "weeks");
			} else if ((intervalTime % (24 * 3600)) == 0) {
				map.put("repeatInterval", (intervalTime / (24 * 3600)) + "");
				map.put("repeatIntervalUnits", "days");
			} else if ((intervalTime % (3600)) == 0) {
				map.put("repeatInterval", (intervalTime / (3600)) + "");
				map.put("repeatIntervalUnits", "hours");
			} else if ((intervalTime % (60)) == 0) {
				map.put("repeatInterval", (intervalTime / (60)) + "");
				map.put("repeatIntervalUnits", "minutes");
			} else {
				map.put("repeatInterval", (intervalTime) + "");
				map.put("repeatIntervalUnits", "seconds");
			}
			String checkIds = task.getProperty("checkIds");
			try {
				if (StringUtils.isEmpty(checkIds))
					map.put("checkIds", Collections.EMPTY_LIST);
				else if (checkIds.contains(",")) {
					List<Integer> ids = new ArrayList<Integer>();
					for (String id: checkIds.split(","))
						ids.add(Integer.parseInt(id));
					map.put("checkIds", ids);
				} else
					map.put("checkIds", Collections.singleton(Integer.parseInt(checkIds)));
			} catch(NumberFormatException e) {
				map.put("checkIds", Collections.EMPTY_LIST);
			}
		} else {
			map.put("startTime", sdf.format(new Date()));
		}
		
		return map;
	}
}
