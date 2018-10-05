package org.sonar.plugins.tsql.helpers;

import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.rules.definitions.CustomPluginChecks;

public class CustomRulesPrinter {

	public static void main(String[] args) {
		final CustomPluginChecks provider = new CustomPluginChecks();
		final SqlRules rules = provider.getRules();

		for (Rule r : rules.getRule()) {
			for (String c : r.getRuleImplementation().getCompliantRulesCodeExamples().getRuleCodeExample()) {
				System.out.println(String.format("-- OK: %s", r.getKey()));
				System.out.println(String.format("%s", c));
			}
			for (String c : r.getRuleImplementation().getViolatingRulesCodeExamples().getRuleCodeExample()) {
				System.out.println(String.format("-- KO: %s", r.getKey()));
				System.out.println(String.format("%s", c));
			}
		}
	}

}
