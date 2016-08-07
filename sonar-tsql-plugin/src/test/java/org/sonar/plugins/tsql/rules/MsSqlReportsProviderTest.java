package org.sonar.plugins.tsql.rules;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

;

public class MsSqlReportsProviderTest {

	@Test
	public void testGet() {
		MsSqlReportsProvider cut = new MsSqlReportsProvider("test.xml");
		List<File> files = cut.get(".");
		Assert.assertEquals(0, files.size());
	}

	@Test
	public void testNonExistingDirectory() {
		MsSqlReportsProvider cut = new MsSqlReportsProvider("test.xml");
		List<File> files = cut.get("./test");
		Assert.assertEquals(0, files.size());
	}
}
