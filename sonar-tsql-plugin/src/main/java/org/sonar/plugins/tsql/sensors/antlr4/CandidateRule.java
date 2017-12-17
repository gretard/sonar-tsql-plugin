package org.sonar.plugins.tsql.sensors.antlr4;

import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;

public class CandidateRule {
	private final String key;
	private final Rule rule;

	public String getKey() {
		return key;
	}

	public Rule getRule() {
		return rule;
	}

	public CandidateRule(final String key, final Rule rule) {
		this.key = key;
		this.rule = rule;
	}

	public RuleImplementation getRuleImplementation() {
		return this.rule.getRuleImplementation();
	}
}
