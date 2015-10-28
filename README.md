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
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.dataintegrity.rule.RuleDefinition;
import org.openmrs.module.dataintegrity.rule.RuleResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvalidDateOfDeath implements RuleDefinition<Patient> {

	@Override
	public List<RuleResult<Patient>> evaluate() {
		SessionFactory sessionFactory = Context.getRegisteredComponent("sessionFactory",SessionFactory.class);

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Patient.class,"patient")
				.add(Restrictions.isNotNull("deathDate"))
				.add(Restrictions.eq("voided",false))
				.add(Restrictions.gt("deathDate",new Date()));

		List<Patient> patientList = criteria.list();

		return patientToRuleResultTransformer(patientList);
	}

	private List<RuleResult<Patient>> patientToRuleResultTransformer(List<Patient> patients){

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
	
	public DataIntegrityRule getRule() { return null; }
}

```
## Groovy Rules
* Groovy rules needs to be available in <openmrs_app_data_directory>/dataQualityRules/<ruleName>.groovy.  The dataintegrity_rule should contain the handler_config='groovy' and handler_classname='<rulename>.groovy'
