package org.sonar.plugins.tsql.lines;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SourceLinesProviderTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testGetLines() throws IOException {
		folder.create();
		String s = "select * from dbo.test\r\n" + "--test";
		File ff = folder.newFile("test.sql");
		FileUtils.write(ff, s, StandardCharsets.UTF_8);
		SourceLinesProvider sut = new SourceLinesProvider();
		SourceLine[] results = sut.getLines(new FileInputStream(ff), Charset.defaultCharset());
		Assert.assertEquals(2, results.length);
		Assert.assertEquals(24, results[0].getCount());

	}

	@Test
	public void testGetLines2() throws IOException {
		SourceLinesProvider sut = new SourceLinesProvider();
		SourceLine[] results = sut.getLines(null, Charset.defaultCharset());
		Assert.assertEquals(0, results.length);

	}

	@Test
	public void testGetLinesBOM() throws IOException {
		folder.create();
		File ff = folder.newFile("test.sql");
		BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(ff), StandardCharsets.UTF_8));
		out.write('\ufeff');
		out.write('a');
		out.flush();
		out.close();

		SourceLinesProvider sut = new SourceLinesProvider();
		SourceLine[] results = sut.getLines(new FileInputStream(ff), StandardCharsets.UTF_8);
		Assert.assertEquals(1, results.length);
		Assert.assertEquals(1, results[0].getCount());

	}
}
