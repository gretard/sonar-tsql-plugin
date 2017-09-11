package org.sonar.plugins.tsql.rules.definitions;

import org.sonar.plugins.tsql.Constants;

public final class CodeGuardRulesDefinition extends BaseRulesDefinition {

	public CodeGuardRulesDefinition() {
		super(Constants.CG_REPO_KEY, Constants.CG_REPO_NAME, Constants.CG_RULES_FILE);
	}

}
