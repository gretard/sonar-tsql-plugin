package org.sonar.plugins.tsql.lines;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SourceLinesProviderTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@SuppressWarnings("deprecation")
	@Test
	public void testGetLines() throws IOException {
		folder.create();
		String s = "select * from dbo.test\r\n" + "--test";
		File ff = folder.newFile("test.sql");
		FileUtils.write(ff, s);
		SourceLinesProvider sut = new SourceLinesProvider();
		SourceLine[] results = sut.getLines(new FileInputStream(ff), Charset.defaultCharset());
		Assert.assertEquals(2, results.length);
		Assert.assertEquals(24, results[0].getCount());

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetLines2() throws IOException {
		SourceLinesProvider sut = new SourceLinesProvider();
		SourceLine[] results = sut.getLines(null, Charset.defaultCharset());
		Assert.assertEquals(0, results.length);

	}
}
