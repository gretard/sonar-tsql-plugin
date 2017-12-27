package org.sonar.plugins.tsql.sensors;

import static java.lang.String.format;

import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.antlr.tsql.TSqlLexer;
import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.rules.definitions.CustomPluginRulesProvider;
import org.sonar.plugins.tsql.rules.definitions.CustomRulesProvider;
import org.sonar.plugins.tsql.sensors.antlr4.AntlrCpdTokenizer;
import org.sonar.plugins.tsql.sensors.antlr4.AntlrCustomRulesSensor;
import org.sonar.plugins.tsql.sensors.antlr4.AntlrHighlighter;
import org.sonar.plugins.tsql.sensors.antlr4.AntlrMeasurer;
import org.sonar.plugins.tsql.sensors.antlr4.AntrlFile;
import org.sonar.plugins.tsql.sensors.antlr4.CandidateRule;
import org.sonar.plugins.tsql.sensors.antlr4.CaseChangingCharStream;
import org.sonar.plugins.tsql.sensors.antlr4.IAntlrSensor;
import org.sonar.plugins.tsql.sensors.antlr4.LinesProvider;
import org.sonar.plugins.tsql.sensors.antlr4.RulesHelper;

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

		final int timeout = settings.getInt(Constants.PLUGIN_CPD_TIMEOUT);
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
		final SqlRules[] finalRules = rules.toArray(new SqlRules[0]);

		final CandidateRule[] candidateRules = RulesHelper.convert(finalRules);
		LOGGER.info(String.format("Total %s custom rules repositories with total %s checks", rules.size(),
				candidateRules.length));

		final IAntlrSensor[] sensors = new IAntlrSensor[] { /*new AntlrCpdTokenizer(), */new AntlrHighlighter(),
				new AntlrCustomRulesSensor(candidateRules), new AntlrMeasurer() };

		final FileSystem fs = context.fileSystem();
		final Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage(TSQLLanguage.KEY));
		final ExecutorService executorService = Executors.newWorkStealingPool();
		final LinesProvider linesProvider = new LinesProvider();
		files.forEach(new Consumer<InputFile>() {

			@Override
			public void accept(final InputFile file) {
				executorService.execute(new Runnable() {

					@Override
					public void run() {
						try {
							long l1 = System.nanoTime();
							final Charset encoding = context.fileSystem().encoding();

							final CharStream mainStream = CharStreams.fromPath(file.path(), encoding);
							final CharStream charStream = new CaseChangingCharStream(mainStream, true);
							final TSqlLexer lexer = new TSqlLexer(charStream);

							lexer.removeErrorListeners();

							final CommonTokenStream stream = new CommonTokenStream(lexer);

							stream.fill();
							final TSqlParser parser = new TSqlParser(stream);
							parser.removeErrorListeners();
							final ParseTree root = parser.tsql_file();
							final AntrlFile antrlFile = new AntrlFile(file, stream,root,
									linesProvider.getLines(new FileInputStream(file.file()), encoding));
							
							long l2 = System.nanoTime();
							
							for (final IAntlrSensor sensor : sensors) {
								
								long l0 = System.nanoTime();
								sensor.work(context, antrlFile);
								long l02 = System.nanoTime();
								//LOGGER.info(format("SENSOR\t%s\tsensor: %s\ttook: %s", file.absolutePath(), sensor.getClass().getSimpleName(),(l02-l0)));
								
							}
							long l3 = System.nanoTime();
							//LOGGER.info(format("ALL\t%s\tlexing %s\tworking %s\tall\t%s", file.absolutePath(), (l2-l1), (l3-l2), (l3-l1)));
							

						} catch (final Throwable e) {
							LOGGER.warn(format("Unexpected error parsing file %s", file.absolutePath()), e);
						}

					}
				});

			}

		});

		try {
			executorService.shutdown();
			executorService.awaitTermination(timeout == 0 ? 3600 : timeout, TimeUnit.SECONDS);
			executorService.shutdownNow();
		} catch (final InterruptedException e) {
			LOGGER.warn("Unexpected error while running waiting for executor service to finish", e);

		}

	}

	@Override
	public void describe(final SensorDescriptor descriptor) {
		descriptor.name(this.getClass().getSimpleName()).onlyOnLanguage(TSQLLanguage.KEY);
	}

}
