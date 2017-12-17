package org.sonar.plugins.tsql.sensors.antlr4;

import java.util.ArrayList;
import java.util.List;

import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.SqlRules;

public class RulesHelper {
	public static CandidateRule[] convert(SqlRules... rules) {
		final List<CandidateRule> convertedRules = new ArrayList<>();
		for (SqlRules r : rules) {
			for (Rule rule : r.getRule()) {
				convertedRules.add(new CandidateRule(r.getRepoKey(), rule));
			}
		}
		return convertedRules.toArray(new CandidateRule[0]);
	}
}
