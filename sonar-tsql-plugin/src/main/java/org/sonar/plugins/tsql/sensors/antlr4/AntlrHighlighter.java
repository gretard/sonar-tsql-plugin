package org.sonar.plugins.tsql.sensors.antlr4;

import static java.lang.String.format;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.antlr4.tsqlParser;
import org.sonar.plugins.tsql.languages.keywords.IKeywordsProvider;
import org.sonar.plugins.tsql.languages.keywords.KeywordsProvider;

public class AntlrHighlighter implements IAntlrSensor {
	private static final Logger LOGGER = Loggers.get(AntlrHighlighter.class);
	private final IKeywordsProvider keywordsProvider = new KeywordsProvider();

	@Override
	public void work(SensorContext context, CommonTokenStream stream, InputFile file) {

		final NewHighlighting newHighlightning = context.newHighlighting().onFile(file);

		final List<Token> alltokens = stream.getTokens();
		for (final Token token : alltokens) {
			try {

				if (token.getType() == tsqlParser.EOF) {
					continue;
				}

				if (token.getType() == tsqlParser.COMMENT || token.getType() == tsqlParser.LINE_COMMENT) {
					newHighlightning.highlight(token.getStartIndex(), token.getStopIndex()+1, TypeOfText.COMMENT);
					continue;
				}

				if (token.getType() == tsqlParser.STRING) {
					newHighlightning.highlight(token.getStartIndex(), token.getStopIndex()+1, TypeOfText.STRING);
					continue;
				}

				if (token.getType() > tsqlParser.LINE_COMMENT) {
					continue;
				}

				if (this.keywordsProvider.isKeyword(tsqlParser.VOCABULARY.getSymbolicName(token.getType()))) {
					newHighlightning.highlight(token.getStartIndex(), token.getStopIndex()+1, TypeOfText.KEYWORD);
				}
			} catch (final Throwable e) {
				LOGGER.debug(format("Unexpected error adding highlightings/tokens on file %s", file.absolutePath()), e);
			}
		}
		newHighlightning.save();

	}

}
