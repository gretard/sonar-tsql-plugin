package org.sonar.plugins.tsql.sensors;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.apache.commons.io.FilenameUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.coverage.HitLines;
import org.sonar.plugins.tsql.coverage.HitLines.LineInfo;
import org.sonar.plugins.tsql.coverage.ICoveragProvider;
import org.sonar.plugins.tsql.languages.TSQLLanguage;

public class CoverageSensor implements org.sonar.api.batch.sensor.Sensor {
	private ICoveragProvider coverageProvider;

	public CoverageSensor(ICoveragProvider coverageProvider) {
		this.coverageProvider = coverageProvider;

	}

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.name(this.getClass().getSimpleName()).onlyOnLanguage(TSQLLanguage.KEY);
	}

	@Override
	public void execute(SensorContext context) {
		final FileSystem fs = context.fileSystem();
		if (context.settings().getBoolean(Constants.PLUGIN_SKIP_COVERAGE)) {
			return;
		}
		try {
			final Map<String, HitLines> coveredFiles = coverageProvider.getHitLines();
			final Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage(TSQLLanguage.KEY));
			files.forEach(new Consumer<InputFile>() {
				@Override
				public void accept(InputFile t) {
					String file = FilenameUtils.removeExtension(t.file().getName());
					for (Entry<String, HitLines> entrySet : coveredFiles.entrySet()) {
						String s = entrySet.getKey();
						if (s.contains(file)) {
							try {
								NewCoverage cov = context.newCoverage().onFile(t);
								HitLines lines = entrySet.getValue();
								for (LineInfo lineInfo : lines.getHitLines()) {
									cov.lineHits(lineInfo.getLine(), lineInfo.getHitsCount());
								}
								cov.save();
							} catch (Throwable e) {

							}
						}
					}

				}

			});

		} catch (Throwable e) {

		}
	}
}
