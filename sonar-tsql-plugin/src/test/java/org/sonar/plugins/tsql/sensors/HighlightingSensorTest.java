package org.sonar.plugins.tsql.sensors;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.config.MapSettings;
import org.sonar.api.config.Settings;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.languages.TSQLLanguage;

public class HighlightingSensorTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testSingleFile() throws IOException {
		TemporaryFolder folder = new TemporaryFolder();
		folder.create();
		Settings settings = new MapSettings();
		settings.setProperty(Constants.PLUGIN_SKIP_CUSTOM_RULES, false);
		SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());
		String tempName = "test.sql";

		File f = folder.newFile(tempName);

		FileUtils.write(f, "select * from dbo.test;\r\n--test", Charset.defaultCharset());
		DefaultInputFile file1 = new TestInputFileBuilder(folder.getRoot().getAbsolutePath(), tempName)
				.initMetadata(new String(Files.readAllBytes(f.toPath()))).setLanguage(TSQLLanguage.KEY).build();
		ctxTester.fileSystem().add(file1);

		CustomChecksSensor sensor = new CustomChecksSensor(settings);
		sensor.execute(ctxTester);
		Collection<Issue> issues = ctxTester.allIssues();
		Assert.assertEquals(1, issues.size());
		Assert.assertEquals(2, ctxTester.cpdTokens(file1.key()).size());
		Assert.assertEquals(1, ctxTester.highlightingTypeAt(file1.key(), 2, 1).size());
		Assert.assertEquals(4, ctxTester.measures(file1.key()).size());
	}

	@Test
	public void testTSQLGrammarFiles() throws IOException {
		TemporaryFolder folder = new TemporaryFolder();
		folder.create();
		Settings settings = new MapSettings();
		settings.setProperty(Constants.PLUGIN_SKIP_CUSTOM_RULES, false);
		settings.setProperty(Constants.PLUGIN_SKIP_CUSTOM, false);
		settings.setProperty(Constants.PLUGIN_SKIP, false);

		settings.setProperty(Constants.PLUGIN_SKIP_CUSTOM_RULES, false);
		
		settings.setProperty(Constants.PLUGIN_MAX_FILE_SIZE, 10);
		String dirPath = "..\\grammars\\tsql";
		File dir = new File(dirPath);
		Collection<File> files = FileUtils.listFiles(dir, new String[] { "sql" }, true);
		SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());
		ctxTester.setSettings(settings);
		for (File f : files) {
			String tempName = f.getName() + System.nanoTime();
			File dest = folder.newFile(tempName);
			FileUtils.copyFile(f, dest);

			DefaultInputFile file1 = new TestInputFileBuilder(folder.getRoot().getAbsolutePath(), tempName)
					.initMetadata(new String(Files.readAllBytes(f.toPath()))).setLanguage(TSQLLanguage.KEY).build();
			ctxTester.fileSystem().add(file1);

		}
		CustomChecksSensor sensor = new CustomChecksSensor(settings);
		sensor.execute(ctxTester);
		Collection<Issue> issues = ctxTester.allIssues();
		Assert.assertEquals(183, issues.size());

	}
	
}
