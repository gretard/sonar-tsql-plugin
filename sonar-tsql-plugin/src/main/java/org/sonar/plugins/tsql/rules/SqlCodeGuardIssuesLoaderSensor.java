package org.sonar.plugins.tsql.rules;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.TempFolder;
import org.sonar.plugins.tsql.rules.definitions.CodeGuardRulesDefinition;
import org.sonar.plugins.tsql.rules.files.CgIssuesProvider;
import org.sonar.plugins.tsql.rules.parsers.SqlCodeGuardIssuesParser;

public class SqlCodeGuardIssuesLoaderSensor extends BaseTsqlSensor {

	public SqlCodeGuardIssuesLoaderSensor(final Settings settings, final FileSystem fileSystem,
			final TempFolder tempFolder) {
		super(settings, fileSystem, new SqlCodeGuardIssuesParser(),
				new CgIssuesProvider(settings, fileSystem, tempFolder), CodeGuardRulesDefinition.getRepositoryKeyForLanguage());
	}
}
