package org.openmrs.module.dataintegrity.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.IntegrityCheck;
import org.openmrs.module.dataintegrity.DataIntegrityService;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.web.WebConstants;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * To create Data Integrity Schedules and map their parameters
 */
public class IntegrityScheduleCreateFormController extends SimpleFormController {
	
	private static final String extention = "dataintegrity";
	
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
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
	                                BindException errors) throws Exception {
		HttpSession httpSession = request.getSession();
		
		SchedulerService ss = Context.getSchedulerService();
		boolean scheduleRunning = false;
		TaskDefinition task = new TaskDefinition();
		String taskId = request.getParameter("taskId");
		String[] blockList = request.getParameterValues("integrityCheckIdChk");
		String integrityChecks = "";
		for (String check : blockList) {
			integrityChecks = integrityChecks + check + ",";
		}
		String taskName = extention + "_" + request.getParameter("name").trim();
		if (!taskId.equals("")) {
			if (ss.getTask(Integer.valueOf(taskId)) != null) {
				task = ss.getTask(Integer.valueOf(taskId));
				if (task.getStarted()) {
					ss.shutdownTask(task);
					scheduleRunning = true;
				}
			}
		}
		task.setName(taskName);
		Map<String, String> properties = new HashMap<String, String>(0);
		properties.put("integrityCheckId", integrityChecks);
		task.setProperties(properties);
		task.setStartTimePattern(startTimePattern);
		Date startTime = sdf.parse(request.getParameter("startTime").trim());
		task.setStartTime(startTime);
		
		String repeatIntervalUnits = request.getParameter("repeatIntervalUnits");
		int repeatInterval = Integer.parseInt(request.getParameter("repeatInterval").trim());
		long interval = 0;
		if (repeatIntervalUnits.equals("days")) {
			interval = repeatInterval * 24 * 3600;
		} else if (repeatIntervalUnits.equals("weeks")) {
			interval = repeatInterval * 7 * 24 * 3600;
		}
		task.setRepeatInterval(interval);
		task.setStartOnStartup(false);
		task.setStarted(false);
		task.setDescription(request.getParameter("description").trim() + "");
		task.setTaskClass("org.openmrs.module.dataintegrity.ScheduledIntegrityChecks");
		
		ss.saveTask(task);
		
		if (scheduleRunning)
			ss.scheduleTask(task);
		
		String success = request.getParameter("name") + " Task is saved";
		httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, success);
		
		return new ModelAndView(new RedirectView(getSuccessView()));
	}
	
	protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
		String taskId = request.getParameter("taskId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startTimePattern", startTimePattern);
		if (taskId != null) {
			TaskDefinition task = Context.getSchedulerService().getTask(Integer.valueOf(taskId));
			map.put("taskId", taskId);
			int index = task.getName().indexOf("_");
			map.put("name", task.getName().substring(index + 1));
			map.put("description", task.getDescription());
			map.put("integrityCheckId", task.getProperty("integrityCheckId"));
			map.put("startTime", sdf.format(task.getStartTime()));
			long intervalTime = task.getRepeatInterval();
			if ((intervalTime % (7 * 24 * 3600)) == 0) {
				map.put("repeatInterval", (intervalTime / (7 * 24 * 3600)) + "");
				map.put("repeatIntervalUnits", "weeks");
			} else {
				map.put("repeatInterval", (intervalTime / (24 * 3600)) + "");
				map.put("repeatIntervalUnits", "days");
			}
		} else {
			map.put("startTime", sdf.format(new Date()));
		}
		
		return map;
	}
}
