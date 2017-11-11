package org.sonar.plugins.tsql.rules.definitions;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.rules.custom.SqlRules;

public class CustomRulesProvider {
	private static final Logger LOGGER = Loggers.get(CustomRulesProvider.class);

	public Map<String, SqlRules> getRules(final String baseDir, final String prefix, final String... paths) {

		final HashMap<String, SqlRules> rulesRepositories = new HashMap<String, SqlRules>();

		final List<String> updatedPaths = new LinkedList<String>();
		for (final String path : paths) {
			File f = new File(path);
			if (baseDir != null && !f.isAbsolute()) {
				f = new File(baseDir, path);
			}
			if (f.isDirectory()) {

				f.listFiles(new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						if (pathname.isFile() && pathname.getName().contains(prefix)) {
							updatedPaths.add(pathname.getAbsolutePath());
						}

						return pathname.isFile();
					}
				});
				continue;
			}

			if (f.getName().contains(prefix)) {
				updatedPaths.add(f.getAbsolutePath());
			}
		}

		for (final String path : updatedPaths) {

			try {
				final InputStream file = new FileInputStream(path);
				final JAXBContext jaxbContext = JAXBContext.newInstance(SqlRules.class);
				final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				rulesRepositories.put(path, (SqlRules) jaxbUnmarshaller.unmarshal(file));
				file.close();
				LOGGER.info("Read rules ok at: " + path);
			} catch (final FileNotFoundException e) {
				LOGGER.info("Custom rule file not found at: " + path);
			} catch (final JAXBException e) {
				LOGGER.info("Was not able to read custom rule file not found at: " + path);
			} catch (final IOException e) {
				LOGGER.info("Was not able to close custom rule file at: " + path);
			}
		}

		return rulesRepositories;
	}
}
