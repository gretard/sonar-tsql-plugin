package org.sonar.plugins.tsql.sensors.antlr4;

import java.nio.charset.Charset;

import org.antlr.tsql.TSqlLexer;
import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.antlr4.tsqlParser;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.SqlRules;
import org.sonar.plugins.tsql.rules.issues.DefaultIssuesFiller;
import org.sonar.plugins.tsql.rules.issues.IIssuesFiller;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.sensors.custom.DefaultCustomRulesViolationsProvider;
import org.sonar.plugins.tsql.sensors.custom.lines.DefaultLinesProvider;

public class AntlrCustomRulesSensor implements IAntlrSensor {
	private static final Logger LOGGER = Loggers.get(AntlrCustomRulesSensor.class);

	private SqlRules[] rules;

	private final IIssuesFiller filler = new DefaultIssuesFiller();

	public AntlrCustomRulesSensor(SqlRules[] rules) {
		this.rules = rules;
	}

	@Override
	public void work(final SensorContext context, final CommonTokenStream stream, final InputFile file) {
		try {
			
			final TSqlParser parser = new TSqlParser(stream);

			if (!LOGGER.isDebugEnabled()) {
				parser.removeErrorListeners();
			}
			final ParseTree root = parser.tsql_file();
			for (final SqlRules rule : this.rules) {
				final String repositoryKey = rule.getRepoKey();

				final DefaultCustomRulesViolationsProvider customRulesViolationsProvider = new DefaultCustomRulesViolationsProvider(
						new DefaultLinesProvider(stream), rule.getRule().toArray(new Rule[0]));
				final TsqlIssue[] issues = customRulesViolationsProvider.getIssues(root);
				filler.fill(repositoryKey, context, file, issues);

			}
		} catch (final Throwable e) {
			e.printStackTrace();
			LOGGER.warn("Unexpected error while parsing issues for: " + file.absolutePath());
		}
	}

}
