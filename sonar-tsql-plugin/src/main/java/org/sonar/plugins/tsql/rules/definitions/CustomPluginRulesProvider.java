package org.sonar.plugins.tsql.rules.definitions;

import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;

public class CustomPluginRulesProvider {

	public SqlRules getRules() {

		return Antlr4Utils.getCustomMainRules();
	}
}
