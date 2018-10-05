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
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.config.Settings;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.antlr.CandidateRule;
import org.sonar.plugins.tsql.antlr.FillerRequest;
import org.sonar.plugins.tsql.antlr.PluginHelper;
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
import org.sonar.plugins.tsql.rules.definitions.CustomPluginChecks;

public class CustomChecksSensor extends BaseTsqlSensor {

	protected final Settings settings;
	private final CustomAllChecksProvider checksProvider;

	public CustomChecksSensor(final Settings settings) {
		super(Constants.PLUGIN_SKIP_CUSTOM);
		this.settings = settings;
		this.checksProvider = new CustomAllChecksProvider(settings);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	protected void innerExecute(final org.sonar.api.batch.sensor.SensorContext context) {

		final int timeout = settings.getInt(Constants.PLUGIN_ANALYSIS_TIMEOUT);
		final int maxSize = settings.getInt(Constants.PLUGIN_MAX_FILE_SIZE);

		final FileSystem fs = context.fileSystem();
		final Charset encoding = context.fileSystem().encoding();

		final CandidateRule[] candidateRules = checksProvider.getChecks(fs.baseDir().getAbsolutePath());

		final Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage(TSQLLanguage.KEY));
		final ExecutorService executorService = Executors.newWorkStealingPool();

		final org.sonar.plugins.tsql.checks.custom.Rule fileTooLargeRule = CustomPluginChecks.getFileTooLargeRule();
		files.forEach(new Consumer<InputFile>() {

			@Override
			public void accept(final InputFile file) {
				long sizeInMb = file.file().length() / 1024 / 1024;

				if (maxSize > 0 && sizeInMb >= maxSize) {
					try {
						NewIssue is = context.newIssue()
								.forRule(RuleKey.of(CustomPluginChecks.getRepoKey(), fileTooLargeRule.getKey()));
						is.at(is.newLocation().on(file).message(String
								.format("File size is %s and over %s mb. Consider splitting it. ", sizeInMb, maxSize)))
								.save();
						context.newAnalysisError().onFile(file)
								.message(String.format("File is over %s mb. Consider splitting it. ", sizeInMb)).save();
						LOGGER.debug(format("File '%s' is too large for analysis: %s, max: %s", file.absolutePath(),
								sizeInMb, maxSize));
					} catch (Throwable e) {
						LOGGER.warn("Unexpected error while saving anslysis error", e);

					}
					return;
				}

				executorService.execute(new Runnable() {

					@Override
					public void run() {
						try {

							final FillerRequest fillerRequest = PluginHelper.createRequest(file, encoding);
							final ISensorFiller[] fillers = new ISensorFiller[] { new SourceLinesMeasuresFiller(),
									new AntlrHighlighter() };
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
