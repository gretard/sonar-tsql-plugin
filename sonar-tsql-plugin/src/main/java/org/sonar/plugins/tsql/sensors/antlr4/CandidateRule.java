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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CandidateRule other = (CandidateRule) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (rule == null) {
			if (other.rule != null)
				return false;
		} else if (!rule.equals(other.rule))
			return false;
		return true;
	}

}
