package org.sonar.plugins.tsql.languages;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.tsql.Constants;

public final class TSQLQualityProfile extends ProfileDefinition {

	@Override
	public RulesProfile createProfile(final ValidationMessages validation) {
		return RulesProfile.create(Constants.PROFILE_NAME, TSQLLanguage.KEY);
	}
}
