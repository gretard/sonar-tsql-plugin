package org.sonar.plugins.tsql.sensors.antlr4;

import java.io.StringBufferInputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.antlr.v4.runtime.Token;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.helpers.AntrlResult;
import org.sonar.plugins.tsql.sensors.antlr4.LinesProvider.SourceLine;

public class AntrlFileTest {

	
	@Test
	public void compareWithAntrl() {
		String s = "select "
				+ "*"
				+ "from dbo.test";
		AntrlResult result = Antlr4Utils.getFull(s);
		LinesProvider p = new LinesProvider();
		SourceLine[] lines = p.getLines(new StringBufferInputStream(s), Charset.defaultCharset());
		AntrlFile file = new AntrlFile(null, null,result.getTree(), lines);
		for (Token t : result.getStream().getTokens()) {
			int[] start = file.getLineAndColumn(t.getStartIndex());
			int[] end = file.getLineAndColumn(t.getStopIndex());
			Assert.assertNotNull(start);
			Assert.assertNotNull(end);
			Assert.assertEquals(t.getLine(), start[0]);
			System.out.println(t.getText()+Arrays.toString(start)+" "+t.getCharPositionInLine()+" "+t.getLine()+" "+Arrays.toString(end));
			Assert.assertEquals(t.getCharPositionInLine(), start[1]);
		}
	}
	@Test
	public void test() {
		AntrlFile file = new AntrlFile(null, null,null, new SourceLine[] { new SourceLine(1, 10, 0, 10),
				new SourceLine(2, 10, 10, 20), new SourceLine(3, 10, 20, 30)

		});

		int[] result = file.getLineAndColumn(4);
		Assert.assertArrayEquals(new int[] { 1, 4 }, result);
	}
	@Test
	public void test2() {
		AntrlFile file = new AntrlFile(null, null,null, new SourceLine[] { new SourceLine(1, 10, 0, 10),
				new SourceLine(2, 10, 10, 20), new SourceLine(3, 10, 20, 30)

		});

		int[] result = file.getLineAndColumn(12);
		Assert.assertArrayEquals(new int[] { 2, 2 }, result);
	}
}
