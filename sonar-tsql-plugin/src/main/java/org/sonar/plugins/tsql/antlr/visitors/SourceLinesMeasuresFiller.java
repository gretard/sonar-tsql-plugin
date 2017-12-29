package org.sonar.plugins.tsql.antlr.visitors;

import static java.lang.String.format;

import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.Token;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.sensors.antlr4.FillerRequest;

public class SourceLinesMeasuresFiller implements ISensorFiller {
	private static final Logger LOGGER = Loggers.get(SourceLinesMeasuresFiller.class);

	@Override
	public void fill(SensorContext sensorContext, FillerRequest fillerRequest) {
		final InputFile file = fillerRequest.getFile();
		final int[] total = new int[fillerRequest.getLines().length];
		final Token[] alltokens = fillerRequest.getTokens();

		for (final Token token : alltokens) {
			int startLine = token.getLine();
			int[] endLines = fillerRequest.getLineAndColumn(token.getStopIndex());
			if (endLines == null || token.getStartIndex() >= token.getStopIndex()) {
				continue;
			}
			if (token.getType() == TSqlParser.EOF || token.getType() == TSqlParser.COMMENT
					|| token.getType() == TSqlParser.LINE_COMMENT) {
				continue;
			}
			for (int i = startLine - 1; i < endLines[0]; i++) {
				total[i] = 1;
			}

		}
		for (final Token token : alltokens) {
			int startLine = token.getLine();
			int[] endLines = fillerRequest.getLineAndColumn(token.getStopIndex());
			if (token.getType() == TSqlParser.EOF || endLines == null
					|| token.getStartIndex() >= token.getStopIndex()) {
				continue;
			}
			if (token.getType() == TSqlParser.COMMENT || token.getType() == TSqlParser.LINE_COMMENT) {
				for (int i = startLine - 1; i < endLines[0]; i++) {
					if (total[i] == 0) {
						total[i] = 2;
					}

				}
			}

		}
		int comments = 0;
		int locs = 0;
		for (int x : total) {
			if (x == 1) {
				locs++;
				continue;
			}
			if (x == 2) {
				comments++;
			}
		}
		synchronized (sensorContext) {

			try {

				sensorContext.<Integer>newMeasure().on(file).forMetric(CoreMetrics.NCLOC).withValue(locs).save();
			} catch (final Throwable e) {
				LOGGER.warn(format("Unexpected adding nloc measures on file %s", file.absolutePath()), e);
			}

			try {
				sensorContext.<Integer>newMeasure().on(file).forMetric(CoreMetrics.COMMENT_LINES).withValue(comments).save();
			} catch (final Throwable e) {
				LOGGER.warn(format("Unexpected adding comment_lines measures on file %s", file.absolutePath()), e);
			}
		}
	}

}
