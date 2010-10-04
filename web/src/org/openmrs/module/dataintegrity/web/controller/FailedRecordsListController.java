package org.openmrs.module.dataintegrity.web.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.SimpleFormController;

public class FailedRecordsListController extends SimpleFormController {
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		return "not used";
    }
}
