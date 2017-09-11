package org.sonar.plugins.tsql.sensors;

import static java.lang.String.format;

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
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.rules.issues.IIssuesProvider;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public abstract class BaseTsqlSensor implements org.sonar.api.batch.sensor.Sensor {

	private static final Logger LOGGER = Loggers.get(BaseTsqlSensor.class);

	protected final Settings settings;

	protected final IIssuesProvider issuesProvider;

	private final String repositoryKey;

	public BaseTsqlSensor(final Settings settings, final IIssuesProvider issuesProvider, final String repositoryKey) {

		this.settings = settings;
		this.issuesProvider = issuesProvider;
		this.repositoryKey = repositoryKey;

	}

	protected void add(final TsqlIssue error, final org.sonar.api.batch.sensor.SensorContext context) {

		final FileSystem fileSystem = context.fileSystem();
		final InputFile file = fileSystem.inputFile(fileSystem.predicates().and(error.getPredicate()));

		if (file == null) {
			LOGGER.debug(format("Cound not find file %s to add issue %s at line %d.", error.getFilePath(),
					error.getType(), error.getLine()));
			return;
		}
		if (error.getLine() < 1) {
			LOGGER.warn(format("Can't add issue %s on file %s as line is 0", error.getType(), error.getFilePath()));
			return;
		}

		try {

			final RuleKey rule = RuleKey.of(repositoryKey, error.getType());
			final NewIssue issue = context.newIssue().forRule(rule);

			final NewIssueLocation loc = issue.newLocation().on(file).at(file.selectLine(error.getLine()));
			if (error.getDescription() != null) {
				loc.message(error.getDescription());
			}

			issue.at(loc).save();
		} catch (final Throwable e) {
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
			LOGGER.debug(format("Skipping plugin as skip flag is set"));
			return;
		}
		final String baseDir = context.fileSystem().baseDir().getAbsolutePath();

		final TsqlIssue[] issues = this.issuesProvider.getIssues(baseDir);
		LOGGER.info(format("Found %d issues", issues.length));
		for (final TsqlIssue issue : issues) {
			add(issue, context);
		}
	}

	@Override
	public void describe(final SensorDescriptor descriptor) {
		descriptor.name(this.getClass().getSimpleName()).onlyOnLanguage(TSQLLanguage.KEY);
	}

}
