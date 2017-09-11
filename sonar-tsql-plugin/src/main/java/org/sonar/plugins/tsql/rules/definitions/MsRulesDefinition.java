package org.sonar.plugins.tsql.rules.definitions;

import org.sonar.plugins.tsql.Constants;

public final class MsRulesDefinition extends BaseRulesDefinition {

	public MsRulesDefinition() {
		super(Constants.MS_REPO_KEY, Constants.MS_REPO_NAME, Constants.MS_RULES_FILE);
	}

}
