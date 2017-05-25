package org.sonar.plugins.tsql.sensors;

import org.sonar.api.config.Settings;
import org.sonar.plugins.tsql.rules.definitions.MsRulesDefinition;
import org.sonar.plugins.tsql.rules.issues.MsIssuesProvider;

public class MsIssuesLoaderSensor extends BaseTsqlSensor {

	public MsIssuesLoaderSensor(final Settings settings) {
		super(settings, new MsIssuesProvider(settings), MsRulesDefinition.getRepositoryKeyForLanguage());
	}
}
