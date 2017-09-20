package org.sonar.plugins.tsql.sensors.antlr4;

import static java.lang.String.format;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.batch.sensor.highlighting.internal.DefaultHighlighting;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.antlr4.tsqlParser;
import org.sonar.plugins.tsql.languages.keywords.IKeywordsProvider;
import org.sonar.plugins.tsql.languages.keywords.KeywordsProvider;

public class AntlrHighlighter implements IAntlrSensor {
	private static final Logger LOGGER = Loggers.get(AntlrHighlighter.class);
	private final IKeywordsProvider keywordsProvider = new KeywordsProvider();
	boolean debugEnabled = LOGGER.isDebugEnabled();
	@Override
	public void work(SensorContext context, CommonTokenStream stream, InputFile file) {

		final NewHighlighting newHighlightning = context.newHighlighting().onFile(file);
	
		final List<Token> alltokens = stream.getTokens();
		for (final Token token : alltokens) {
			final String text = token.getText();
			int startLine = token.getLine();
			int startLineOffset = token.getCharPositionInLine();
			int endLine = startLine;
			int endLineOffset = startLineOffset + token.getText().length();
			if (startLine == 1) {
				startLineOffset -= 1;
			}
			try {

				if (token.getStartIndex() >= token.getStopIndex() || text.isEmpty()) {
					continue;
				}

				if (token.getType() == tsqlParser.EOF) {
					continue;
				}
				if (token.getType() == tsqlParser.COMMENT || token.getType() == tsqlParser.LINE_COMMENT) {

					newHighlightning.highlight(startLine, startLineOffset, endLine, endLineOffset, TypeOfText.COMMENT);
					continue;
				}

				if (token.getType() == tsqlParser.STRING) {
					newHighlightning.highlight(startLine, startLineOffset, endLine, endLineOffset, TypeOfText.STRING);

					continue;
				}

				if (token.getType() > tsqlParser.LINE_COMMENT) {
					continue;
				}

				if (this.keywordsProvider.isKeyword(tsqlParser.VOCABULARY.getSymbolicName(token.getType()))) {
					newHighlightning.highlight(startLine, startLineOffset, endLine, endLineOffset, TypeOfText.KEYWORD);
				}
			} catch (final Throwable e) {
				if (debugEnabled) {
					LOGGER.debug(
							format("Unexpected error adding highlightings/tokens on file %s for token %s on (%s, %s) -  (%s, %s)",
									file.absolutePath(), text, startLine, startLineOffset, endLine, endLineOffset),
							e);
				}

			}
		}
		newHighlightning.save();

	}

}
