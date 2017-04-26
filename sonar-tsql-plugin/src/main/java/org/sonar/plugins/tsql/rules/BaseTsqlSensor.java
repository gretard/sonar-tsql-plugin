package org.sonar.plugins.tsql.rules;

import static java.lang.String.format;

import java.io.File;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.config.Settings;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.rules.files.IReporsProvider;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.rules.parsers.IIssuesParser;

public abstract class BaseTsqlSensor implements org.sonar.api.batch.sensor.Sensor {

	private static final Logger LOGGER = Loggers.get(BaseTsqlSensor.class);

	protected final Settings settings;

	protected final FileSystem fileSystem;

	protected final IIssuesParser<TsqlIssue> parser;

	private final IReporsProvider reportsProvider;

	private final String repositoryKey;

	public BaseTsqlSensor(final Settings settings, final FileSystem fileSystem, final IIssuesParser<TsqlIssue> parser,
			final IReporsProvider reportsProvider, final String repositoryKey) {

		this.settings = settings;
		this.parser = parser;
		this.fileSystem = fileSystem;
		this.reportsProvider = reportsProvider;
		this.repositoryKey = repositoryKey;

	}

	protected void add(final TsqlIssue error, final org.sonar.api.batch.sensor.SensorContext context) {

		final InputFile file = fileSystem.inputFile(fileSystem.predicates().and(error.getPredicate()));

		if (file == null) {
			LOGGER.debug(format("Cound not find file %s to add issue %s at line %d.", error.getFilePath(),
					error.getType(), error.getLine()));
			return;
		}

		try {
			final RuleKey rule = RuleKey.of(repositoryKey, error.getType());
			final NewIssue issue = context.newIssue();

			final NewIssueLocation loc = issue.newLocation().on(file).at(file.selectLine(error.getLine()))
					.message(error.getDescription());
			issue.at(loc).forRule(rule);
			issue.save();
		} catch (Throwable e) {
			LOGGER.warn(format("Can't add issue %s on file %s at line %d.", error.getType(), file.absolutePath(),
					error.getLine()), e);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void execute(final org.sonar.api.batch.sensor.SensorContext context) {

		final boolean skipAnalysis = this.settings.getBoolean(Constants.PLUGIN_SKIP);

		if (skipAnalysis) {
			LOGGER.debug(format("Skipping plugin"));
			return;
		}

		for (final File reportPath : this.reportsProvider.get()) {
			final TsqlIssue[] errors = this.parser.parse(reportPath);
			LOGGER.debug(format("Found total %d issues at %s.", errors.length, reportPath));
			for (final TsqlIssue error : errors) {
				add(error, context);
			}
		}

	}

	@Override
	public void describe(final SensorDescriptor descriptor) {
		descriptor.name(this.getClass().getSimpleName());
	}

}
