package org.sonar.plugins.tsql.rules;

import static java.lang.String.format;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.config.Settings;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class TsqlMsIssuesLoaderSensor implements org.sonar.api.batch.sensor.Sensor {

	private static final Logger LOGGER = Loggers.get(TsqlMsIssuesLoaderSensor.class);

	protected static final String REPORT_PATH_KEY = "sonar.tsql.ms.reportPath";

	protected static final String REPORT_FILE = "sonar.tsql.ms.report";

	protected final Settings settings;

	protected final FileSystem fileSystem;

	private final IIssuesParser<TsqlIssue> parser = new VsSqlIssuesParser();

	/**
	 * Use of IoC to get Settings, FileSystem, RuleFinder and
	 * ResourcePerspectives
	 */
	public TsqlMsIssuesLoaderSensor(final Settings settings, final FileSystem fileSystem) {
		this.settings = settings;
		this.fileSystem = fileSystem;
	}

	protected List<File> getReports(org.sonar.api.batch.sensor.SensorContext context) {
		String dir = fileSystem.baseDir().getAbsolutePath();
		String ending = "staticcodeanalysis.results.xml";
		String reportPath = settings.getString(REPORT_PATH_KEY);
		if (!StringUtils.isEmpty(reportPath)) {
			dir = reportPath;
		}
		String reportEnd = settings.getString(REPORT_FILE);
		if (!StringUtils.isEmpty(reportEnd)) {
			ending = reportEnd.toLowerCase();
		}
		LOGGER.debug("Sarching directory: "+dir+" for analysis files with: "+ending);
		List<File> result = new MsSqlReportsProvider(ending).get(dir);
		return result;

	}

	private void add(TsqlIssue error, org.sonar.api.batch.sensor.SensorContext context) {

		InputFile file = fileSystem.inputFile(fileSystem.predicates().and(error.getPredicate()));
		if (file == null) {
			LOGGER.warn(format("Cound not find file %s to add issue %s at line %d.", error.getFilePath(),
					error.getType(), error.getLine()));
			return;
		}
		try {
			RuleKey rule = RuleKey.of(TsqlMsRulesDefinition.getRepositoryKeyForLanguage(file.language()),
					error.getType());
			NewIssue issue = context.newIssue();

			NewIssueLocation loc = issue.newLocation().on(file).at(file.selectLine(error.getLine()))
					.message(error.getDescription());
			issue.at(loc).forRule(rule);
			issue.save();
			LOGGER.debug(format("Added issue %s on file %s at line %d.", error.getType(), file.absolutePath(),
					error.getLine()));
		} catch (Throwable e) {
			LOGGER.error(format("Can't add issue %s on file %s at line %d.", error.getType(), file.absolutePath(),
					error.getLine()), e);
		}
	}

	@Override
	public String toString() {
		return "MsSQLIssuesLoaderSensor";
	}

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.name(this.getClass().getSimpleName());
	}

	@Override
	public void execute(org.sonar.api.batch.sensor.SensorContext context) {

		for (File reportPath : getReports(context)) {
			TsqlIssue[] errors = this.parser.parse(reportPath);
			LOGGER.info(format("Found total %d issues at %s.", errors.length, reportPath));
			for (TsqlIssue error : errors) {
				add(error, context);
			}
		}

	}
}
