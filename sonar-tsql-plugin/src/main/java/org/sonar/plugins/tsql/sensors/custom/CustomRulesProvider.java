package org.sonar.plugins.tsql.sensors.custom;

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

import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.rules.custom.CustomRules;

public class CustomRulesProvider {
	private static final Logger LOGGER = Loggers.get(CustomRulesProvider.class);

	public Map<String, CustomRules> getRules(final Settings settings) {

		final HashMap<String, CustomRules> rulesRepositories = new HashMap<String, CustomRules>();
		final String[] paths = settings.getStringArray(Constants.PLUGIN_CUSTOM_RULES_PATH);
		final String rulesPrefix = settings.getString(Constants.PLUGIN_CUSTOM_RULES_PREFIX);
		final List<String> updatedPaths = new LinkedList<String>();
		for (final String path : paths) {
			final File f = new File(path);
			if (f.isDirectory()) {

				f.listFiles(new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						if (pathname.isFile() && pathname.getName().contains(rulesPrefix)) {
							updatedPaths.add(pathname.getAbsolutePath());
						}

						return pathname.isFile();
					}
				});
				continue;
			}

			if (f.getName().contains(rulesPrefix)) {
				updatedPaths.add(f.getAbsolutePath());
			}
		}

		for (final String path : updatedPaths) {

			try {
				final InputStream file = new FileInputStream(path);
				final JAXBContext jaxbContext = JAXBContext.newInstance(CustomRules.class);
				final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				rulesRepositories.put(path, (CustomRules) jaxbUnmarshaller.unmarshal(file));
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
