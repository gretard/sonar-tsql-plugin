package org.sonar.plugins.tsql.sensors.antlr4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.sonar.api.config.Settings;

public class LinesProvider {

	public LinesProvider() {

	}

	public SourceLine[] getLines(InputStream file, Charset charset) {
		final List<SourceLine> sourceLines = new ArrayList<>();

		try {
			char bom = '\ufeff';
			int totalLines = 1;
			int global = 0;
			int count = 0;
			BufferedReader br = new BufferedReader(new InputStreamReader(file, charset));
			  int r;
			  while ((r = br.read()) != -1) {
		            if (r == bom) {
		            	continue;
		            }
		            global++;
		            count++;
		            if (r == 10) {
		            	
		            	sourceLines.add(new SourceLine(totalLines, count, global- count, global));
						
		            	totalLines++;
		            	count = 0;
		            }
		       
		        }
			  sourceLines.add(new SourceLine(totalLines, count, global- count, global));
				
			  
			/*String line;
			while ((line = br.readLine()) != null) {
				int count = line.length();
				System.out.println(line);
				
				sourceLines.add(new SourceLine(totalLines, count, global, global + count));
				global += (count+1);
				totalLines++;
			}*/
			  file.close();
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * try { BufferedReader br = new BufferedReader(new Innew
		 * FileInputStream(file));
		 * 
		 * String readLine; int line = 1; int global = 0; while ((readLine =
		 * br.readLine()) != null) { int count = readLine.length();
		 * lines.add(new SourceLine(line, count, global, global + count));
		 * global += count; line++; } br.close(); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
		return sourceLines.toArray(new SourceLine[0]);
	}

	public static class SourceLine {
		@Override
		public String toString() {
			return "SourceLine [line=" + line + ", count=" + count + ", start=" + start + ", end=" + end + "]";
		}

		private final int line;

		public int getLine() {
			return line;
		}

		public int getCount() {
			return count;
		}

		public int getEnd() {
			return end;
		}

		public int getStart() {
			return start;
		}

		private final int count;
		private final int start;
		private final int end;

		public SourceLine(int line, int count, int start, int end) {
			this.line = line;
			this.count = count;
			this.start = start;
			this.end = end;

		}
	}
}
