package org.sonar.plugins.tsql;

import org.sonar.api.Plugin;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.languages.TSQLQualityProfile;
import org.sonar.plugins.tsql.rules.TsqlMsIssuesLoaderSensor;
import org.sonar.plugins.tsql.rules.TsqlMsRulesDefinition;

public class TSQLPlugin implements Plugin {

	@Override
	public void define(Context context) {

		context.addExtensions(TSQLLanguage.class, TSQLQualityProfile.class);

		context.addExtensions(TsqlMsRulesDefinition.class, TsqlMsIssuesLoaderSensor.class);

	}
}
