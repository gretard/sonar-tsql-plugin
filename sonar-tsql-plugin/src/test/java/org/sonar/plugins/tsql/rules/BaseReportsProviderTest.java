package org.sonar.plugins.tsql.rules;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.rules.files.BaseReportsProvider;

public class BaseReportsProviderTest {

	@Test
	public void testGetNonExistingDir() {
		BaseReportsProvider cut = new BaseReportsProvider("test.xml");
		File[] files = cut.get("./test");
		Assert.assertEquals(0, files.length);
	}

	@Test
	public void testGetFilesInExistingDir() {
		String file = this.getClass().getClassLoader().getResource(".").getFile();

		BaseReportsProvider cut = new BaseReportsProvider(".xml");
		File[] files = cut.get(file);
		Assert.assertEquals(5, files.length);
	}

	@Test
	public void testGetSpecificFile() {
		String file = this.getClass().getClassLoader().getResource(".").getFile();

		BaseReportsProvider cut = new BaseReportsProvider("Gsample.xml");
		File[] files = cut.get(file);
		Assert.assertEquals(1, files.length);
	}
}