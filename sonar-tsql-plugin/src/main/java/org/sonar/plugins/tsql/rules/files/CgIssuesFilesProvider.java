package org.sonar.plugins.tsql.rules.files;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

public class CgIssuesFilesProvider implements IReporsProvider {

	protected static final String REPORT_PATH_KEY = "sonar.tsql.cg.reportPath";

	protected static final String REPORT_FILE = "sonar.tsql.cg.report";

	private final IReporsProvider reportsProvider;

	public CgIssuesFilesProvider(final Settings settings, final FileSystem fileSystem) {
		final String reportPath = settings.getString(REPORT_PATH_KEY);
		final String reportEnd = settings.getString(REPORT_FILE);
		
		String dir = fileSystem.baseDir().getAbsolutePath();
		String ending = "cgresults.xml";

		if (!StringUtils.isEmpty(reportPath)) {
			dir = reportPath;
		}
		
		if (!StringUtils.isEmpty(reportEnd)) {
			ending = reportEnd.toLowerCase();
		}
		this.reportsProvider = new BaseReportsProvider(dir, ending);
	}

	@Override
	public List<File> get() {
		return this.reportsProvider.get();
	}

}
