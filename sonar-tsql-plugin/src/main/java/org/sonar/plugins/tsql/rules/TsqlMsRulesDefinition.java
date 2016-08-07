package org.sonar.plugins.tsql.rules;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.plugins.tsql.languages.TSQLLanguage;

public final class TsqlMsRulesDefinition implements RulesDefinition {

	protected static final String KEY = "mssql";
	protected static final String NAME = "Microsoft T-SQL";

	protected String rulesDefinitionFilePath() {
		return "/rules/vssql-rules.xml";
	}

	private void defineRulesForLanguage(Context context, String repositoryKey, String repositoryName,
			String languageKey) {
		NewRepository repository = context.createRepository(repositoryKey, languageKey).setName(repositoryName);

		InputStream rulesXml = this.getClass().getResourceAsStream(rulesDefinitionFilePath());
		if (rulesXml != null) {
			RulesDefinitionXmlLoader rulesLoader = new RulesDefinitionXmlLoader();
			rulesLoader.load(repository, rulesXml, StandardCharsets.UTF_8.name());
		}
		
		repository.done();
	}

	@Override
	public void define(Context context) {
		String repositoryKey = TsqlMsRulesDefinition.getRepositoryKeyForLanguage(TSQLLanguage.KEY);
		String repositoryName = TsqlMsRulesDefinition.getRepositoryNameForLanguage(TSQLLanguage.KEY);
		defineRulesForLanguage(context, repositoryKey, repositoryName, TSQLLanguage.KEY);
	}

	public static String getRepositoryKeyForLanguage(String languageKey) {
		return languageKey.toLowerCase() + "-" + KEY;
	}

	public static String getRepositoryNameForLanguage(String languageKey) {
		return languageKey.toUpperCase() + " " + NAME;
	}

}
