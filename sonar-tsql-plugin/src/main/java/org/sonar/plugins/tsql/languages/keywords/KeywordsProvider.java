package org.sonar.plugins.tsql.languages.keywords;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.sensors.HighlightingSensor;

public class KeywordsProvider implements IKeywordsProvider {

	private static final Logger LOGGER = Loggers.get(HighlightingSensor.class);

	private final List<String> keywords = new ArrayList<String>();

	public KeywordsProvider() {
		this("/tsql.keywords", "/tsql.odbc.keywords");
	}

	public KeywordsProvider(final String... files) {
		for (String file : files) {
			init(file);
		}
	}

	private void init(final String file) {
		try {
			final InputStream stream = this.getClass().getResourceAsStream(file);

			final BufferedReader br = new BufferedReader(new InputStreamReader(stream));

			for (String line; (line = br.readLine()) != null;) {
				if (!StringUtils.isEmpty(line)) {
					final String keyword = line.toUpperCase().trim();
					if (!this.keywords.contains(keyword)) {
						this.keywords.add(keyword);
					}

				}
			}
		} catch (final IOException e) {
			LOGGER.warn(String.format("Error reading keywords file %s", file), e);
		}

	}

	public boolean isKeyword(final String name) {
		return this.keywords.contains(name);
	}
}
