package org.sonar.plugins.tsql.sensors.antlr4;

import org.antlr.v4.runtime.CommonTokenStream;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.rules.issues.DefaultIssuesFiller;
import org.sonar.plugins.tsql.rules.issues.IIssuesFiller;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.sensors.custom.CustomIssuesProvider;

public class AntlrCustomRulesSensor implements IAntlrSensor {
	private static final Logger LOGGER = Loggers.get(AntlrCustomRulesSensor.class);

	private SqlRules[] rules;

	private final IIssuesFiller filler = new DefaultIssuesFiller();
	private final CustomIssuesProvider provider = new CustomIssuesProvider();

	public AntlrCustomRulesSensor(SqlRules[] rules) {
		this.rules = rules;
	}

	@Override
	public void work(final SensorContext context, final CommonTokenStream stream, final InputFile file) {
		try {

			final TsqlIssue[] finalIssues = provider.getIssues(stream, rules);

			filler.fill(context, file, finalIssues);
		} catch (final Throwable e) {
			e.printStackTrace();
			LOGGER.warn("Unexpected error while parsing issues for: " + file.absolutePath());
		}
	}

}
