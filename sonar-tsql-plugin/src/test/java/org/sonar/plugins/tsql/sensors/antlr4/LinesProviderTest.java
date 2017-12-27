package org.sonar.plugins.tsql.sensors.antlr4;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.plugins.tsql.sensors.antlr4.LinesProvider.SourceLine;

public class LinesProviderTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testGetLines() throws IOException {
		folder.create();
		String s = "select * from dbo.test\r\n"
				+ "--test";
		File ff = folder.newFile("test.sql");
		FileUtils.write(ff, s);
		LinesProvider sut = new LinesProvider();
		SourceLine[] results = sut.getLines(new FileInputStream(ff), Charset.defaultCharset());
		Assert.assertEquals(2, results.length);
		
		Assert.assertEquals(24, results[0].getCount());

	}

}
