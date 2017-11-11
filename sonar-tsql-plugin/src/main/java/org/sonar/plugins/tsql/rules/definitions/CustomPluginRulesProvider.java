package org.sonar.plugins.tsql.rules.definitions;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.rules.custom.SqlRules;

public class CustomPluginRulesProvider {
	private static final Logger LOGGER = Loggers.get(CustomPluginRulesProvider.class);

	public SqlRules getRules() {

		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(SqlRules.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (SqlRules) jaxbUnmarshaller
					.unmarshal(this.getClass().getResourceAsStream(Constants.PLUGIN_RULES_FILE));

		} catch (final Throwable e) {
			LOGGER.warn("Was not able to read custom plugin rules", e);
		}
		return null;
	}
}
