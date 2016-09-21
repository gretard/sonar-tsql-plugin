package org.sonar.plugins.tsql.rules;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.rules.files.BaseReportsProvider;

public class BaseReportsProviderTest {

	@Test
	public void testGetNonExistingDir() {
		BaseReportsProvider cut = new BaseReportsProvider("./test", "test.xml");
		List<File> files = cut.get();
		Assert.assertEquals(0, files.size());
	}

	@Test
	public void testGetFilesInExistingDir() {
		String file = this.getClass().getClassLoader().getResource(".").getFile();

		BaseReportsProvider cut = new BaseReportsProvider(file, ".xml");
		List<File> files = cut.get();
		Assert.assertEquals(2, files.size());
	}

	@Test
	public void testGetSpecificFile() {
		String file = this.getClass().getClassLoader().getResource(".").getFile();

		BaseReportsProvider cut = new BaseReportsProvider(file, "Gsample.xml");
		List<File> files = cut.get();
		Assert.assertEquals(1, files.size());
	}
}