package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.CustomRules;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public interface ICustomRulesViolationsProvider {
	TsqlIssue[] getIssues(ParseTree root, CustomRules... customRules) ;
}
