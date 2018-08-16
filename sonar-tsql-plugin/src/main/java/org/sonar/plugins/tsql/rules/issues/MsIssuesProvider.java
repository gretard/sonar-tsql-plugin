package org.sonar.plugins.tsql.rules.issues;

import static java.lang.String.format;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.rules.files.IReportsProvider;
import org.sonar.plugins.tsql.rules.files.MsIssuesFilesProvider;
import org.sonar.plugins.tsql.rules.parsers.IIssuesParser;
import org.sonar.plugins.tsql.rules.parsers.MsIssuesParser;

public class MsIssuesProvider implements IIssuesProvider {

	private final IIssuesParser<TsqlIssue> issuesParser = new MsIssuesParser();
	private final IReportsProvider reportsProvider;

	private static final Logger LOGGER = Loggers.get(MsIssuesProvider.class);

	public MsIssuesProvider(final Settings settings) {
		this.reportsProvider = new MsIssuesFilesProvider(settings);
	}

	@Override
	public TsqlIssue[] getIssues(String baseDir) {
		final List<TsqlIssue> foundIssues = new ArrayList<TsqlIssue>();
		for (final File reportPath : this.reportsProvider.get(baseDir)) {
			final TsqlIssue[] errors = this.issuesParser.parse(reportPath);
			LOGGER.debug(format("Found total %d issues at %s.", errors.length, reportPath));
			foundIssues.addAll(Arrays.asList(errors));
		}
		return foundIssues.toArray(new TsqlIssue[0]);
	}

}
