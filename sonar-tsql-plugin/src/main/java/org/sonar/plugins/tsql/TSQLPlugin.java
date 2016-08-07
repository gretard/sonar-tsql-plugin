package org.sonar.plugins.tsql;

import org.sonar.api.Plugin;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.languages.TSQLQualityProfile;
import org.sonar.plugins.tsql.rules.TsqlMsIssuesLoaderSensor;
import org.sonar.plugins.tsql.rules.TsqlMsRulesDefinition;

/**
 * This class is the entry point for all extensions. It is referenced in
 * pom.xml.
 */
public class TSQLPlugin implements Plugin {

	@Override
	public void define(Context context) {
		// tutorial on hooks
		// http://docs.sonarqube.org/display/DEV/Adding+Hooks

		// tutorial on languages
		context.addExtensions(TSQLLanguage.class, TSQLQualityProfile.class);

		// tutorial on measures
		// context
		// .addExtensions(ExampleMetrics.class, SetSizeOnFilesSensor.class,
		// ComputeSizeAverage.class, ComputeSizeRating.class);

		// tutorial on rules
		// context.addExtensions(JavaRulesDefinition.class,
		// CreateIssuesOnJavaFilesSensor.class);
		context.addExtensions(TsqlMsRulesDefinition.class, TsqlMsIssuesLoaderSensor.class);

		// tutorial on settings
		// context
		// .addExtensions(ExampleProperties.definitions())
		// .addExtension(SayHelloFromScanner.class);

		// tutorial on web extensions
		// context.addExtensions(ExampleFooter.class, ExampleWidget.class);
	}
}
