package org.sonar.plugins.tsql.sensors.antlr4;

import org.antlr.v4.runtime.CommonTokenStream;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.rules.issues.DefaultIssuesFiller;
import org.sonar.plugins.tsql.rules.issues.IIssuesFiller;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.sensors.custom.CustomIssuesProvider;

public class AntlrCustomRulesSensor implements IAntlrSensor {
	private static final Logger LOGGER = Loggers.get(AntlrCustomRulesSensor.class);

	private final IIssuesFiller filler = new DefaultIssuesFiller();
	private final CustomIssuesProvider provider;

	public AntlrCustomRulesSensor(CandidateRule[] rules) {
		this.provider = new CustomIssuesProvider(rules);
	}

	@Override
	public void work(final SensorContext context, final CommonTokenStream stream, final InputFile file) {
		try {

			final TsqlIssue[] finalIssues = provider.getIssues(stream);

			filler.fill(context, file, finalIssues);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.info(String.format("Finished analyzing %s file with  %s issues found", file.absolutePath(),
						finalIssues.length));
			}
		} catch (final Throwable e) {
			LOGGER.warn("Unexpected error while parsing issues for: " + file.absolutePath(), e);
		}
	}

}
