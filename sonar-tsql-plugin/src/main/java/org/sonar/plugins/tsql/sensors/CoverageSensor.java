package org.sonar.plugins.tsql.sensors;

import java.io.File;
import java.util.Map;
import java.util.function.Consumer;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.coverage.FileNamesMatcher;
import org.sonar.plugins.tsql.coverage.CoveredLinesReport;
import org.sonar.plugins.tsql.coverage.CoveredLinesReport.LineInfo;
import org.sonar.plugins.tsql.coverage.ICoveragProvider;
import org.sonar.plugins.tsql.coverage.SqlCoverCoverageProvider;
import org.sonar.plugins.tsql.languages.TSQLLanguage;

public class CoverageSensor implements org.sonar.api.batch.sensor.Sensor {

	private static final Logger LOGGER = Loggers.get(CoverageSensor.class);

	private final ICoveragProvider coverageProvider;

	private final FileNamesMatcher fileNamesMatcher = new FileNamesMatcher();

	public CoverageSensor(final ICoveragProvider coverageProvider) {
		this.coverageProvider = coverageProvider;
	}

	public CoverageSensor(final FileSystem fs, final Settings settings) {
		this(new SqlCoverCoverageProvider(settings, fs));
	}

	@Override
	public void describe(final SensorDescriptor descriptor) {
		descriptor.name(this.getClass().getSimpleName()).onlyOnLanguage(TSQLLanguage.KEY);
	}

	@Override
	public void execute(final SensorContext context) {
		final FileSystem fs = context.fileSystem();
		if (context.settings().getBoolean(Constants.PLUGIN_SKIP_COVERAGE)) {
			return;
		}
		try {
			final Map<String, CoveredLinesReport> coveredFiles = coverageProvider.getHitLines();
			final Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage(TSQLLanguage.KEY));
			files.forEach(new Consumer<InputFile>() {
				@Override
				public void accept(InputFile t) {
					final File file = t.file();
					final CoveredLinesReport[] hitLines = fileNamesMatcher.match(file.getName(), file.getParent(), coveredFiles);
					final int coverageReportsFound = hitLines.length;
					if (coverageReportsFound == 0) {
						return;
					}
					if (coverageReportsFound > 1) {
						LOGGER.info("Found {} multiple coverage matches for file {}", coverageReportsFound,
								t.absolutePath());
						return;
					}
					try {
						NewCoverage newCoverage = context.newCoverage().onFile(t);
						final CoveredLinesReport lines = hitLines[0];
						for (final LineInfo lineInfo : lines.getHitLines()) {
							newCoverage.lineHits(lineInfo.getLine(), lineInfo.getHitsCount());
						}
						newCoverage.save();
					} catch (Throwable e) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Error while adding coverage for {}", t.absolutePath(), e);
						}

					}

				}
			});

		} catch (Throwable e) {
			LOGGER.warn("Unexpected error while running sensor", e);
		}
	}

}
