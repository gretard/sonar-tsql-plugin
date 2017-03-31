package org.sonar.plugins.tsql.rules.files;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.utils.internal.JUnitTempFolder;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.rules.parsers.SqlCodeGuardIssuesParser;

public class CgIssuesProviderTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@org.junit.Rule
	public JUnitTempFolder temp = new JUnitTempFolder();

	@Test
	public void testGet() throws IOException {
		File baseFile = folder.newFile("sp_Get.sql");
		FileUtils.copyURLToFile(getClass().getResource("/testFiles/sp_Get.sql"), baseFile);

		DefaultFileSystem fs = new DefaultFileSystem(folder.getRoot());

		CgIssuesProvider provider = new CgIssuesProvider(null, fs, temp);
		File[] files = provider.get();
		Assert.assertEquals(1, files.length);

		SqlCodeGuardIssuesParser parser = new SqlCodeGuardIssuesParser();

		TsqlIssue[] issues = parser.parse(files[0]);
		Assert.assertEquals(8, issues.length);
	}

}
