package org.sonar.plugins.tsql;

import org.sonar.api.Plugin;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.languages.TSQLQualityProfile;
import org.sonar.plugins.tsql.rules.definitions.CodeGuardRulesDefinition;
import org.sonar.plugins.tsql.rules.definitions.MsRulesDefinition;
import org.sonar.plugins.tsql.sensors.CodeGuardIssuesLoaderSensor;
import org.sonar.plugins.tsql.sensors.HighlightingSensor;
import org.sonar.plugins.tsql.sensors.MsIssuesLoaderSensor;

public class TSQLPlugin implements Plugin {

	@Override
	public void define(final Context context) {
		context.addExtension(PropertyDefinition.builder(Constants.CG_APP_PATH).name("SQL Code Guard path")
				.description("Path to the sql code guard exe")
				.defaultValue("C:\\Program Files (x86)\\SqlCodeGuard\\SqlCodeGuard.Cmd.exe").type(PropertyType.STRING)
				.build());

		context.addExtension(PropertyDefinition.builder(Constants.CG_REPORT_FILE).name("SQL Code Guard results file")
				.description("SQL Code Guard results file").defaultValue("cgresults.xml").type(PropertyType.STRING)
				.build());

		context.addExtension(PropertyDefinition.builder(Constants.MS_REPORT_FILE)
				.name("MSBuild SQL code analysis results file").description("MSBuild SQL code analysis results file")
				.defaultValue("staticcodeanalysis.results.xml").type(PropertyType.STRING).build());

		context.addExtension(PropertyDefinition.builder(Constants.PLUGIN_SKIP).name("Disable plugin")
				.description("Flag whether to disable plugin").defaultValue("false").type(PropertyType.BOOLEAN)
				.build());

		context.addExtensions(TSQLLanguage.class, TSQLQualityProfile.class);
		context.addExtensions(MsRulesDefinition.class, CodeGuardRulesDefinition.class, MsIssuesLoaderSensor.class,
				CodeGuardIssuesLoaderSensor.class, HighlightingSensor.class);

	}
}
