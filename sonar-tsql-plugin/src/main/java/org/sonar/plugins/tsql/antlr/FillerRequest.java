package org.sonar.plugins.tsql.antlr;

import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.tsql.lines.SourceLine;

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

	public SourceCodeMeasures getMeasures() {
		final int[] total = new int[getLines().length];
		final Token[] alltokens = getTokens();

		for (final Token token : alltokens) {
			int startLine = token.getLine();
			int[] endLines = getLineAndColumn(token.getStopIndex());
			if (endLines == null || token.getStartIndex() >= token.getStopIndex()) {
				continue;
			}
			if (token.getType() == TSqlParser.EOF || token.getType() == TSqlParser.COMMENT
					|| token.getType() == TSqlParser.LINE_COMMENT) {
				continue;
			}
			for (int i = startLine - 1; i < endLines[0]; i++) {
				total[i] = 1;
			}

		}
		for (final Token token : alltokens) {
			int startLine = token.getLine();
			int[] endLines = getLineAndColumn(token.getStopIndex());
			if (token.getType() == TSqlParser.EOF || endLines == null
					|| token.getStartIndex() >= token.getStopIndex()) {
				continue;
			}
			if (token.getType() == TSqlParser.COMMENT || token.getType() == TSqlParser.LINE_COMMENT) {
				for (int i = startLine - 1; i < endLines[0]; i++) {
					if (total[i] == 0) {
						total[i] = 2;
					}

				}
			}

		}
		int comments = 0;
		int locs = 0;
		for (int x : total) {
			if (x == 1) {
				locs++;
				continue;
			}
			if (x == 2) {
				comments++;
			}
		}
		return new SourceCodeMeasures(locs, comments, lines.length);
	}

	public static class SourceCodeMeasures {
		private final int locs;
		private final int comments;
		private final int total;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + comments;
			result = prime * result + locs;
			result = prime * result + total;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SourceCodeMeasures other = (SourceCodeMeasures) obj;
			if (comments != other.comments)
				return false;
			if (locs != other.locs)
				return false;
			if (total != other.total)
				return false;
			return true;
		}

		public int getLocs() {
			return locs;
		}

		public int getComments() {
			return comments;
		}

		public int getTotal() {
			return total;
		}

		public SourceCodeMeasures(int locs, int comments, int total) {
			this.locs = locs;
			this.comments = comments;
			this.total = total;

		}
	}

}
