package org.sonar.plugins.tsql.sensors.antlr4;

import static java.lang.String.format;

import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;

public class AntlrCpdTokenizer implements IAntlrSensor {
	private static final Logger LOGGER = Loggers.get(AntlrCpdTokenizer.class);

	@Override
	public void work(final SensorContext context, final CommonTokenStream stream, InputFile file) {
		final boolean skipCpdAnalysis = context.settings().getBoolean(Constants.PLUGIN_SKIP_CPD);

		if (skipCpdAnalysis || file == null) {

			return;
		}
		final NewCpdTokens cpdTokens = context.newCpdTokens().onFile(file);

		final Token[] alltokens = stream.getTokens().toArray(new Token[0]);
		for (final Token token : alltokens) {

			int startLine = token.getLine();
			int startLineOffset = token.getCharPositionInLine();
			int endLine = startLine;
			int endLineOffset = startLineOffset + token.getText().length();
			if (startLine == 1) {
				// startLineOffset -= 1;
			}
			final String text = token.getText();
			try {

				if (token.getStartIndex() >= token.getStopIndex() || token.getType() == TSqlParser.EOF
						|| token.getType() == TSqlParser.COMMENT || token.getType() == TSqlParser.LINE_COMMENT
						|| StringUtils.isEmpty(text) || !StringUtils.isAlphanumeric(text)) {
					continue;
				}
				final TextRange range = file.newRange(startLine, startLineOffset, endLine, endLineOffset);
				cpdTokens.addToken(range, text);
			} catch (final Throwable e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(
							format("Unexpected error adding cpd tokens on file %s for token %s on (%s, %s) -  (%s, %s)",
									file.absolutePath(), text, startLine, startLineOffset, endLine, endLineOffset),
							e);
				}

			}
		}
		try {
			synchronized (context) {
				cpdTokens.save();
			}

		} catch (Throwable e) {
			LOGGER.warn(format("Unexpected error saving cpd tokens on file %s", file.absolutePath()), e);
		}
	}

}
