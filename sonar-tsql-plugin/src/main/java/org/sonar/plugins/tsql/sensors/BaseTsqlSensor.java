package org.sonar.plugins.tsql.sensors;

import static java.lang.String.format;

import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.rules.issues.DefaultIssuesFiller;
import org.sonar.plugins.tsql.rules.issues.IIssuesFiller;
import org.sonar.plugins.tsql.rules.issues.IIssuesProvider;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public abstract class BaseTsqlSensor implements org.sonar.api.batch.sensor.Sensor {

	private static final Logger LOGGER = Loggers.get(BaseTsqlSensor.class);

	protected final Settings settings;

	protected final IIssuesProvider issuesProvider;

	private final String repositoryKey;
	private final IIssuesFiller filler = new DefaultIssuesFiller();

	public BaseTsqlSensor(final Settings settings, final IIssuesProvider issuesProvider, final String repositoryKey) {
		this.settings = settings;

		this.issuesProvider = issuesProvider;
		this.repositoryKey = repositoryKey;

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

		filler.fill(this.repositoryKey, context, null, issues);

	}

	@Override
	public void describe(final SensorDescriptor descriptor) {
		descriptor.name(this.getClass().getSimpleName()).onlyOnLanguage(TSQLLanguage.KEY);
	}

}
