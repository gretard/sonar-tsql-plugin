package org.sonar.plugins.tsql.sensors;

import static java.lang.String.format;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.rules.definitions.CustomAllChecksProvider;
import org.sonar.plugins.tsql.sensors.antlr4.AntlrCustomRulesSensor;
import org.sonar.plugins.tsql.sensors.antlr4.AntlrHighlighter;
import org.sonar.plugins.tsql.sensors.antlr4.AntlrMeasurer;
import org.sonar.plugins.tsql.sensors.antlr4.CandidateRule;
import org.sonar.plugins.tsql.sensors.antlr4.FillerRequest;
import org.sonar.plugins.tsql.sensors.antlr4.IAntlrFiller;
import org.sonar.plugins.tsql.sensors.antlr4.PluginHelper;
import org.sonar.plugins.tsql.sensors.custom.lines.SourceLinesProvider;

public class HighlightingSensor implements org.sonar.api.batch.sensor.Sensor {

	private static final Logger LOGGER = Loggers.get(HighlightingSensor.class);
	protected final Settings settings;
	private final SourceLinesProvider linesProvider = new SourceLinesProvider();
	private final CustomAllChecksProvider checksProvider;

	public HighlightingSensor(final Settings settings) {
		this.settings = settings;
		this.checksProvider = new CustomAllChecksProvider(settings);
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

		final int timeout = settings.getInt(Constants.PLUGIN_ANALYSIS_TIMEOUT);
		final FileSystem fs = context.fileSystem();
		final Charset encoding = context.fileSystem().encoding();

		final CandidateRule[] candidateRules = checksProvider.getChecks(fs.baseDir().getAbsolutePath());
		final IAntlrFiller[] sensors = new IAntlrFiller[] { new AntlrHighlighter(),
				new AntlrCustomRulesSensor(candidateRules), new AntlrMeasurer() };

		final Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage(TSQLLanguage.KEY));
		final ExecutorService executorService = Executors.newWorkStealingPool();

		files.forEach(new Consumer<InputFile>() {

			@Override
			public void accept(final InputFile file) {
				executorService.execute(new Runnable() {

					@Override
					public void run() {
						try {

							final FillerRequest fillerRequest = PluginHelper.createRequest(linesProvider, file,
									encoding);

							for (final IAntlrFiller sensor : sensors) {

								sensor.fill(context, fillerRequest);

							}

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
