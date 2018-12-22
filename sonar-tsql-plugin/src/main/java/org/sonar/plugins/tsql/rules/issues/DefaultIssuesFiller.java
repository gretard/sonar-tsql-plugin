package org.sonar.plugins.tsql.rules.issues;

import static java.lang.String.format;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewExternalIssue;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rules.RuleType;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class DefaultIssuesFiller implements IIssuesFiller {
	private static final Logger LOGGER = Loggers.get(DefaultIssuesFiller.class);

	@Override
	public void fill(final SensorContext context, final InputFile inputFile, final TsqlIssue... issues) {

		for (final TsqlIssue tsqlIssue : issues) {
			InputFile file = inputFile;
			if (tsqlIssue == null) {
				continue;
			}
			try {
				if (tsqlIssue.getLine() < 1) {
					LOGGER.warn(format("Can't add issue %s on file %s as line is 0", tsqlIssue.getType(),
							tsqlIssue.getFilePath()));
					continue;
				}

				if (file == null) {
					final FileSystem fileSystem = context.fileSystem();
					file = fileSystem.inputFile(fileSystem.predicates().and(tsqlIssue.getPredicate()));

					if (file == null) {
						LOGGER.debug(format("Cound not find file %s to add issue %s at line %d.",
								tsqlIssue.getFilePath(), tsqlIssue.getType(), tsqlIssue.getLine()));
						continue;
					}
				}
				final RuleKey rule = RuleKey.of(tsqlIssue.getRepositoryKey(), tsqlIssue.getType());

				if (tsqlIssue.isExternal()) {
					final NewExternalIssue externalIssue = context.newExternalIssue().engineId(tsqlIssue.getRepositoryKey())
							.ruleId(tsqlIssue.getType()).severity(Severity.INFO).type(RuleType.CODE_SMELL);
					final NewIssueLocation loc = externalIssue.newLocation().on(file)
							.at(file.selectLine(tsqlIssue.getLine()));
					if (tsqlIssue.getDescription() != null) {
						loc.message(tsqlIssue.getDescription());
					}
					externalIssue.at(loc).save();
					continue;
				}

				final NewIssue issue = context.newIssue().forRule(rule);
				final NewIssueLocation loc = issue.newLocation().on(file).at(file.selectLine(tsqlIssue.getLine()));
				if (tsqlIssue.getDescription() != null) {
					loc.message(tsqlIssue.getDescription());
				}

				issue.at(loc).save();
			} catch (final Throwable e) {
				LOGGER.warn(format("Can't add issue %s on file %s at line %d.", tsqlIssue.getType(), file,
						tsqlIssue.getLine()), e);
			}
		}
	}

}
