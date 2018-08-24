package org.sonar.plugins.tsql.rules.issues;

import static java.lang.String.format;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.rules.files.FilesProvider;
import org.sonar.plugins.tsql.rules.parsers.IIssuesParser;
import org.sonar.plugins.tsql.rules.parsers.MsIssuesParser;

public class MsIssuesProvider implements IIssuesProvider {

	private final IIssuesParser<TsqlIssue> issuesParser = new MsIssuesParser();

	private static final Logger LOGGER = Loggers.get(MsIssuesProvider.class);
	private final FilesProvider filesProvider = new FilesProvider();
	private final Settings settings;

	public MsIssuesProvider(final Settings settings) {
		this.settings = settings;
	}

	@Override
	public TsqlIssue[] getIssues(String baseDir) {
		final List<TsqlIssue> foundIssues = new ArrayList<TsqlIssue>();
		File[] files = filesProvider.getFiles(Constants.MS_REPORT_FILE_DEFAULT_VALUE,
				settings.getString(Constants.MS_REPORT_FILE), baseDir);
		for (final File reportPath : files) {
			final TsqlIssue[] errors = this.issuesParser.parse(reportPath);
			LOGGER.debug(format("Found total %d issues at %s.", errors.length, reportPath));
			foundIssues.addAll(Arrays.asList(errors));
		}
		return foundIssues.toArray(new TsqlIssue[0]);
	}

}
