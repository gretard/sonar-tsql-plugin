package org.sonar.plugins.tsql.sensors;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.coverage.SqlCoverCoverageProvider;
import org.sonar.plugins.tsql.languages.TSQLLanguage;

public class CoverageSensorTest {

	@Test
	public void test() throws Throwable {
		TemporaryFolder folder = new TemporaryFolder();
		folder.create();

		SensorContextTester ctxTester = SensorContextTester.create(folder.getRoot());
		String tempName = "GetStatusMessage.sql";
		String covReport = "test.xml";
		File f = folder.newFile(tempName);
		File coverage = folder.newFile(covReport);

		FileUtils.copyInputStreamToFile(this.getClass().getResourceAsStream("/coverage/Coverage.opencoverxml"),
				coverage);

		FileUtils.copyInputStreamToFile(this.getClass().getResourceAsStream("/coverage/TestCode.sql"), f);
		DefaultInputFile file1 = new TestInputFileBuilder(folder.getRoot().getAbsolutePath(), tempName)
				.initMetadata(new String(Files.readAllBytes(f.toPath()))).setLanguage(TSQLLanguage.KEY).build();
		ctxTester.fileSystem().add(file1);
		ctxTester.settings().setProperty(Constants.COVERAGE_FILE, coverage.getAbsolutePath());
		ctxTester.settings().setProperty(Constants.PLUGIN_SKIP_COVERAGE, false);
		CoverageSensor sut = new CoverageSensor(new SqlCoverCoverageProvider(ctxTester.settings(), ctxTester.fileSystem()));
		sut.execute(ctxTester);
		Assert.assertEquals((int) 2, (int) ctxTester.lineHits(file1.key(), 7));

	}
	@Test
	public void test2() throws Throwable {
		List<String> names = Arrays.asList("dbo.test", "test", "other.test");
		
		String name = "test";
		
		if (names.contains(name)) {
			
		}
	}
	
}
