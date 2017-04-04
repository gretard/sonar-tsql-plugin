package org.sonar.plugins.tsql.languages;

import org.sonar.api.resources.AbstractLanguage;

public final class TSQLLanguage extends AbstractLanguage {

	public static final String NAME = "T-SQL";

	public static final String KEY = "tsql";

	public static final String[] DEFAULT_FILE_SUFFIXES = new String[] { "sql" };

	public TSQLLanguage() {
		super(KEY, NAME);
	}

	public String[] getFileSuffixes() {
		return DEFAULT_FILE_SUFFIXES;
	}

}
