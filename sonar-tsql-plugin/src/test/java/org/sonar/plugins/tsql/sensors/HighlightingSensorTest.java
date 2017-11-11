package org.sonar.plugins.tsql.sensors;

import java.io.File;
import java.nio.file.Files;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.internal.JUnitTempFolder;
import org.sonar.duplications.internal.pmd.TokensLine;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.languages.TSQLLanguage;

public class HighlightingSensorTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@org.junit.Rule
	public JUnitTempFolder temp = new JUnitTempFolder();

	@Test
	public void testHighlighting() throws Throwable {

		File baseFile0 = temp.newFile("customRulesSample", "xml");

		FileUtils.copyURLToFile(getClass().getResource("/customRulesSample.xml"), baseFile0);

		Settings settings = new Settings();
		settings.setProperty(Constants.PLUGIN_CUSTOM_RULES_PATH, baseFile0.getAbsolutePath());
		settings.setProperty(Constants.PLUGIN_CUSTOM_RULES_PREFIX, "customRulesSample");

		File baseFile = folder.newFile("test.sql");

		FileUtils.copyURLToFile(getClass().getResource("/testFiles/scriptExample.sql"), baseFile);

		DefaultInputFile file1 = new DefaultInputFile("test", "test.sql");

		file1.initMetadata(new String(Files.readAllBytes(baseFile.toPath())));
		file1.setLanguage(TSQLLanguage.KEY);

		DefaultInputFile file2 = new DefaultInputFile("test", "test.php");
		file2.setLanguage("php");
		SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());
		ctxTester.fileSystem().add(file1);
		ctxTester.fileSystem().add(file2);
		HighlightingSensor sensor = new HighlightingSensor(settings);
		sensor.execute(ctxTester);
		Collection<Issue> issues = ctxTester.allIssues();
		for (Issue is : issues) {
			System.out.println(is.ruleKey() + " " + is.primaryLocation().message());
		}
		Assert.assertEquals(9, issues.size());

		// Assert.assertEquals(1, ctxTester.highlightingTypeAt("test:test.sql",
		// 1, 0).size());
		// Assert.assertEquals(TypeOfText.KEYWORD,
		// ctxTester.highlightingTypeAt("test:test.sql", 1, 0).get(0));
		Assert.assertEquals(0, ctxTester.highlightingTypeAt("test:test.sql", 2, 0).size());
		Assert.assertEquals(0, ctxTester.highlightingTypeAt("test:test.sql", 5, 0).size());
		Assert.assertEquals(15, ctxTester.cpdTokens("test:test.sql").size());
		for (TokensLine line : ctxTester.cpdTokens("test:test.sql")) {
			System.out.println(
					" LINE " + line.getValue() + " " + line.getStartUnit() + " " + line.getEndUnit() + line.toString());
		}

	}

}
