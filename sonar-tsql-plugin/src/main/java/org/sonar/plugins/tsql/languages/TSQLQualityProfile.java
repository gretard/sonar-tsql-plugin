package org.sonar.plugins.tsql.languages;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;

public final class TSQLQualityProfile extends ProfileDefinition {

	private static final Logger LOGGER = Loggers.get(TSQLQualityProfile.class);

	@Override
	public RulesProfile createProfile(final ValidationMessages validation) {

		final RulesProfile profile = RulesProfile.create(Constants.PROFILE_NAME, TSQLLanguage.KEY);
		activeRules(profile, Constants.CG_REPO_KEY, Constants.CG_RULES_FILE);
		activeRules(profile, Constants.MS_REPO_KEY, Constants.MS_RULES_FILE);
		return profile;
	}

	private void activeRules(final RulesProfile profile, final String key, final String file) {
		try {

			final JAXBContext jaxbContext = JAXBContext.newInstance(SqlRules.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final SqlRules issues = (SqlRules) jaxbUnmarshaller.unmarshal(this.getClass().getResourceAsStream(file));
			for (final org.sonar.plugins.tsql.languages.SqlRules.Rule rule : issues.rule) {
				profile.activateRule(Rule.create(key, rule.getKey()), null);
			}
		}

		catch (final Throwable e) {
			LOGGER.warn("Unexpected error occured while reading rules for " + key, e);
		}
	}
}
