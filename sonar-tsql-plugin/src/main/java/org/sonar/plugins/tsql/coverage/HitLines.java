package org.sonar.plugins.tsql.coverage;

public class HitLines {

	String file;
	
	
	LineInfo[] hitLines;
	
	
	public String getFile() {
		return file;
	}

	public LineInfo[] getHitLines() {
		return hitLines;
	}

	public HitLines(String file, LineInfo ...infos) {
		this.file = file;
		this.hitLines = infos;
	}
	
	public static class LineInfo {
		int line;
		int hitsCount;
		public int getLine() {
			return line;
		}
		public int getHitsCount() {
			return hitsCount;
		}
		public LineInfo(int line, int hits) {
			this.line = line;
			this.hitsCount = hits;
		}
	}
}
