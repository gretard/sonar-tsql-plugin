package org.sonar.plugins.tsql.sensors;

import static java.lang.String.format;

import org.sonar.plugins.tsql.rules.issues.DefaultIssuesFiller;
import org.sonar.plugins.tsql.rules.issues.IIssuesFiller;
import org.sonar.plugins.tsql.rules.issues.IIssuesProvider;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public abstract class BaseTsqlExternalSensor extends BaseTsqlSensor {

	private final IIssuesProvider issuesProvider;
	private final String repositoryKey;
	private final IIssuesFiller filler = new DefaultIssuesFiller();

	public BaseTsqlExternalSensor(final IIssuesProvider issuesProvider, final String sensorName,
			final String repositoryKey) {
		super(sensorName);
		this.issuesProvider = issuesProvider;
		this.repositoryKey = repositoryKey;

	}

	protected void innerExecute(final org.sonar.api.batch.sensor.SensorContext context) {

		final String baseDir = context.fileSystem().baseDir().getAbsolutePath();

		final TsqlIssue[] issues = this.issuesProvider.getIssues(baseDir);

		for (final TsqlIssue i : issues) {
			i.setRepositoryKey(this.repositoryKey);
		}
		LOGGER.info(format("Found %d issues", issues.length));

		filler.fill(context, null, issues);

	}

}
