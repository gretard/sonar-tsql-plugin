package org.sonar.plugins.tsql.rules.issues;

import static java.lang.String.format;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.TempFolder;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.rules.files.CodeGuardExecutingReportsProvider;
import org.sonar.plugins.tsql.rules.files.IReporsProvider;
import org.sonar.plugins.tsql.rules.parsers.IIssuesParser;
import org.sonar.plugins.tsql.rules.parsers.CodeGuardIssuesParser;

public class CodeGuardIssuesProvider implements IIssuesProvider {

	private final IIssuesParser<TsqlIssue> issuesParser = new CodeGuardIssuesParser();
	private final IReporsProvider reportsProvider;

	private static final Logger LOGGER = Loggers.get(CodeGuardIssuesProvider.class);

	public CodeGuardIssuesProvider(final Settings settings,  final TempFolder tempFolder) {
		this.reportsProvider = new CodeGuardExecutingReportsProvider(settings, tempFolder);
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
