package org.sonar.plugins.tsql.rules.definitions;

import org.sonar.plugins.tsql.Constants;

public final class CustomPluginRulesDefinition extends BaseRulesDefinition {

	public CustomPluginRulesDefinition() {
		super(Constants.PLUGIN_REPO_KEY, Constants.PLUGIN_REPO_NAME, Constants.PLUGIN_RULES_FILE);
	}

}
