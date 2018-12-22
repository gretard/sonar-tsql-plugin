package org.sonar.plugins.tsql.checks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.checks.custom.SqlRules;

public class CustomUserChecksProvider {
	private static final Logger LOGGER = Loggers.get(CustomUserChecksProvider.class);

	public Map<String, SqlRules> getRules(final String baseDir, final String prefix, final String... paths) {

		final HashMap<String, SqlRules> rulesRepositories = new HashMap<>();

		final List<String> updatedPaths = new LinkedList<>();
		for (final String path : paths) {
			File f = new File(path);
			if (baseDir != null && !f.isAbsolute()) {
				f = new File(baseDir, path);
			}
			try (final Stream<Path> stream = Files.walk(f.toPath())) {
				stream.filter(x -> x.toFile().isFile() && x.toString().contains(prefix))
						.forEach(x -> updatedPaths.add(x.toFile().getAbsolutePath()));
			} catch (IOException e) {
				LOGGER.info(String.format("Unexpected error ocurred while reading: %s", f.getAbsolutePath()), e);
			}
		}

		for (final String path : updatedPaths) {

			try (final InputStream file = new FileInputStream(path)) {
				final JAXBContext jaxbContext = JAXBContext.newInstance(SqlRules.class);
				final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				final SqlRules rules = (SqlRules) jaxbUnmarshaller.unmarshal(file);
				rulesRepositories.put(path, rules);
				LOGGER.info(String.format("Read %s rules ok at: %s", rules.getRule().size(), path));
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
