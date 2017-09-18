package org.sonar.plugins.tsql.languages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.sonar.api.config.Settings;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.sensors.custom.CustomRulesProvider;

public final class TSQLQualityProfile extends ProfileDefinition {

	private static final Logger LOGGER = Loggers.get(TSQLQualityProfile.class);
	private final Settings settings;
	private final CustomRulesProvider customRulesProvider = new CustomRulesProvider();

	public TSQLQualityProfile(Settings settings) {
		this.settings = settings;
	}

	@Override
	public RulesProfile createProfile(final ValidationMessages validation) {

		final RulesProfile profile = RulesProfile.create(Constants.PROFILE_NAME, TSQLLanguage.KEY);
		activeRules(profile, Constants.CG_REPO_KEY, Constants.CG_RULES_FILE);
		activeRules(profile, Constants.MS_REPO_KEY, Constants.MS_RULES_FILE);

		final Map<String, org.sonar.plugins.tsql.rules.custom.CustomRules> rules = customRulesProvider.getRules(null,
				this.settings);

		for (String key : rules.keySet()) {
			try {
				org.sonar.plugins.tsql.rules.custom.CustomRules set = rules.get(key);
				FileInputStream st = new FileInputStream(key);
				activeRules(profile, set.getRepoKey(), st);
				st.close();
			} catch (FileNotFoundException e) {
				LOGGER.info("File was not found at " + key);
			} catch (IOException e) {
				LOGGER.info("Error occured reading file at " + key);
			}

		}

		return profile;
	}

	private void activeRules(final RulesProfile profile, final String key, final String file) {
		activeRules(profile, key, this.getClass().getResourceAsStream(file));
	}

	private void activeRules(final RulesProfile profile, final String key, final InputStream file) {
		try {

			final JAXBContext jaxbContext = JAXBContext.newInstance(TSQLRules.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final TSQLRules issues = (TSQLRules) jaxbUnmarshaller.unmarshal(file);
			for (final org.sonar.plugins.tsql.languages.TSQLRules.Rule rule : issues.rule) {
				profile.activateRule(Rule.create(key, rule.getKey()), null);
			}
		}

		catch (final Throwable e) {
			LOGGER.warn("Unexpected error occured while reading rules for " + key, e);
		}
	}
}
