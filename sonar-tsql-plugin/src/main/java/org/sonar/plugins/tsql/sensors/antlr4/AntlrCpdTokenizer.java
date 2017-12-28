package org.sonar.plugins.tsql.sensors.antlr4;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;

public class AntlrCpdTokenizer implements IAntlrFiller {
	private static final Logger LOGGER = Loggers.get(AntlrCpdTokenizer.class);

	@Override
	public void fill(final SensorContext context, final FillerRequest antrlFile) {

		final boolean skipCpdAnalysis = context.settings().getBoolean(Constants.PLUGIN_SKIP_CPD);
		final InputFile file = antrlFile.getFile();
		if (skipCpdAnalysis || file == null) {

			return;
		}

		final NewCpdTokens cpdTokens = context.newCpdTokens().onFile(file);
		final List<TextRange> ranges = new ArrayList<>();
		final Token[] alltokens = antrlFile.getTokens();
		main: for (final Token token : alltokens) {

			final int startLine = token.getLine();
			final int startLineOffset = token.getCharPositionInLine();
			final int[] endDetails = antrlFile.getLineAndColumn(token.getStopIndex());
			if (endDetails == null) {
				continue;
			}
			final int endLine = endDetails[0];
			final int endLineOffset = endDetails[1];

			try {

				if (token.getStartIndex() >= token.getStopIndex() || token.getType() == TSqlParser.EOF
						|| token.getType() == TSqlParser.COMMENT || token.getType() == TSqlParser.LINE_COMMENT
						|| !StringUtils.isAlphanumeric(token.getText())) {
					continue;
				}
				final TextRange range = file.newRange(startLine, startLineOffset, endLine, endLineOffset);
				
				for (final TextRange r : ranges) {
					if (r.overlap(range)) {
						continue main;
					}
				}
				ranges.add(range);
				cpdTokens.addToken(range, token.getText());
			} catch (final Throwable e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(
							format("Unexpected error adding cpd tokens on file %s for token %s on (%s, %s) -  (%s, %s)",
									file.absolutePath(), token.getText(), startLine, startLineOffset, endLine,
									endLineOffset),
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
