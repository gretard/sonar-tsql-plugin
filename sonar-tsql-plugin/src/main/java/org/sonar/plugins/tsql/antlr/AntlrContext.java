package org.sonar.plugins.tsql.antlr;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.antlr.tsql.TSqlLexer;
import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.tsql.lines.SourceLine;
import org.sonar.plugins.tsql.lines.SourceLinesProvider;

public class AntlrContext {

	public InputFile getFile() {
		return file;
	}

	public CommonTokenStream getStream() {
		return stream;
	}

	public SourceLine[] getLines() {
		return lines;
	}

	public Token[] getTokens() {
		return this.stream.getTokens().toArray(new Token[0]);
	}

	public ParseTree getRoot() {
		return root;
	}

	public int[] getLineAndColumn(final int global) {

		for (final SourceLine line : this.lines) {
			if (line.getEnd() > global) {
				return new int[] { line.getLine(), global - line.getStart() };
			}
		}
		return null;
	}

	private final InputFile file;
	private final CommonTokenStream stream;
	private final SourceLine[] lines;
	private final ParseTree root;

	public AntlrContext(InputFile file, CommonTokenStream stream, ParseTree root, SourceLine[] lines) {
		this.file = file;
		this.stream = stream;
		this.root = root;
		this.lines = lines;
	}

	public static AntlrContext fromInputFile(InputFile file, Charset charset) throws IOException {
		return fromStreams(file, file.inputStream(), file.inputStream(), charset);
	}

	public static AntlrContext fromStreams(InputFile inputFile, InputStream file, InputStream linesStream,
			Charset charset) throws IOException {
		final SourceLinesProvider linesProvider = new SourceLinesProvider();
		final CharStream charStream = new CaseChangingCharStream(CharStreams.fromStream(file, charset), true);
		final TSqlLexer lexer = new TSqlLexer(charStream);
		lexer.removeErrorListeners();
		final CommonTokenStream stream = new CommonTokenStream(lexer);
		stream.fill();
		final TSqlParser parser = new TSqlParser(stream);
		parser.removeErrorListeners();
		final ParseTree root = parser.tsql_file();
		final SourceLine[] lines = linesProvider.getLines(linesStream, charset);
		return new AntlrContext(inputFile, stream, root, lines);
	}
}
