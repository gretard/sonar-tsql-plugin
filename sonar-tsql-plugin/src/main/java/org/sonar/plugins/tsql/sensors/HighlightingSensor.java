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
import org.sonar.plugins.tsql.antlr.visitors.AntlrHighlighter;
import org.sonar.plugins.tsql.antlr.visitors.CComplexityVisitor;
import org.sonar.plugins.tsql.antlr.visitors.ComplexityVisitor;
import org.sonar.plugins.tsql.antlr.visitors.CustomRulesVisitor;
import org.sonar.plugins.tsql.antlr.visitors.CustomTreeVisitor;
import org.sonar.plugins.tsql.antlr.visitors.IParseTreeItemVisitor;
import org.sonar.plugins.tsql.antlr.visitors.ISensorFiller;
import org.sonar.plugins.tsql.antlr.visitors.SourceLinesMeasuresFiller;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.rules.definitions.CustomAllChecksProvider;
import org.sonar.plugins.tsql.sensors.antlr4.CandidateRule;
import org.sonar.plugins.tsql.sensors.antlr4.FillerRequest;
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
							final ISensorFiller[] fillers = new ISensorFiller[] { new SourceLinesMeasuresFiller(), new AntlrHighlighter() };
							final IParseTreeItemVisitor[] visitors = new IParseTreeItemVisitor[] {
									new CustomRulesVisitor(candidateRules), new ComplexityVisitor(),
									new CComplexityVisitor() };
							new CustomTreeVisitor(visitors).visit(fillerRequest.getRoot());
							for (final ISensorFiller f : fillers) {
								f.fill(context, fillerRequest);
							}
							for (final ISensorFiller f : visitors) {
								f.fill(context, fillerRequest);
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
