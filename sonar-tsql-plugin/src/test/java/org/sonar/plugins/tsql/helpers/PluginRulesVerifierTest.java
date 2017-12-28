package org.sonar.plugins.tsql.helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.config.Settings;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.sensors.HighlightingSensor;

public class PluginRulesVerifierTest {

	@Test
	public void run() throws IOException {
		TemporaryFolder folder = new TemporaryFolder();
		folder.create();
		Settings settings = new Settings();
		settings.setProperty(Constants.PLUGIN_SKIP_CUSTOM_RULES, false);
		String dirPath = "..\\grammars\\tsql";
		File dir = new File(dirPath);
		Collection<File> files = FileUtils.listFiles(dir, new String[] { "sql" }, true);
		SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());
		for (File f : files) {
			String tempName = f.getName() + System.nanoTime();
			File dest = folder.newFile(tempName);
			FileUtils.copyFile(f, dest);

			DefaultInputFile file1 = new DefaultInputFile("test", tempName);
			file1.initMetadata(new String(Files.readAllBytes(f.toPath())));
			file1.setLanguage(TSQLLanguage.KEY);
			ctxTester.fileSystem().add(file1);

		}
		HighlightingSensor sensor = new HighlightingSensor(settings);
		sensor.execute(ctxTester);
		Collection<Issue> issues = ctxTester.allIssues();
		// System.out.println(files.size() + " " + issues.size() + " " + took);
		Assert.assertEquals(97, issues.size());

	}

}
