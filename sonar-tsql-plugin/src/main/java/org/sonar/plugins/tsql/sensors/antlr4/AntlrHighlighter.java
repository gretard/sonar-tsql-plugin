package org.sonar.plugins.tsql.sensors.antlr4;

import static java.lang.String.format;

import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.languages.keywords.IKeywordsProvider;
import org.sonar.plugins.tsql.languages.keywords.KeywordsProvider;

public class AntlrHighlighter implements IAntlrSensor {
	private static final Logger LOGGER = Loggers.get(AntlrHighlighter.class);
	private final IKeywordsProvider keywordsProvider = new KeywordsProvider();
	boolean debugEnabled = LOGGER.isDebugEnabled();

	@Override
	public void work(SensorContext context, CommonTokenStream stream, InputFile file) {

		final NewHighlighting newHighlightning = context.newHighlighting().onFile(file);

		final Token[] alltokens = stream.getTokens().toArray(new Token[0]);
		for (final Token token : alltokens) {
			final String text = token.getText();
			int startLine = token.getLine();
			int startLineOffset = token.getCharPositionInLine();
			int endLine = startLine;
			int endLineOffset = startLineOffset + token.getText().length();

			try {

				if (token.getStartIndex() >= token.getStopIndex() ||  StringUtils.isEmpty(text)) {
					continue;
				}

				if (token.getType() == TSqlParser.EOF) {
					continue;
				}
				if (token.getType() == TSqlParser.COMMENT || token.getType() == TSqlParser.LINE_COMMENT) {

					newHighlightning.highlight(startLine, startLineOffset, endLine, endLineOffset, TypeOfText.COMMENT);
					continue;
				}

				if (token.getType() == TSqlParser.STRING) {
					newHighlightning.highlight(startLine, startLineOffset, endLine, endLineOffset, TypeOfText.STRING);

					continue;
				}

				if (token.getType() > TSqlParser.LINE_COMMENT) {
					continue;
				}

				if (this.keywordsProvider.isKeyword(TSqlParser.VOCABULARY.getSymbolicName(token.getType()))) {
					newHighlightning.highlight(startLine, startLineOffset, endLine, endLineOffset, TypeOfText.KEYWORD);
				}
			} catch (final Throwable e) {
				if (debugEnabled) {
					LOGGER.debug(
							format("Unexpected error adding highlightings on file %s for token %s on (%s, %s) -  (%s, %s)",
									file.absolutePath(), text, startLine, startLineOffset, endLine, endLineOffset),
							e);
				}

			}
		}
		newHighlightning.save();

	}

}
