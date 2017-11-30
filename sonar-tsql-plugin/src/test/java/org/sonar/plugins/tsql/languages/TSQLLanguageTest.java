package org.sonar.plugins.tsql.languages;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.plugins.tsql.Constants;

public class TSQLLanguageTest {

	@Test
	public void testDefaultSuffixes() {
		final Settings settings = new Settings();
		final TSQLLanguage language = new TSQLLanguage(settings);
		Assert.assertArrayEquals(new String[] { ".sql" }, language.getFileSuffixes());
	}

	@Test
	public void testDefinedSuffixes() {
		final Settings settings = new Settings();
		settings.setProperty(Constants.PLUGIN_SUFFIXES, ".sql,.test");
		final TSQLLanguage language = new TSQLLanguage(settings);
		Assert.assertArrayEquals(new String[] { ".sql", ".test" }, language.getFileSuffixes());
	}

}
