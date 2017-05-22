package org.sonar.plugins.tsql.rules.issues;

public interface IIssuesProvider {
	TsqlIssue[] getIssues();
}
