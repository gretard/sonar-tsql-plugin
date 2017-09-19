package org.sonar.plugins.tsql.sensors.custom;

import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public interface IViolationsProvider {
	public TsqlIssue[] getIssues(ParsedNode... nodes);
}
