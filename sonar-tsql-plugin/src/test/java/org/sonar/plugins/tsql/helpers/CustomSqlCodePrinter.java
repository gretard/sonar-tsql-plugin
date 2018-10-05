package org.sonar.plugins.tsql.helpers;

import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.rules.definitions.CustomPluginChecks;

public class CustomSqlCodePrinter {

	public static void main(String[] args) {
		final CustomPluginChecks provider = new CustomPluginChecks();
		final SqlRules rules = provider.getRules();
		System.out.println("# Rules #");
		System.out.println();
		System.out.println(String.format("Plugin supports the following %s rules:", rules.getRule().size()));
		System.out.println();
		for (Rule r : rules.getRule()) {
			System.out.println(String.format("- %s - %s", r.getKey(), r.getName()));
		}
		System.out.println();
		for (Rule r : rules.getRule()) {
			System.out.println(String.format("## %s - %s ##", r.getKey(), r.getName()));
			System.out.println(r.getDescription().replace("<h2>Description</h2>", ""));
			System.out.println();
			if (!r.getRuleImplementation().getCompliantRulesCodeExamples().getRuleCodeExample().isEmpty()) {
				System.out.println("### Compliant examples ###");
				System.out.println();
				for (String c : r.getRuleImplementation().getCompliantRulesCodeExamples().getRuleCodeExample()) {
					System.out.println(String.format("`%s`\r\n", c));
				}
			}

			if (!r.getRuleImplementation().getViolatingRulesCodeExamples().getRuleCodeExample().isEmpty()) {
				System.out.println("### Non-compliant examples ###");
				System.out.println();
				for (String c : r.getRuleImplementation().getViolatingRulesCodeExamples().getRuleCodeExample()) {
					System.out.println(String.format("`%s`\r\n", c));
				}
			}
		}

	}

}
