package org.sonar.plugins.tsql.sensors.antlr4;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.tsql.sensors.custom.lines.SourceLine;

public class FillerRequest {
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

	public int[] getLineAndColumn(int global) {

		for (SourceLine line : this.lines) {
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

	public ParseTree getRoot() {
		return root;
	}

	public FillerRequest(InputFile file, CommonTokenStream stream, ParseTree root, SourceLine[] lines) {
		this.file = file;
		this.stream = stream;
		this.root = root;
		this.lines = lines;
	}
}
