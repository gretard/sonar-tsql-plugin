package org.sonar.plugins.tsql.sensors.custom;

import org.sonar.plugins.tsql.rules.custom.RuleImplementation;

public interface IRulesChecker {
	boolean check(ParsedNode[] nodes, RuleImplementation rule, Direction dir);
}
