package org.sonar.plugins.tsql;

import org.sonar.api.Plugin;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.languages.TSQLQualityProfile;
import org.sonar.plugins.tsql.rules.SqlCodeGuardIssuesLoaderSensor;
import org.sonar.plugins.tsql.rules.VisualStudioIssuesLoaderSensor;
import org.sonar.plugins.tsql.rules.definitions.CodeGuardRulesDefinition;
import org.sonar.plugins.tsql.rules.definitions.TsqlMsRulesDefinition;

public class TSQLPlugin implements Plugin {

	@Override
	public void define(final Context context) {

		context.addExtensions(TSQLLanguage.class, TSQLQualityProfile.class);

		context.addExtensions(TsqlMsRulesDefinition.class, CodeGuardRulesDefinition.class, VisualStudioIssuesLoaderSensor.class, SqlCodeGuardIssuesLoaderSensor.class);

	}
}
