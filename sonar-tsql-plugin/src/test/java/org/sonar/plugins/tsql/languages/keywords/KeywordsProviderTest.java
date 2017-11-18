package org.sonar.plugins.tsql.languages.keywords;

import org.junit.Assert;
import org.junit.Test;

public class KeywordsProviderTest {

	@Test
	public void test() {
		KeywordsProvider provider = new KeywordsProvider();
		Assert.assertFalse(provider.isKeyword("test"));
	}
	
	@Test
	public void testKeyword() {
		KeywordsProvider provider = new KeywordsProvider();
		Assert.assertTrue(provider.isKeyword("SELECT"));
	}
	
	@Test
	public void testInitializeNonExisting() {
		KeywordsProvider provider = new KeywordsProvider("nonExisting");
		Assert.assertFalse(provider.isKeyword("SELECT"));
	}

}
