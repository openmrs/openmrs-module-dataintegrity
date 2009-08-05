package org.openmrs.module.dataintegrity.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.openmrs.module.dataintegrity.DataIntegrityCheckResultTemplate;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class FailedRecordsListController extends SimpleFormController {
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		return "not used";
    }
}
