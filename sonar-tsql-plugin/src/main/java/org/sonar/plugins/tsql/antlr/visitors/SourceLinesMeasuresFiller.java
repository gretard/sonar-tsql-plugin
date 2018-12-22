package org.sonar.plugins.tsql.antlr.visitors;

import java.io.InputStream;
import java.nio.charset.Charset;

import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.antlr.AntlrContext;
import org.sonar.plugins.tsql.lines.SourceLine;
import org.sonar.plugins.tsql.lines.SourceLinesProvider;

public class SourceLinesMeasuresFiller implements IParseTreeItemVisitor {
	private static final Logger LOGGER = Loggers.get(SourceLinesMeasuresFiller.class);

	final SourceLinesProvider linesProvider = new SourceLinesProvider();

	private static int[] getLineAndColumn(final SourceLine[] lines, final int global) {

		for (final SourceLine line : lines) {
			if (line.getEnd() > global) {
				return new int[] { line.getLine(), global - line.getStart() };
			}
		}
		return new int[0];
	}

	@Override
	public void fillContext(SensorContext sensorContext, AntlrContext antlrContext) {
		try (InputStream stream = antlrContext.getFile().inputStream()) {
			final SourceLine[] lines = antlrContext.getLines();
			final Token[] alltokens = antlrContext.getTokens();
			final int[] total = new int[lines.length];
			final InputFile file = antlrContext.getFile();
			for (final Token token : alltokens) {
				int startLine = token.getLine();
				int[] endLines = getLineAndColumn(lines, token.getStopIndex());
				if (endLines == null || endLines.length == 0 || token.getStartIndex() >= token.getStopIndex()) {
					continue;
				}
				if (token.getType() == TSqlParser.EOF || token.getType() == TSqlParser.COMMENT
						|| token.getType() == TSqlParser.LINE_COMMENT) {
					continue;
				}
				for (int i = startLine - 1; i < endLines[0]; i++) {
					total[i] = 1; // line of code
				}

			}
			for (final Token token : alltokens) {
				int startLine = token.getLine();
				int[] endLines = getLineAndColumn(lines, token.getStopIndex());
				if (token.getType() == TSqlParser.EOF || endLines == null || endLines.length == 0
						|| token.getStartIndex() >= token.getStopIndex()) {
					continue;
				}
				if (token.getType() == TSqlParser.COMMENT || token.getType() == TSqlParser.LINE_COMMENT) {
					for (int i = startLine - 1; i < endLines[0]; i++) {
						if (total[i] == 0) {
							total[i] = 2; // comment
						}

					}
				}

			}
			int comments = 0;
			int locs = 0;
			for (final int lineType : total) {
				if (lineType == 1) {
					locs++;
					continue;
				}
				if (lineType == 2) {
					comments++;
				}
			}

			synchronized (sensorContext) {

				try {
					sensorContext.<Integer>newMeasure().on(file).forMetric(CoreMetrics.NCLOC).withValue(locs).save();
				} catch (final Throwable e) {
					LOGGER.warn(String.format("Unexpected adding nloc measures on file %s", file.absolutePath()), e);
				}

				try {
					sensorContext.<Integer>newMeasure().on(file).forMetric(CoreMetrics.COMMENT_LINES)
							.withValue(comments).save();
				} catch (final Throwable e) {
					LOGGER.warn(String.format("Unexpected error while adding comment_lines measures on file %s",
							file.absolutePath()), e);
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	@Override
	public void apply(ParseTree tree) {

	}

}
