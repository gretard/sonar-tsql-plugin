package org.sonar.plugins.tsql.sensors;

import java.io.File;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.internal.JUnitTempFolder;
import org.sonar.plugins.tsql.rules.issues.IIssuesProvider;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class BaseTsqlSensorTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@org.junit.Rule
	public JUnitTempFolder temp = new JUnitTempFolder();

	@Test
	public void testAdd() throws Throwable {

		SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());

		File baseFile = folder.newFile("test.sql");

		FileUtils.copyURLToFile(getClass().getResource("/testFiles/TestTable.sql"), baseFile);

		DefaultFileSystem fs = new DefaultFileSystem(folder.getRoot());

		DefaultInputFile ti = new DefaultInputFile("test", "test.sql");
		ti.initMetadata(new String(Files.readAllBytes(baseFile.toPath())));

		fs.add(ti);
		final TsqlIssue issue = new TsqlIssue();
		issue.setFilePath(ti.absolutePath());
		issue.setLine(2);
		issue.setType("test");
		issue.setDescription("test123");
		IIssuesProvider provider = new IIssuesProvider() {

			@Override
			public TsqlIssue[] getIssues() {
				return new TsqlIssue[] {

						issue };
			}

		};
		Settings settings = new Settings();
		BaseTsqlSensor sensor = new BaseTsqlSensor(settings, fs, provider, "test") {
		};
		sensor.execute(ctxTester);
		Assert.assertEquals(1, ctxTester.allIssues().size());
	}

	@Test
	public void testAddLine0() throws Throwable {

		SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());

		File baseFile = folder.newFile("test.sql");

		FileUtils.copyURLToFile(getClass().getResource("/testFiles/TestTable.sql"), baseFile);

		DefaultFileSystem fs = new DefaultFileSystem(folder.getRoot());

		DefaultInputFile ti = new DefaultInputFile("test", "test.sql");
		ti.initMetadata(new String(Files.readAllBytes(baseFile.toPath())));

		fs.add(ti);
		final TsqlIssue issue = new TsqlIssue();
		issue.setFilePath(ti.absolutePath());
		issue.setLine(0);
		issue.setType("test");
		issue.setDescription("test123");
		IIssuesProvider provider = new IIssuesProvider() {

			@Override
			public TsqlIssue[] getIssues() {
				return new TsqlIssue[] {

						issue };
			}

		};
		Settings settings = new Settings();
		BaseTsqlSensor sensor = new BaseTsqlSensor(settings, fs, provider, "test") {
		};
		sensor.execute(ctxTester);
		Assert.assertEquals(0, ctxTester.allIssues().size());
	}

	@Test
	public void testAddDescNull() throws Throwable {

		SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());

		File baseFile = folder.newFile("test.sql");

		FileUtils.copyURLToFile(getClass().getResource("/testFiles/TestTable.sql"), baseFile);

		DefaultFileSystem fs = new DefaultFileSystem(folder.getRoot());

		DefaultInputFile ti = new DefaultInputFile("test", "test.sql");
		ti.initMetadata(new String(Files.readAllBytes(baseFile.toPath())));

		fs.add(ti);
		final TsqlIssue issue = new TsqlIssue();
		issue.setFilePath(ti.absolutePath());
		issue.setLine(1);
		issue.setType("test");
		IIssuesProvider provider = new IIssuesProvider() {

			@Override
			public TsqlIssue[] getIssues() {
				return new TsqlIssue[] {

						issue };
			}

		};
		Settings settings = new Settings();
		BaseTsqlSensor sensor = new BaseTsqlSensor(settings, fs, provider, "test") {
		};
		sensor.execute(ctxTester);
		Assert.assertEquals(1, ctxTester.allIssues().size());
	}
}
