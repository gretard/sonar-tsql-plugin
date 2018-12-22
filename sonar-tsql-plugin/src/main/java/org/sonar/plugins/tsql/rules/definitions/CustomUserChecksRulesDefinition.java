package org.sonar.plugins.tsql.rules.definitions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.sonar.api.config.Settings;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.checks.CustomUserChecksProvider;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.languages.TSQLLanguage;

public class CustomUserChecksRulesDefinition implements RulesDefinition {

	private final Settings settings;
	private final CustomUserChecksProvider provider = new CustomUserChecksProvider();

	private static final Logger LOGGER = Loggers.get(CustomUserChecksRulesDefinition.class);

	public CustomUserChecksRulesDefinition(Settings settings) {
		this.settings = settings;
	}

	private void defineRulesForLanguage(final Context context) {

		final String[] paths = settings.getStringArray(Constants.PLUGIN_CUSTOM_RULES_PATH);
		final String rulesPrefix = settings.getString(Constants.PLUGIN_CUSTOM_RULES_PREFIX);
		final Map<String, SqlRules> rules = provider.getRules(null, rulesPrefix, paths);

		for (final String key : rules.keySet()) {
			final SqlRules type = rules.get(key);
			if (type.isIsAdhoc()) {
				continue;
			}
			final String repositoryKey = type.getRepoKey();
			final String repositoryName = type.getRepoName();
			final NewRepository repository = context.createRepository(repositoryKey, TSQLLanguage.KEY)
					.setName(repositoryName);
			try (final InputStream rulesXml = new FileInputStream(key)) {
				final RulesDefinitionXmlLoader rulesLoader = new RulesDefinitionXmlLoader();
				rulesLoader.load(repository, rulesXml, StandardCharsets.UTF_8.name());
			} catch (FileNotFoundException e) {
				LOGGER.info("File was not found: " + key);
			} catch (IOException e1) {
				LOGGER.info("Error reading file: " + key);
			}

			repository.done();
		}

	}

	@Override
	public void define(final Context context) {
		defineRulesForLanguage(context);
	}

}
