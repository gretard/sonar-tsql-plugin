package org.sonar.plugins.tsql.languages;

import org.sonar.api.config.Settings;
import org.sonar.api.resources.AbstractLanguage;
import org.sonar.plugins.tsql.Constants;

public final class TSQLLanguage extends AbstractLanguage {

	public static final String NAME = "T-SQL";

	public static final String KEY = "tsql";

	public static final String[] DEFAULT_FILE_SUFFIXES = new String[] { ".sql" };

	private final Settings settings;

	public TSQLLanguage(final Settings settings) {
		super(KEY, NAME);
		this.settings = settings;

	}

	public String[] getFileSuffixes() {
		final String[] suffixes = settings.getStringArray(Constants.PLUGIN_SUFFIXES);
		if (suffixes == null || suffixes.length == 0) {
			return DEFAULT_FILE_SUFFIXES;
		}
		return suffixes;

	}

}
