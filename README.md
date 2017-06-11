# Data Integrity Module 

This module provides a way of writing rules that evaluate missing and invalid data within encounters, visits and observations. 

The rules can be run manually or automatically by a job which is based on OpenMRS scheduler Task that is scheduled to run at a configurable regular interval which runs all the rules.


## Writing a rule

* Rules can be written in java and groovy. Every rule should extend [RuleDefinition](https://github.com/openmrs/openmrs-module-dataintegrity/blob/master/api/src/main/java/org/openmrs/module/dataintegrity/rule/RuleDefinition.java)
* The RuleDefn can return data violations in org.openmrs.Patient and org.openmrs.PatientProgram entities.
* The RuleDefinition returns [RuleResult](https://github.com/openmrs/openmrs-module-dataintegrity/blob/master/api/src/main/java/org/openmrs/module/dataintegrity/rule/RuleResult.java).  It contains the entity details (like Patient or PatientProgram) with some extra information like notes and actionUrl (for some UI action in case of integrity issues).
* Rule can be made available in any module.  Once the rule is written, it has to updated in the dataintegrity_rule table in the following ways.
  * Return non-null DataIntegrityRule instance from the getRule() method of the RuleDefinition interface which is automatically saved to the database.
  * The following is a sample insert command.

```sql
insert into dataintegrity_rule(rule_name,rule_category,handler_config,handler_classname) values('Questionable date of death','patient','java','org.openmrs.module.dataintegrity.InvalidDateOfDeath');
```
    
* Rules are evaulated by a scheduled task called [DataIntegrityTask](https://github.com/openmrs/openmrs-module-dataintegrity/blob/master/dataintegrity-omod/src/main/java/org/openmrs/module/dataintegrity/scheduler/DataIntegrityTask.java).  This is based on OpenMRS Scheduler task.  The job is automatically added when this OMOD is deployed.  It can be seen in the (Admin > Manage Scheduler)[http://localhost:8081/openmrs-standalone/admin/scheduler/scheduler.list] screen.  It is configured to run every night.  The schedule can be modified as per your requirement.  The result of evaluation are stored in dataintegrity_result table and it is re-populated on every execution.
* Once installed a Data Quality app is provided on the home page, which shows the results of the rules run and provides a way to manually run any or all rules. Please note that this method may result in an HTTP timeout for many rules. 

## Sample Java Rule

``` java
package org.openmrs.module.dataintegrity;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.DataIntegrityRule;
import org.openmrs.module.dataintegrity.rule.RuleDefinition;
import org.openmrs.module.dataintegrity.rule.RuleResult;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class InvalidDateOfDeath implements RuleDefinition<Patient> {

	@Override
	public List<RuleResult<Patient>> evaluate() {
		Criteria criteria = getSession().createCriteria(Patient.class, "patient");
		criteria.add(Restrictions.isNotNull("deathDate"));
		criteria.add(Restrictions.eq("voided", false));
		criteria.add(Restrictions.gt("deathDate", new Date()));
		
		List<Patient> patientList = criteria.list();
		return patientToRuleResultTransformer(patientList);
	}

	private List<RuleResult<Patient>> patientToRuleResultTransformer(List<Patient> patients) {
        List<RuleResult<Patient>> ruleResults = new ArrayList<>();
        for (Patient patient : patients) {
            RuleResult<Patient> ruleResult = new RuleResult<>();
            ruleResult.setActionUrl("");
            ruleResult.setNotes("Patient with invalid date");
            ruleResult.setEntity(patient);
            ruleResults.add(ruleResult);
        }
		return ruleResults;
    }
	
	public DataIntegrityRule getRule() {
		DataIntegrityRule rule = new DataIntegrityRule();
		rule.setRuleCategory("patient");
		rule.setHandlerConfig("java");
		rule.setHandlerClassname(getClass().getName());
		rule.setRuleName("Invalid Date of Death");
		rule.setUuid("ed46f4cb-fac6-4c2f-9eea-7355b64755e3");
		return rule;
	}
	
	private Session getSession() {
		return Context.getRegisteredComponent("sessionFactory", SessionFactory.class).getCurrentSession();
}

```
## Groovy Rules
* Groovy rules needs to be available in <openmrs_app_data_directory>/dataQualityRules/<ruleName>.groovy.  The dataintegrity_rule should contain the handler_config='groovy' and handler_classname='<rulename>.groovy'

## GSoC'17
This project is selected in Google Summer of Code - 2017. More details can be found in the [Project wiki page](https://wiki.openmrs.org/display/projects/Data+Integrity+Module+4.x+Improvement+Project).

## Development
OpenMRS uses JIRA for tracking issues and progress of different project. Pick any introductory ticket from [this page](https://issues.openmrs.org/browse/DINT/). Please check out the [OpenMRS Pull Request Tips](https://wiki.openmrs.org/display/docs/Pull+Request+Tips).
### Code Style
Please follow the coding convention used by OpenMRS. They are listed [here](https://wiki.openmrs.org/display/docs/Conventions).

## License
This project is licensed under the OpenMRS Public License, see the [license file](https://github.com/openmrs/openmrs-module-dataintegrity/blob/master/license.txt) for details.

## Resources
* JIRA: https://issues.openmrs.org/browse/DINT/
* Wiki: https://wiki.openmrs.org/display/docs/Data+Integrity+Module
* Demo: http://aijar.mets.or.ug:8080/ugandaemr/index.htm
* Travis CI: https://travis-ci.org/openmrs/openmrs-module-dataintegrity/
* Example Module: https://github.com/shivtej1505/lite

