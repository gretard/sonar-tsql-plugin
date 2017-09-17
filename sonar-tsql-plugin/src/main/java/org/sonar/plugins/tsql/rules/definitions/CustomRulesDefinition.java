package org.sonar.plugins.tsql.rules.definitions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.sonar.api.config.Settings;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.rules.custom.CustomRules;
import org.sonar.plugins.tsql.sensors.custom.CustomRulesProvider;

public class CustomRulesDefinition implements RulesDefinition {

	private final Settings settings;
	private final CustomRulesProvider provider = new CustomRulesProvider();

	private static final Logger LOGGER = Loggers.get(CustomRulesDefinition.class);

	public CustomRulesDefinition(Settings settings) {
		this.settings = settings;
	}

	private void defineRulesForLanguage(final Context context) {

		final Map<String, CustomRules> rules = provider.getRules(this.settings);

		for (final String key : rules.keySet()) {

			final CustomRules type = rules.get(key);
			final String repositoryKey = type.getRepoKey();
			final String repositoryName = type.getRepoName();
			final NewRepository repository = context.createRepository(repositoryKey, TSQLLanguage.KEY)
					.setName(repositoryName);
			try {
				InputStream rulesXml = new FileInputStream(key);
				if (rulesXml != null) {
					final RulesDefinitionXmlLoader rulesLoader = new RulesDefinitionXmlLoader();
					rulesLoader.load(repository, rulesXml, StandardCharsets.UTF_8.name());
				}
			} catch (FileNotFoundException e) {
				LOGGER.info("File was not found: " + key);
			}

			repository.done();
		}

	}

	@Override
	public void define(final Context context) {
		defineRulesForLanguage(context);
	}

}
