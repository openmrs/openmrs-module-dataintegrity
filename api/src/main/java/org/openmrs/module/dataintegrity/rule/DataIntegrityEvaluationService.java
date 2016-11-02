package org.openmrs.module.dataintegrity.rule;

public interface DataIntegrityEvaluationService<T> {
    void fireRules();
    void fireRule(String uuid);
}
