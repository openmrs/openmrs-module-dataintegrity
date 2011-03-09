package org.openmrs.module.dataintegrity.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.IntegrityCheckUtil;
import org.openmrs.module.dataintegrity.SendIntegrityCheckSummaryEmailTask;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.WebConstants;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * To create Data Integrity Schedules and map their parameters
 */
public class IntegrityScheduleEmailFormController extends SimpleFormController {
	
	private static final String prefix = "dataintegrityEmail";
	
	private static final String startTimePattern = "MM/dd/yyyy HH:mm:ss";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat(startTimePattern);
	
	/**
	 * provide data for the view
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		
		if (Context.isAuthenticated()) {
			// return a non-empty list if the user has authenticated properly
			AdministrationService as = Context.getAdministrationService();
			return as.getAllGlobalProperties();
		}
		return new ArrayList<GlobalProperty>();
	}
	
	/**
	 * handle form submission
	 */
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
	                                BindException errors) throws Exception {
		HttpSession httpSession = request.getSession();
		
		SchedulerService ss = Context.getSchedulerService();

		boolean scheduleRunning = false;
		TaskDefinition task = new TaskDefinition();
		String taskId = request.getParameter("taskId");
		String emailsVal = request.getParameter("emailsVal");
		String txtName = request.getParameter("txtname");
		
		if (StringUtils.hasText(txtName)) {
			String taskName = txtName;
			if (StringUtils.hasText(taskId)) {
				if (ss.getTask(Integer.valueOf(taskId)) != null) {
					task = ss.getTask(Integer.valueOf(taskId));
					if (task.getStarted()) {
						ss.shutdownTask(task);
						scheduleRunning = true;
					}
				}
			}
			task.setName(prefix + "_" + taskName);
			Map<String, String> properties = new HashMap<String, String>(0);
			properties.put("emailsVal", emailsVal);
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
			task.setTaskClass(SendIntegrityCheckSummaryEmailTask.class.getCanonicalName());
			
			ss.saveTask(task);
			
			// schedule the task if it was previously running
			if (scheduleRunning)
				ss.scheduleTask(task);
			
			String success = task.getName() + " Task is saved";
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, success);
			
			return new ModelAndView(new RedirectView(getSuccessView()));
			
		} else {
			String failure = "Task not saved. The [Schedule Name] was not provided.";
			httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, failure);
			return new ModelAndView(new RedirectView(IntegrityCheckUtil.getWebAppUrl(request)
			        + "/module/dataintegrity/integrityScheduleEmail.form"));
		}
		
	}

	/**
	 * generate data for the view
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
			map.put("emailsVal", task.getProperty("emailsVal"));
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
		} else {
			map.put("startTime", sdf.format(new Date()));
		}
		
		return map;
	}
}
