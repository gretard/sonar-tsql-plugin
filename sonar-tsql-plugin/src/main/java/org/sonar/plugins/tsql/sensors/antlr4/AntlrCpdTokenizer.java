package org.sonar.plugins.tsql.sensors.antlr4;

import static java.lang.String.format;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;

public class AntlrCpdTokenizer implements IAntlrSensor {
	private static final Logger LOGGER = Loggers.get(AntlrCpdTokenizer.class);
	boolean debugEnabled = LOGGER.isDebugEnabled();
	@Override
	public void work(final SensorContext context, final CommonTokenStream stream, InputFile file) {
		final boolean skipCpdAnalysis = context.settings().getBoolean(Constants.PLUGIN_SKIP_CPD);

		if (skipCpdAnalysis || !(file instanceof DefaultInputFile)) {

			return;
		}
		final NewCpdTokens cpdTokens = context.newCpdTokens().onFile(file);

		final List<Token> alltokens = stream.getTokens();
		for (final Token token : alltokens) {
			int startLine = token.getLine();
			int startLineOffset = token.getCharPositionInLine();
			int endLine = startLine;
			int endLineOffset = startLineOffset + token.getText().length();
			if (startLine == 1) {
				startLineOffset -= 1;
			}
			final String text = token.getText();
			try {

				if (token.getStartIndex() >= token.getStopIndex() || text.isEmpty()) {
					continue;
				}
				cpdTokens.addToken(startLine, startLineOffset, endLine, endLineOffset, text);
			} catch (final Throwable e) {
				if (debugEnabled) {
					LOGGER.debug(
							format("Unexpected error adding cpd tokens on file %s for token %s on (%s, %s) -  (%s, %s)",
									file.absolutePath(), text, startLine, startLineOffset, endLine, endLineOffset),
							e);
				}

			}
		}
		cpdTokens.save();

	}

}
