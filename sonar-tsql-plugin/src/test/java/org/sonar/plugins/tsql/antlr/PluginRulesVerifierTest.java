package org.sonar.plugins.tsql.antlr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
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
import org.sonar.plugins.tsql.sensors.HighlightingSensor;

public class PluginRulesVerifierTest {

	@Test
	public void run() throws IOException {
		TemporaryFolder folder = new TemporaryFolder();
		folder.create();
		Settings settings = new MapSettings();
		settings.setProperty(Constants.PLUGIN_SKIP_CUSTOM_RULES, false);
		String dirPath = "..\\grammars\\tsql";
		File dir = new File(dirPath);
		Collection<File> files = FileUtils.listFiles(dir, new String[] { "sql" }, true);
		SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());
		for (File f : files) {
			String tempName = f.getName() + System.nanoTime();
			File dest = folder.newFile(tempName);
			FileUtils.copyFile(f, dest);

			DefaultInputFile file1 = new TestInputFileBuilder(folder.getRoot().getAbsolutePath(), tempName)
					.initMetadata(new String(Files.readAllBytes(f.toPath()))).setLanguage(TSQLLanguage.KEY).build();
			ctxTester.fileSystem().add(file1);

		}
		HighlightingSensor sensor = new HighlightingSensor(settings);
		sensor.execute(ctxTester);
		Collection<Issue> issues = ctxTester.allIssues();
		Assert.assertEquals(97, issues.size());

	}

}
