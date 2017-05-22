package org.sonar.plugins.tsql.sensors;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.TempFolder;
import org.sonar.plugins.tsql.rules.definitions.CodeGuardRulesDefinition;
import org.sonar.plugins.tsql.rules.issues.CodeGuardIssuesProvider;

public class CodeGuardIssuesLoaderSensor extends BaseTsqlSensor {

	public CodeGuardIssuesLoaderSensor(final Settings settings, final FileSystem fileSystem,
			final TempFolder tempFolder) {
		super(settings, fileSystem, new CodeGuardIssuesProvider(settings, fileSystem, tempFolder),
				CodeGuardRulesDefinition.getRepositoryKeyForLanguage());
	}
}
