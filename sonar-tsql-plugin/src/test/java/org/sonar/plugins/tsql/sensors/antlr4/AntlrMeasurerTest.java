package org.sonar.plugins.tsql.sensors.antlr4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metrics;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.helpers.AntrlResult;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.sensors.antlr4.LinesProvider.SourceLine;


public class AntlrMeasurerTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void test() throws IOException {
		String s = "/** comment **/\r\n"
				+ "select\r\n"
				+ "11\n"
				+ "--aaa\r\n";

		File ff = folder.newFile("test.sql");
		FileUtils.write(ff, s);
		DefaultInputFile f = new DefaultInputFile("test", "test.sql");
		f.initMetadata(s);
		f.setLanguage(TSQLLanguage.KEY);
		SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());
		ctxTester.fileSystem().add(f);
		AntrlResult result = Antlr4Utils.getFull(s);
		AntlrMeasurer m = new AntlrMeasurer();
		SourceLine[] lines = new LinesProvider().getLines(new FileInputStream(ff), Charset.defaultCharset());
		m.work(ctxTester, new AntrlFile(f, result.getStream(),result.getTree(), lines));
		Assert.assertEquals(2, ctxTester.measure("test:test.sql", CoreMetrics.NCLOC).value().intValue());
		Assert.assertEquals(2, ctxTester.measure("test:test.sql", CoreMetrics.COMMENT_LINES).value().intValue());

	}

}
