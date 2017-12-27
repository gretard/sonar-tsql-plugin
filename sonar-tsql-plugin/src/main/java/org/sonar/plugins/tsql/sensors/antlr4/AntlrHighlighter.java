package org.sonar.plugins.tsql.sensors.antlr4;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.Token;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.languages.keywords.IKeywordsProvider;
import org.sonar.plugins.tsql.languages.keywords.KeywordsProvider;

public class AntlrHighlighter implements IAntlrSensor {
	private static final Logger LOGGER = Loggers.get(AntlrHighlighter.class);
	private final IKeywordsProvider keywordsProvider = new KeywordsProvider();
	private final boolean debugEnabled = LOGGER.isDebugEnabled();

	@Override
	public void work(final SensorContext context, final AntrlFile antrlFile) {

		final InputFile file = antrlFile.getFile();
		if (file == null) {
			return;
		}
		final NewCpdTokens cpdTokens = context.newCpdTokens().onFile(file);

		final NewHighlighting newHighlightning = context.newHighlighting().onFile(file);
		final List<TextRange> ranges = new ArrayList<TextRange>();
		final Token[] alltokens = antrlFile.getTokens();
		main: for (final Token token : alltokens) {
			final int startLine = token.getLine();
			final int startLineOffset = token.getCharPositionInLine();
			final int[] endDetails = antrlFile.getLineAndColumn(token.getStopIndex());

			if (endDetails == null || token.getType() == TSqlParser.EOF
					|| token.getStartIndex() >= token.getStopIndex()) {
				continue;
			}

			final int endLine = endDetails[0];
			final int endLineOffset = endDetails[1] + 1;
		

			try {
				final TextRange range = file.newRange(startLine, startLineOffset, endLine, endLineOffset);

				for (final TextRange r : ranges) {
					if (r.overlap(range)) {
						continue main;
					}
				}
				cpdTokens.addToken(range, token.getText());
				ranges.add(range);
				if (token.getType() == TSqlParser.COMMENT || token.getType() == TSqlParser.LINE_COMMENT) {
					newHighlightning.highlight(range, TypeOfText.COMMENT);
					continue;
				}

				if (token.getType() == TSqlParser.STRING) {
					newHighlightning.highlight(range, TypeOfText.STRING);
					continue;
				}

				if (this.keywordsProvider.isKeyword(TSqlParser.VOCABULARY.getSymbolicName(token.getType()))) {
					newHighlightning.highlight(range, TypeOfText.KEYWORD);
				}

			} catch (final Throwable e) {
				if (debugEnabled) {
					LOGGER.debug(
							format("Unexpected error adding highlightings on file %s for token %s on (%s, %s) -  (%s, %s)",
									file.absolutePath(), token.getText(), startLine, startLineOffset, endLine,
									endLineOffset),
							e);
				}

			}
		}
		synchronized (context) {

			try {
				newHighlightning.save();
			} catch (final Throwable e) {
				LOGGER.warn(format("Unexpected error saving highlightings on file %s", file.absolutePath()), e);
			}
			try {
				cpdTokens.save();
			} catch (final Throwable e) {
				LOGGER.warn(format("Unexpected error saving cpd tokens on file %s", file.absolutePath()), e);
			}
		}
	}

}
