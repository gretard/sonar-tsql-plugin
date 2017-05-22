package org.sonar.plugins.tsql.sensors;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.plugins.tsql.rules.definitions.MsRulesDefinition;
import org.sonar.plugins.tsql.rules.issues.MsIssuesProvider;

public class MsIssuesLoaderSensor extends BaseTsqlSensor {

	public MsIssuesLoaderSensor(final Settings settings, final FileSystem fileSystem) {
		super(settings, fileSystem, new MsIssuesProvider(settings, fileSystem),
				MsRulesDefinition.getRepositoryKeyForLanguage());
	}
}
