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

	@Override
	public void work(SensorContext context, CommonTokenStream stream, InputFile file) {
		final boolean skipCpdAnalysis = context.settings().getBoolean(Constants.PLUGIN_SKIP_CPD);

		if (skipCpdAnalysis || !(file instanceof DefaultInputFile)) {

			return;
		}
		final NewCpdTokens cpdTokens = context.newCpdTokens().onFile(file);

		final List<Token> alltokens = stream.getTokens();
		for (final Token token : alltokens) {
			try {

				cpdTokens.addToken(((DefaultInputFile) file).newRange(token.getStartIndex(), token.getStopIndex()+1),
						token.getText());

			} catch (final Throwable e) {
				LOGGER.debug(format("Unexpected error adding highlightings/tokens on file %s", file.absolutePath()), e);
			}
		}
		cpdTokens.save();

	}

}
