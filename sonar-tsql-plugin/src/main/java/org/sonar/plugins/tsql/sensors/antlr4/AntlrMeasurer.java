package org.sonar.plugins.tsql.sensors.antlr4;

import static java.lang.String.format;

import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class AntlrMeasurer implements IAntlrSensor {
	private static final Logger LOGGER = Loggers.get(AntlrMeasurer.class);
	boolean debugEnabled = LOGGER.isDebugEnabled();

	@Override
	public void work(SensorContext context, AntrlFile antrlFile) {

		InputFile file = antrlFile.getFile();
		final int[] total = new int[antrlFile.getLines().length];
		final Token[] alltokens = antrlFile.getTokens();

		for (final Token token : alltokens) {
			int startLine = token.getLine();
			int[] endLines = antrlFile.getLineAndColumn(token.getStopIndex());
			if (endLines == null || token.getStartIndex() >= token.getStopIndex()
					|| StringUtils.isEmpty(token.getText())) {
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
			int[] endLines = antrlFile.getLineAndColumn(token.getStopIndex());
			if (token.getType() == TSqlParser.EOF || endLines == null || token.getStartIndex() >= token.getStopIndex()
					|| StringUtils.isEmpty(token.getText())) {
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
		final int complexity = new ComplexityVisitor().visit(antrlFile.getRoot());
		synchronized (context) {
			
			try {
				context.<Integer>newMeasure().on(file).forMetric(CoreMetrics.NCLOC).withValue(locs).save();
			} catch (final Throwable e) {
				LOGGER.warn(format("Unexpected adding nloc measures on file %s", file.absolutePath()), e);
			}
			try {
				context.<Integer>newMeasure().on(file).forMetric(CoreMetrics.COMPLEXITY).withValue(complexity).save();
			} catch (final Throwable e) {
				LOGGER.warn(format("Unexpected adding complexity measures on file %s", file.absolutePath()), e);
			}
			try {
				context.<Integer>newMeasure().on(file).forMetric(CoreMetrics.COMMENT_LINES).withValue(comments).save();
			} catch (final Throwable e) {
				LOGGER.warn(format("Unexpected adding comment_lines measures on file %s", file.absolutePath()), e);
			}
		}
	}

}
