package org.openmrs.module.dataintegrity.rule;

import java.util.List;

public interface RuleDefn<T>
{
    List<RuleResult<T>> evaluate();
}
