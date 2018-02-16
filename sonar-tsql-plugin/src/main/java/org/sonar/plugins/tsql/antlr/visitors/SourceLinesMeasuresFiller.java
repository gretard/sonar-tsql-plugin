package org.sonar.plugins.tsql.antlr.visitors;

import static java.lang.String.format;

import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.Token;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.antlr.FillerRequest;
import org.sonar.plugins.tsql.antlr.FillerRequest.SourceCodeMeasures;

public class SourceLinesMeasuresFiller implements ISensorFiller {
	private static final Logger LOGGER = Loggers.get(SourceLinesMeasuresFiller.class);

	@Override
	public void fill(SensorContext sensorContext, FillerRequest fillerRequest) {
		final InputFile file = fillerRequest.getFile();
		final SourceCodeMeasures measures = fillerRequest.getMeasures();
		synchronized (sensorContext) {

			try {
				sensorContext.<Integer>newMeasure().on(file).forMetric(CoreMetrics.NCLOC).withValue(measures.getLocs())
						.save();
			} catch (final Throwable e) {
				LOGGER.warn(format("Unexpected adding nloc measures on file %s", file.absolutePath()), e);
			}

			try {
				sensorContext.<Integer>newMeasure().on(file).forMetric(CoreMetrics.COMMENT_LINES)
						.withValue(measures.getComments()).save();
			} catch (final Throwable e) {
				LOGGER.warn(format("Unexpected adding comment_lines measures on file %s", file.absolutePath()), e);
			}
		}
	}

}
