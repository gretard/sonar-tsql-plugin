package org.sonar.plugins.tsql.coverage;

public class CoveredLinesReport {

	private final String file;

	private final LineInfo[] hitLines;

	public CoveredLinesReport(String file, LineInfo... infos) {
		this.file = file;
		this.hitLines = infos;
	}

	public String getFile() {
		return file;
	}

	public LineInfo[] getHitLines() {
		return hitLines;
	}

	public static class LineInfo {
		private final int line;
		private final int hitsCount;

		public LineInfo(final int line, final int hits) {
			this.line = line;
			this.hitsCount = hits;
		}

		public int getLine() {
			return line;
		}

		public int getHitsCount() {
			return hitsCount;
		}

	}
}
