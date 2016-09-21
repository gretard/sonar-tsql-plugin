package org.sonar.plugins.tsql.languages;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.AbstractLanguage;

public final class TSQLLanguage extends AbstractLanguage {

	public static final String NAME = "T-SQL";
	public static final String KEY = "tsql";
	public static final String FILE_SUFFIXES_PROPERTY_KEY = "sonar.tsql.file.suffixes";
	public static final String DEFAULT_FILE_SUFFIXES = "sql";

	private final Settings settings;

	public TSQLLanguage(final Settings settings) {
		super(KEY, NAME);
		this.settings = settings;
	}

	@Override
	public String[] getFileSuffixes() {
		String[] suffixes = filterEmptyStrings(settings.getStringArray(FILE_SUFFIXES_PROPERTY_KEY));
		if (suffixes.length == 0) {
			suffixes = StringUtils.split(DEFAULT_FILE_SUFFIXES, ",");
		}
		return suffixes;
	}

	private String[] filterEmptyStrings(String[] stringArray) {
		final List<String> nonEmptyStrings = new ArrayList<>();
		for (String string : stringArray) {
			if (StringUtils.isNotBlank(string.trim())) {
				nonEmptyStrings.add(string.trim());
			}
		}
		return nonEmptyStrings.toArray(new String[nonEmptyStrings.size()]);
	}

}
