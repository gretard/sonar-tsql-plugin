package org.sonar.plugins.tsql.rules.files;

import java.io.File;

import org.sonar.api.config.Settings;
import org.sonar.plugins.tsql.Constants;

public class MsIssuesFilesProvider implements IReporsProvider {

	private final IReporsProvider reportsProvider;

	public MsIssuesFilesProvider(final Settings settings) {
		final String reportEnd = settings.getString(Constants.MS_REPORT_FILE);
		this.reportsProvider = new BaseReportsProvider(reportEnd);
	}

	@Override
	public File[] get(String baseDir) {
		return this.reportsProvider.get(baseDir);
	}

}
