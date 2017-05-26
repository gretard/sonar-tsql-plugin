package org.sonar.plugins.tsql.sensors;

import static java.lang.String.format;

import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.antlr4.tsqlLexer;
import org.sonar.plugins.tsql.antlr4.tsqlParser;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.languages.keywords.IKeywordsProvider;
import org.sonar.plugins.tsql.languages.keywords.KeywordsProvider;

public class HighlightingSensor implements org.sonar.api.batch.sensor.Sensor {

	private static final Logger LOGGER = Loggers.get(HighlightingSensor.class);

	protected final Settings settings;

	private final IKeywordsProvider keywordsProvider = new KeywordsProvider();

	public HighlightingSensor(final Settings settings) {
		this.settings = settings;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void execute(final org.sonar.api.batch.sensor.SensorContext context) {

		final boolean skipAnalysis = this.settings.getBoolean(Constants.PLUGIN_SKIP);
		final boolean skipCpdAnalysis = this.settings.getBoolean(Constants.PLUGIN_SKIP_CPD);

		if (skipAnalysis) {
			LOGGER.debug(format("Skipping plugin as skip flag is set"));
			return;
		}

		final FileSystem fs = context.fileSystem();
		final Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasLanguage(TSQLLanguage.KEY));

		for (final InputFile file : files) {
			try {

				final NewCpdTokens cpdTokens = context.newCpdTokens().onFile(file);
				final NewHighlighting newHighlightning = context.newHighlighting().onFile(file);
				final CharStream charStream = CharStreams.fromFileName(file.absolutePath());
				final tsqlLexer lexer = new tsqlLexer(charStream);
				final CommonTokenStream tokens = new CommonTokenStream(lexer);
				tokens.fill();
				final List<Token> alltokens = tokens.getTokens();
				for (final Token token : alltokens) {
					try {
						if (token.getType() == tsqlParser.EOF) {
							continue;
						}

						if (!skipCpdAnalysis && file instanceof DefaultInputFile) {
							cpdTokens.addToken(
									((DefaultInputFile) file).newRange(token.getStartIndex(), token.getStopIndex()),
									token.getText());
						}

						if (token.getType() == tsqlParser.COMMENT || token.getType() == tsqlParser.LINE_COMMENT) {
							newHighlightning.highlight(token.getStartIndex(), token.getStopIndex(), TypeOfText.COMMENT);
							continue;
						}

						if (token.getType() == tsqlParser.STRING) {
							newHighlightning.highlight(token.getStartIndex(), token.getStopIndex(), TypeOfText.STRING);
							continue;
						}

						if (token.getType() > tsqlParser.LINE_COMMENT) {
							continue;
						}

						if (this.keywordsProvider.isKeyword(tsqlParser.VOCABULARY.getSymbolicName(token.getType()))) {
							newHighlightning.highlight(token.getStartIndex(), token.getStopIndex(), TypeOfText.KEYWORD);
						}
					} catch (final Throwable e) {
						LOGGER.debug(
								format("Unexpected error adding highlightings/tokens on file %s", file.absolutePath()),
								e);
					}
				}

				try {
					cpdTokens.save();
				} catch (final Throwable e) {
					LOGGER.debug(format("Unexpected error saving cpdtokens on file %s", file.absolutePath()), e);
				}
				try {
					newHighlightning.save();
				} catch (final Throwable e) {
					LOGGER.debug(format("Unexpected error saving highlightings on file %s", file.absolutePath()), e);
				}

			} catch (final Throwable e) {
				LOGGER.warn(format("Unexpected error adding highlightings/tokens on file %s", file.absolutePath()), e);
			}
		}
	}

	@Override
	public void describe(final SensorDescriptor descriptor) {
		descriptor.name(this.getClass().getSimpleName()).onlyOnLanguage(TSQLLanguage.KEY);
	}

}
