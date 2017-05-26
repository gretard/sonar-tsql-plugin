package org.sonar.plugins.tsql.sensors;

import java.io.File;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.internal.JUnitTempFolder;
import org.sonar.plugins.tsql.languages.TSQLLanguage;

public class HighlightingSensorTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@org.junit.Rule
	public JUnitTempFolder temp = new JUnitTempFolder();

	@SuppressWarnings("deprecation")
	@Test
	public void testHighlighting() throws Throwable {

		File baseFile = folder.newFile("test.sql");

		FileUtils.copyURLToFile(getClass().getResource("/testFiles/TestTable.sql"), baseFile);

		DefaultInputFile file1 = new DefaultInputFile("test", "test.sql");

		file1.initMetadata(new String(Files.readAllBytes(baseFile.toPath())));
		file1.setLanguage(TSQLLanguage.KEY);

		DefaultInputFile file2 = new DefaultInputFile("test", "test.php");
		file2.setLanguage("php");
		SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());
		ctxTester.fileSystem().add(file1);
		ctxTester.fileSystem().add(file2);
		HighlightingSensor sensor = new HighlightingSensor(new Settings());
		sensor.execute(ctxTester);

		Assert.assertEquals(1, ctxTester.highlightingTypeAt("test:test.sql", 1, 0).size());
		Assert.assertEquals(TypeOfText.KEYWORD, ctxTester.highlightingTypeAt("test:test.sql", 1, 0).get(0));
		Assert.assertEquals(1, ctxTester.highlightingTypeAt("test:test.sql", 2, 0).size());
		Assert.assertEquals(TypeOfText.COMMENT, ctxTester.highlightingTypeAt("test:test.sql", 2, 0).get(0));
		Assert.assertEquals(1, ctxTester.highlightingTypeAt("test:test.sql", 5, 0).size());
		Assert.assertEquals(TypeOfText.STRING, ctxTester.highlightingTypeAt("test:test.sql", 5, 0).get(0));

		Assert.assertEquals(17, ctxTester.cpdTokens("test:test.sql").size());

	}

}
