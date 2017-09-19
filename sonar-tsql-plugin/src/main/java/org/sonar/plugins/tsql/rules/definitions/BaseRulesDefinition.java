package org.sonar.plugins.tsql.rules.definitions;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.plugins.tsql.languages.TSQLLanguage;

public abstract class BaseRulesDefinition implements RulesDefinition {
	private final String rulesPath;
	private final String repositoryName;
	private final String repositoryKey;

	public BaseRulesDefinition(String repositoryKey, String repositoryName, String rulesPath) {
		this.repositoryKey = repositoryKey;
		this.repositoryName = repositoryName;
		this.rulesPath = rulesPath;

	}

	private void defineRulesForLanguage(final Context context) {
		final NewRepository repository = context.createRepository(this.repositoryKey, TSQLLanguage.KEY)
				.setName(this.repositoryName);

		final InputStream rulesXml = this.getClass().getResourceAsStream(this.rulesPath);
		if (rulesXml != null) {
			final RulesDefinitionXmlLoader rulesLoader = new RulesDefinitionXmlLoader();
			rulesLoader.load(repository, rulesXml, StandardCharsets.UTF_8.name());
		}

		repository.done();
	}

	@Override
	public void define(final Context context) {
		defineRulesForLanguage(context);
	}
}
