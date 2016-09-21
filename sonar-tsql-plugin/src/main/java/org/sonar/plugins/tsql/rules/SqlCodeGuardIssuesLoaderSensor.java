package org.sonar.plugins.tsql.rules;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.plugins.tsql.rules.definitions.CodeGuardRulesDefinition;
import org.sonar.plugins.tsql.rules.files.CgIssuesFilesProvider;
import org.sonar.plugins.tsql.rules.parsers.SqlCodeGuardIssuesParser;

public class SqlCodeGuardIssuesLoaderSensor extends BaseTsqlSensor {

	public SqlCodeGuardIssuesLoaderSensor(final Settings settings, final FileSystem fileSystem) {
		super(settings, fileSystem, new SqlCodeGuardIssuesParser(), new CgIssuesFilesProvider(settings, fileSystem),
				CodeGuardRulesDefinition.KEY);
	}
}
