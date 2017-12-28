package org.sonar.plugins.tsql.sensors.antlr4;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.antlr.v4.runtime.Token;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.helpers.AntrlResult;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.sensors.custom.lines.SourceLinesProvider;
import org.sonar.plugins.tsql.sensors.custom.lines.SourceLine;

public class AntrlFileTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void compareWithAntrl() {
		String s = "select " + "*" + "from dbo.test";
		AntrlResult result = Antlr4Utils.getFull(s);
		SourceLinesProvider p = new SourceLinesProvider();
		SourceLine[] lines = p.getLines(new StringBufferInputStream(s), Charset.defaultCharset());
		FillerRequest file = new FillerRequest(null, null, result.getTree(), lines);
		for (Token t : result.getStream().getTokens()) {
			if (t.getType() == Token.EOF) {
				continue;
			}
			int[] start = file.getLineAndColumn(t.getStartIndex());
			int[] end = file.getLineAndColumn(t.getStopIndex());
			Assert.assertNotNull(start);
			Assert.assertNotNull(end);
			Assert.assertEquals(t.getLine(), start[0]);
			System.out.println(t.getText() + Arrays.toString(start) + " " + t.getCharPositionInLine() + " "
					+ t.getLine() + " " + Arrays.toString(end));
			Assert.assertEquals(t.getCharPositionInLine(), start[1]);
		}
	}
	@Test
	public void compareWithDefaultInputFile() throws IOException {
		String s = "select " + "*" + "from dbo.test\r\nselect 1";
		File ff = folder.newFile("test.sql");
		FileUtils.write(ff, s);
		DefaultInputFile f = new DefaultInputFile("test", "test.sql");
		f.initMetadata(s);
		f.setLanguage(TSQLLanguage.KEY);
		
		AntrlResult result = Antlr4Utils.getFull(s);
		SourceLinesProvider p = new SourceLinesProvider();
		SourceLine[] lines = p.getLines(new FileInputStream(ff), Charset.defaultCharset());
		FillerRequest file = new FillerRequest(null, null, result.getTree(), lines);
		for (SourceLine l : lines) {
			System.out.println(l);
		}
		for (Token t : result.getStream().getTokens()) {
			if (t.getType() == Token.EOF) {
				continue;
			}
			int[] start = file.getLineAndColumn(t.getStartIndex());
			int[] end = file.getLineAndColumn(t.getStopIndex());
			TextPointer p1 = f.newPointer(t.getStartIndex());
			TextPointer p2 = f.newPointer(t.getStopIndex());
			Assert.assertNotNull(start);
			Assert.assertNotNull(end);
			Assert.assertEquals(p1.line(), start[0]);
			Assert.assertEquals(p1.lineOffset(), start[1]);
			Assert.assertEquals(p2.line(), end[0]);
			Assert.assertEquals(p2.lineOffset(), end[1]);
		}
	}
	@Test
	public void test() {
		FillerRequest file = new FillerRequest(null, null, null, new SourceLine[] { new SourceLine(1, 10, 0, 10),
				new SourceLine(2, 10, 10, 20), new SourceLine(3, 10, 20, 30)

		});

		int[] result = file.getLineAndColumn(4);
		Assert.assertArrayEquals(new int[] { 1, 4 }, result);
	}

	@Test
	public void test2() {
		FillerRequest file = new FillerRequest(null, null, null, new SourceLine[] { new SourceLine(1, 10, 0, 10),
				new SourceLine(2, 10, 10, 20), new SourceLine(3, 10, 20, 30)

		});

		int[] result = file.getLineAndColumn(12);
		Assert.assertArrayEquals(new int[] { 2, 2 }, result);
	}
}
