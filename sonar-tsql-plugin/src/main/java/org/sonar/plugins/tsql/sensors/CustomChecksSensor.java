package org.sonar.plugins.tsql.sensors;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Configuration;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.antlr.AntlrContext;
import org.sonar.plugins.tsql.antlr.CandidateRule;
import org.sonar.plugins.tsql.antlr.visitors.AntlrHighlighter;
import org.sonar.plugins.tsql.antlr.visitors.CComplexityVisitor;
import org.sonar.plugins.tsql.antlr.visitors.ComplexityVisitor;
import org.sonar.plugins.tsql.antlr.visitors.CustomRulesVisitor;
import org.sonar.plugins.tsql.antlr.visitors.CustomTreeVisitor;
import org.sonar.plugins.tsql.antlr.visitors.IParseTreeItemVisitor;
import org.sonar.plugins.tsql.antlr.visitors.SourceLinesMeasuresFiller;
import org.sonar.plugins.tsql.checks.CustomAllChecksProvider;
import org.sonar.plugins.tsql.languages.TSQLLanguage;

public class CustomChecksSensor extends BaseTsqlSensor {

	public CustomChecksSensor() {
		super(Constants.PLUGIN_SKIP_CUSTOM);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	protected void innerExecute(final org.sonar.api.batch.sensor.SensorContext context) {
		final Configuration config = context.config();
		final int timeout = config.getInt(Constants.PLUGIN_ANALYSIS_TIMEOUT).orElse(10000);
		final int maxSize = config.getInt(Constants.PLUGIN_MAX_FILE_SIZE).orElse(10);

		CustomAllChecksProvider checks = new CustomAllChecksProvider();
		CandidateRule[] rules = checks.getChecks(context);
		final ExecutorService executorService = Executors.newWorkStealingPool();

		final org.sonar.api.batch.fs.FileSystem fs = context.fileSystem();
		final Charset charset = fs.encoding();
		final Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage(TSQLLanguage.KEY));
		files.forEach(f -> {
			long fileSizeInMb = f.file().length() / 1024 / 1024;
			if (fileSizeInMb > maxSize) {
				context.newAnalysisError().onFile(f)
						.message(String.format("File is %s too large for analysis: size %s maximum allowed: %s",
								f.filename(), fileSizeInMb, maxSize))
						.save();
				return;
			}

			executorService.execute(() -> {
				try {

					final AntlrContext antlrContext = AntlrContext.fromInputFile(f, charset);
					final IParseTreeItemVisitor visitor = new CustomTreeVisitor(new CustomRulesVisitor(rules),
							new AntlrHighlighter(), new CComplexityVisitor(), new ComplexityVisitor(),
							new SourceLinesMeasuresFiller());
					visitor.fillContext(context, antlrContext);
				} catch (Throwable e) {
					LOGGER.warn("Unexpected error while analyzing file " + f.filename(), e);
				}
			});

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
