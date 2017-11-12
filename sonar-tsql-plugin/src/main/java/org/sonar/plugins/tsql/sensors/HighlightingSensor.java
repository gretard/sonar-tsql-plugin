package org.sonar.plugins.tsql.sensors;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import org.antlr.tsql.TSqlLexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.FileUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.rules.custom.SqlRules;
import org.sonar.plugins.tsql.rules.definitions.CustomPluginRulesProvider;
import org.sonar.plugins.tsql.rules.definitions.CustomRulesProvider;
import org.sonar.plugins.tsql.sensors.antlr4.AntlrCpdTokenizer;
import org.sonar.plugins.tsql.sensors.antlr4.AntlrCustomRulesSensor;
import org.sonar.plugins.tsql.sensors.antlr4.AntlrHighlighter;
import org.sonar.plugins.tsql.sensors.antlr4.IAntlrSensor;

public class HighlightingSensor implements org.sonar.api.batch.sensor.Sensor {

	private static final Logger LOGGER = Loggers.get(HighlightingSensor.class);
	protected final Settings settings;
	private final CustomRulesProvider provider = new CustomRulesProvider();

	public HighlightingSensor(final Settings settings) {
		this.settings = settings;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void execute(final org.sonar.api.batch.sensor.SensorContext context) {
		final boolean skipAnalysis = this.settings.getBoolean(Constants.PLUGIN_SKIP);

		if (skipAnalysis) {
			LOGGER.debug("Skipping plugin as skip flag is set");
			return;
		}
		final boolean skipCustomRules = this.settings.getBoolean(Constants.PLUGIN_SKIP_CUSTOM_RULES);

		final String[] paths = settings.getStringArray(Constants.PLUGIN_CUSTOM_RULES_PATH);
		final String rulesPrefix = settings.getString(Constants.PLUGIN_CUSTOM_RULES_PREFIX);
		final List<SqlRules> rules = new ArrayList<>();
		if (!skipCustomRules) {
			final SqlRules customRules = new CustomPluginRulesProvider().getRules();
			if (customRules != null) {
				rules.add(customRules);
			}
		}

		rules.addAll(provider.getRules(context.fileSystem().baseDir().getAbsolutePath(), rulesPrefix, paths).values());
		LOGGER.info(String.format("Total %s custom rules repositories", rules.size()));
		SqlRules[] finalRules = rules.toArray(new SqlRules[0]);
		final IAntlrSensor[] sensors = new IAntlrSensor[] { new AntlrCpdTokenizer(), new AntlrHighlighter(),
				new AntlrCustomRulesSensor(finalRules) };

		final FileSystem fs = context.fileSystem();
		final Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage(TSQLLanguage.KEY));

		for (final InputFile file : files) {
			try {
				@SuppressWarnings("deprecation")
				final CharStream charStream = CharStreams
						.fromString(FileUtils.readFileToString(file.file()).toUpperCase());

				final TSqlLexer lexer = new TSqlLexer(charStream);

				if (!LOGGER.isDebugEnabled()) {
					lexer.removeErrorListeners();
				}

				final CommonTokenStream stream = new CommonTokenStream(lexer);

				stream.fill();

				for (final IAntlrSensor sensor : sensors) {
					sensor.work(context, stream, file);
				}

				LOGGER.info(String.format("Finished working on %s file", file.absolutePath()));

			} catch (final Throwable e) {
				LOGGER.warn(format("Unexpected error parsing file %s", file.absolutePath()), e);
			}
		}
	}

	@Override
	public void describe(final SensorDescriptor descriptor) {
		descriptor.name(this.getClass().getSimpleName()).onlyOnLanguage(TSQLLanguage.KEY);
	}

}
