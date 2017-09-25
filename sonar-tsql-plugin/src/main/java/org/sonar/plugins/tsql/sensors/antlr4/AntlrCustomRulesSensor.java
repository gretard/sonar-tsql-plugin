package org.sonar.plugins.tsql.sensors.antlr4;

import org.antlr.v4.runtime.CommonTokenStream;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.antlr4.tsqlParser;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Tsql_fileContext;
import org.sonar.plugins.tsql.rules.custom.SqlRules;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.sensors.custom.CustomRulesViolationsProvider;
import org.sonar.plugins.tsql.sensors.custom.ICustomRulesViolationsProvider;

public class AntlrCustomRulesSensor implements IAntlrSensor {
	private static final Logger LOGGER = Loggers.get(AntlrCustomRulesSensor.class);

	private SqlRules[] rules;

	public AntlrCustomRulesSensor(SqlRules[] rules) {
		this.rules = rules;
	}

	@Override
	public void work(final SensorContext context, final CommonTokenStream stream, final InputFile file) {
		try {
			final ICustomRulesViolationsProvider customRulesViolationsProvider = new CustomRulesViolationsProvider(
					stream);
			final tsqlParser parser = new tsqlParser(stream);
			if (!LOGGER.isDebugEnabled()) {
				parser.removeErrorListeners();
			}
			final Tsql_fileContext ct = parser.tsql_file();
			for (final SqlRules rule : this.rules) {
				try {
					final TsqlIssue[] issues = customRulesViolationsProvider.getIssues(ct, this.rules);

					for (final TsqlIssue issue : issues) {

						final RuleKey srule = RuleKey.of(rule.getRepoKey(), issue.getType());
						final NewIssue newIssue = context.newIssue().forRule(srule);

						final NewIssueLocation loc = newIssue.newLocation().on(file)
								.at(file.selectLine(issue.getLine()));
						if (issue.getDescription() != null) {
							loc.message(issue.getDescription());
						}
						newIssue.at(loc).save();
					}
				} catch (final Throwable e) {
					LOGGER.warn(String.format("Unexpected error while reading/adding issues for: %s file for %s repo",
							file.absolutePath(), rule.getRepoName()), e);
				}
			}
		} catch (Throwable e) {
			LOGGER.warn("Unexpected error while parsing issues for: " + file.absolutePath());
		}
	}

}
