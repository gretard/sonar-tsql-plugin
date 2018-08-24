package org.sonar.plugins.tsql.rules.files;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FilesProviderTest {

	FilesProvider sut = new FilesProvider();
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testNonExisting() {
		File[] files = sut.getFiles("test", "test", ".");
		Assert.assertEquals(0, files.length);
	}

	@Test
	public void testDefaultValues() throws IOException {
		folder.newFile("aatest.xml");
		File[] files = sut.getFiles("test.xml", "test.xml", folder.getRoot().getAbsolutePath());
		Assert.assertEquals(1, files.length);

	}

	@Test
	public void testFolderSpecified() throws IOException {
		folder.newFile("aatest.xml");
		File fsFolder = folder.newFolder("sample");
		File[] files = sut.getFiles("test.xml", folder.getRoot().getAbsolutePath(), fsFolder.getAbsolutePath());
		Assert.assertEquals(1, files.length);
	}

	@Test
	public void testSearchPathSpecified() throws IOException {
		folder.newFile("aatest.xml");
		File[] files = sut.getFiles("samplePrefix.xml", "test.xml", folder.getRoot().getAbsolutePath());
		Assert.assertEquals(1, files.length);
	}

	@Test
	public void testNonExistingSearchPathSpecified() throws IOException {
		folder.newFile("aatest.xml");
		File[] files = sut.getFiles("samplePrefix.xml", "a.xml", folder.getRoot().getAbsolutePath());
		Assert.assertEquals(0, files.length);
	}
	
	@Test
	public void testFindMultiple() throws IOException {
		folder.newFile("aatest.xml");
		folder.newFile("aaBsssjtest.xml");
		
		File testFolder = folder.newFolder("test");
		new File(testFolder, "0test.xml").createNewFile();
		new File(testFolder, "0tes.xml").createNewFile();
		File[] files = sut.getFiles("test.xml", "test.xml", folder.getRoot().getAbsolutePath());
		Assert.assertEquals(3, files.length);
	}
}
