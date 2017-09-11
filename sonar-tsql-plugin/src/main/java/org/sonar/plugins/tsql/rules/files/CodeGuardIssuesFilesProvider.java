package org.sonar.plugins.tsql.rules.files;

import java.io.File;

import org.sonar.api.config.Settings;
import org.sonar.plugins.tsql.Constants;

public class CodeGuardIssuesFilesProvider implements IReporsProvider {

	private final IReporsProvider reportsProvider;

	public CodeGuardIssuesFilesProvider(final Settings settings) {

		final String reportEnd = settings.getString(Constants.CG_REPORT_FILE);
		this.reportsProvider = new BaseReportsProvider(reportEnd);
	}

	@Override
	public File[] get(String baseDir) {
		return this.reportsProvider.get(baseDir);
	}

}
