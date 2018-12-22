package org.sonar.plugins.tsql.rules.definitions;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.checks.CustomPluginChecks;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.languages.TSQLLanguage;

public final class CustomPluginChecksRulesDefinition implements RulesDefinition {

	private final CustomPluginChecks provider = new CustomPluginChecks();

	private static final Logger LOGGER = Loggers.get(CustomUserChecksRulesDefinition.class);

	@Override
	public void define(final Context context) {
		try {
			final SqlRules rules = this.provider.getRules();
			final String rulesXml = ruleToString(rules);
			final NewRepository repository = context.createRepository(rules.getRepoKey(), TSQLLanguage.KEY)
					.setName(rules.getRepoName());

			final RulesDefinitionXmlLoader rulesLoader = new RulesDefinitionXmlLoader();
			rulesLoader.load(repository, IOUtils.toInputStream(rulesXml, "UTF-8"), StandardCharsets.UTF_8.name());
			repository.done();
		} catch (final Throwable e) {
			LOGGER.warn("Error occured loading custom plugin rules.", e);
		}

	}

	private static String ruleToString(SqlRules customRules) {

		for (final org.sonar.plugins.tsql.checks.custom.Rule r : customRules.getRule()) {
			final List<String> compliant = r.getRuleImplementation().getCompliantRulesCodeExamples()
					.getRuleCodeExample();
			final List<String> violating = r.getRuleImplementation().getViolatingRulesCodeExamples()
					.getRuleCodeExample();
			if (compliant.isEmpty() && violating.isEmpty()) {
				continue;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(r.getDescription());
			sb.append("<h2>Code examples</h2>");
			if (!violating.isEmpty()) {
				sb.append("<h3>Non-compliant</h3>");
				for (String x : violating) {
					sb.append("<pre><code>" + x + "</code></pre>");
				}
			}

			if (!compliant.isEmpty()) {
				sb.append("<h3>Compliant</h3>");
				for (String x : compliant) {
					sb.append("<pre><code>" + x + "</code></pre>");
				}
			}
			if (r.getSource() != null && !r.getSource().isEmpty()) {
				sb.append("<h4>Source</h4>");
				sb.append(String.format("<a href='%s'>%s</a>", r.getSource(), r.getSource()));
			}
			r.setDescription(sb.toString());

		}
		String xmlString = "";
		try {
			JAXBContext context = JAXBContext.newInstance(SqlRules.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To
			StringWriter sw = new StringWriter();
			m.marshal(customRules, sw);
			xmlString = sw.toString();

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return xmlString;
	}
}
