package org.sonar.plugins.tsql.antlr;

import java.io.File;
import java.io.IOException;

import org.antlr.v4.runtime.Token;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.plugins.tsql.helpers.AntlrUtils;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.lines.SourceLine;

public class FillerRequestTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void compareLinesWithAntrl() {
		String s = "select " + "*" + "from dbo.test";
		FillerRequest result = AntlrUtils.getRequest(s);
		for (Token t : result.getStream().getTokens()) {
			if (t.getType() == Token.EOF) {
				continue;
			}
			int[] start = result.getLineAndColumn(t.getStartIndex());
			int[] end = result.getLineAndColumn(t.getStopIndex());
			Assert.assertNotNull(start);
			Assert.assertNotNull(end);
			Assert.assertEquals(t.getLine(), start[0]);
			Assert.assertEquals(t.getCharPositionInLine(), start[1]);
		}
	}

	@Test
	public void compareWithSonarInputFile() throws IOException {
		String s = "select " + "*" + "from dbo.test\r\nselect 1";
		File ff = folder.newFile("test.sql");
		FileUtils.write(ff, s);
		DefaultInputFile f = new TestInputFileBuilder("", "test.sql").setLanguage(TSQLLanguage.KEY).initMetadata(s)
				.build();
		FillerRequest result = AntlrUtils.getRequest(s);

		for (Token t : result.getStream().getTokens()) {
			if (t.getType() == Token.EOF) {
				continue;
			}
			int[] start = result.getLineAndColumn(t.getStartIndex());
			int[] end = result.getLineAndColumn(t.getStopIndex());
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
	public void testLinesCalculation1() {
		FillerRequest file = new FillerRequest(null, null, null, new SourceLine[] { new SourceLine(1, 10, 0, 10),
				new SourceLine(2, 10, 10, 20), new SourceLine(3, 10, 20, 30)

		});

		int[] result = file.getLineAndColumn(4);
		Assert.assertArrayEquals(new int[] { 1, 4 }, result);
	}

	@Test
	public void testLinesCalculation2() {
		FillerRequest file = new FillerRequest(null, null, null, new SourceLine[] { new SourceLine(1, 10, 0, 10),
				new SourceLine(2, 10, 10, 20), new SourceLine(3, 10, 20, 30)

		});

		int[] result = file.getLineAndColumn(12);
		Assert.assertArrayEquals(new int[] { 2, 2 }, result);
	}
}
