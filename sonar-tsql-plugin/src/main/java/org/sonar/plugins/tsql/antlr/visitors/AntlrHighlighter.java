package org.sonar.plugins.tsql.antlr.visitors;

import static java.lang.String.format;

import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.antlr.AntlrContext;
import org.sonar.plugins.tsql.languages.keywords.IKeywordsProvider;
import org.sonar.plugins.tsql.languages.keywords.KeywordsProvider;

public class AntlrHighlighter implements IParseTreeItemVisitor {

	private static final Logger LOGGER = Loggers.get(AntlrHighlighter.class);
	private final IKeywordsProvider keywordsProvider = new KeywordsProvider();

	@Override
	public void fillContext(final SensorContext context, final AntlrContext antrlFile) {

		final InputFile file = antrlFile.getFile();
		if (file == null) {
			return;
		}
		final NewCpdTokens cpdTokens = context.newCpdTokens().onFile(file);

		final NewHighlighting newHighlightning = context.newHighlighting().onFile(file);
		final Token[] alltokens = antrlFile.getTokens();
		for (final Token token : alltokens) {
			final int startLine = token.getLine();
			final int startLineOffset = token.getCharPositionInLine();
			final int[] endDetails = antrlFile.getLineAndColumn(token.getStopIndex());

			if (endDetails == null || endDetails.length != 2 || token.getType() == TSqlParser.EOF
					|| token.getStartIndex() >= token.getStopIndex()) {
				continue;
			}

			final int endLine = endDetails[0];
			final int endLineOffset = endDetails[1] + 1;

			try {
				final TextRange range = file.newRange(startLine, startLineOffset, endLine, endLineOffset);

				addHighlighting(newHighlightning, token, file, range);

				addCpdToken(cpdTokens, file, token, range);

			} catch (final Throwable e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(format(
							"Unexpected error creating text range on file %s for token %s on (%s, %s) -  (%s, %s)",
							file.absolutePath(), token.getText(), startLine, startLineOffset, endLine, endLineOffset),
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

	private static void addCpdToken(final NewCpdTokens cpdTokens, InputFile file, final Token token,
			final TextRange range) {
		try {
			cpdTokens.addToken(range, token.getText());
		} catch (Throwable e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(format("Unexpected error adding cpd tokens on file %s", file.absolutePath()), e);
			}

		}
	}

	private void addHighlighting(final NewHighlighting newHighlightning, final Token token, final InputFile file,
			final TextRange range) {
		try {
			if (token.getType() == TSqlParser.COMMENT || token.getType() == TSqlParser.LINE_COMMENT) {
				newHighlightning.highlight(range, TypeOfText.COMMENT);
			}

			if (token.getType() == TSqlParser.STRING) {
				newHighlightning.highlight(range, TypeOfText.STRING);
			}

			if (this.keywordsProvider.isKeyword(TSqlParser.VOCABULARY.getSymbolicName(token.getType()))) {
				newHighlightning.highlight(range, TypeOfText.KEYWORD);
			}
		} catch (final Throwable e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(format("Unexpected error adding highlighting on file %s", file.absolutePath()), e);
			}
		}
	}

	@Override
	public void apply(ParseTree tree) {

	}

}
