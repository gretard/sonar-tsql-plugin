package org.sonar.plugins.tsql.sensors;

import static java.lang.String.format;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.antlr4.tsqlLexer;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.rules.custom.CustomRules;
import org.sonar.plugins.tsql.sensors.antlr4.AntlrCpdTokenizer;
import org.sonar.plugins.tsql.sensors.antlr4.AntlrCustomRulesSensor;
import org.sonar.plugins.tsql.sensors.antlr4.AntlrHighlighter;
import org.sonar.plugins.tsql.sensors.antlr4.IAntlrSensor;
import org.sonar.plugins.tsql.sensors.custom.CustomRulesProvider;

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
		final CustomRules[] rules = provider.getRules(context.fileSystem().baseDir().getAbsolutePath(), settings)
				.values().toArray(new CustomRules[0]);
		final IAntlrSensor[] sensors = new IAntlrSensor[] { new AntlrCpdTokenizer(), new AntlrHighlighter(),
				new AntlrCustomRulesSensor(rules) };

		final FileSystem fs = context.fileSystem();
		final Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage(TSQLLanguage.KEY));

		for (final InputFile file : files) {
			try {
				final CharStream charStream = CharStreams.fromFileName(file.absolutePath());
				final tsqlLexer lexer = new tsqlLexer(charStream);
				if (!LOGGER.isDebugEnabled()) {
					lexer.removeErrorListeners();
				}
				final CommonTokenStream stream = new CommonTokenStream(lexer);
				stream.fill();
				for (final IAntlrSensor sensor : sensors) {
					sensor.work(context, stream, file);
				}
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
