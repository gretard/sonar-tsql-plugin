package org.sonar.plugins.tsql.rules;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.plugins.tsql.rules.definitions.TsqlMsRulesDefinition;
import org.sonar.plugins.tsql.rules.files.MsIssuesFilesProvider;
import org.sonar.plugins.tsql.rules.parsers.VsSqlIssuesParser;

public class VisualStudioIssuesLoaderSensor extends BaseTsqlSensor {

	public VisualStudioIssuesLoaderSensor(final Settings settings, final FileSystem fileSystem) {
		super(settings, fileSystem, new VsSqlIssuesParser(), new MsIssuesFilesProvider(settings, fileSystem),
				TsqlMsRulesDefinition.KEY);
	}
}
