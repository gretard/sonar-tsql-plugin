package org.sonar.plugins.tsql.rules.issues;

import static java.lang.String.format;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class DefaultIssuesFiller implements IIssuesFiller {
	private static final Logger LOGGER = Loggers.get(DefaultIssuesFiller.class);

	@Override
	public void fill(final SensorContext context, final InputFile inputFile,
			final TsqlIssue... issues) {
	
		for (final TsqlIssue error : issues) {
			InputFile file = inputFile; 
			try {
				if (error.getLine() < 1) {
					LOGGER.warn(
							format("Can't add issue %s on file %s as line is 0", error.getType(), error.getFilePath()));
					continue;
				}
				if (file == null){
					final FileSystem fileSystem = context.fileSystem();
					file = fileSystem.inputFile(fileSystem.predicates().and(error.getPredicate()));

					if (file == null) {
						LOGGER.debug(format("Cound not find file %s to add issue %s at line %d.", error.getFilePath(),
								error.getType(), error.getLine()));
						continue;
					}
				}
				final RuleKey rule = RuleKey.of(error.getRepositoryKey(), error.getType());
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
	}

}
