package org.sonar.plugins.tsql.rules.definitions;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.languages.TSQLLanguage;

public final class CustomPluginChecksRulesDefinition implements RulesDefinition {

	private final CustomPluginChecksProvider provider = new CustomPluginChecksProvider();

	private static final Logger LOGGER = Loggers.get(CustomUserChecksRulesDefinition.class);

	@Override
	public void define(final Context context) {
		try {
			final SqlRules rules = this.provider.getRules();
			final NewRepository repository = context.createRepository(rules.getRepoKey(), TSQLLanguage.KEY)
					.setName(rules.getRepoName());

			final JAXBContext jaxbContext = JAXBContext.newInstance(SqlRules.class);
			final Marshaller m = jaxbContext.createMarshaller();
			final StringWriter sw = new StringWriter();
			m.marshal(rules, sw);
			final RulesDefinitionXmlLoader rulesLoader = new RulesDefinitionXmlLoader();
			rulesLoader.load(repository, IOUtils.toInputStream(sw.toString(), "UTF-8"), StandardCharsets.UTF_8.name());
			repository.done();
		} catch (final Throwable e) {
			LOGGER.warn("Error occured loading custom plugin rules.", e);
		}

	}
}
