package org.sonar.plugins.tsql.languages;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;

public final class TSQLQualityProfile extends ProfileDefinition {

	@Override
	public RulesProfile createProfile(final  ValidationMessages validation) {
		return RulesProfile.create("T-SQL Rules", TSQLLanguage.KEY);
	}
}
