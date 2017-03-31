package org.sonar.plugins.tsql.rules.files;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

public class MsIssuesFilesProvider implements IReporsProvider {

	protected static final String REPORT_PATH_KEY = "sonar.tsql.ms.reportPath";

	protected static final String REPORT_FILE = "sonar.tsql.ms.report";

	private final IReporsProvider reportsProvider;

	public MsIssuesFilesProvider(final Settings settings, final FileSystem fileSystem) {
		final String reportPath = settings.getString(REPORT_PATH_KEY);
		final String reportEnd = settings.getString(REPORT_FILE);
		
		String dir = fileSystem.baseDir().getAbsolutePath();
		String ending = "staticcodeanalysis.results.xml";
		if (!StringUtils.isEmpty(reportPath)) {
			dir = reportPath;
		}
		
		if (!StringUtils.isEmpty(reportEnd)) {
			ending = reportEnd.toLowerCase();
		}
		this.reportsProvider = new BaseReportsProvider(dir, ending);
	}

	@Override
	public File[] get() {
		return this.reportsProvider.get();
	}

}
