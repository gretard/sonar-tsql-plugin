package org.sonar.plugins.tsql;

import org.sonar.plugins.tsql.languages.TSQLLanguage;

public final class Constants {
	public static final String PROFILE_NAME = "Sonar Way";

	public static final String CG_REPORT_FILE = "sonar.tsql.cg.report";

	public static final String PLUGIN_SKIP = "sonar.tsql.skip";

	public static final String PLUGIN_SKIP_COVERAGE = "sonar.tsql.sqlcover.skip";
	
	public static final String CG_APP_PATH = "sonar.tsql.cg.path";

	public static final String COVERAGE_FILE = "sonar.tsql.sqlcover.reports.path";
	
	public static final String MS_REPORT_FILE = "sonar.tsql.ms.report";

	public static final String PLUGIN_SKIP_CPD = "sonar.tsql.skip.cpd";

	public static final String PLUGIN_ANALYSIS_TIMEOUT = "sonar.tsql.timeout.analysis";

	
	public static final String PLUGIN_SUFFIXES = "sonar.tsql.file.suffixes";

	public static final String PLUGIN_SKIP_CUSTOM_RULES = "sonar.tsql.skip.custom.rules";

	public static final String CG_REPO_KEY = TSQLLanguage.KEY.toLowerCase() + "-sqlcodeguard";

	public static final String CG_REPO_NAME = TSQLLanguage.KEY.toLowerCase() + " SQL Code Guard";

	public static final String CG_RULES_FILE = "/rules/cgsql-rules.xml";

	public static final String MS_REPO_KEY = TSQLLanguage.KEY.toLowerCase() + "-mssql";

	public static final String MS_REPO_NAME = TSQLLanguage.KEY.toLowerCase() + " Microsoft T-SQL";

	public static final String MS_RULES_FILE = "/rules/vssql-rules.xml";

	public static final String PLUGIN_CUSTOM_RULES_PATH = "sonar.tsql.customrules.paths";

	
	
	public static final String PLUGIN_CUSTOM_RULES_PREFIX = "sonar.tsql.customrules.prefix";

	public static final String PLUGIN_RULES_FILE = "/rules/plugin-rules.xml";

	public static final String PLUGIN_REPO_NAME = TSQLLanguage.KEY.toLowerCase() +" plugin custom rules";

	public static final String PLUGIN_REPO_KEY = "tsqlPluginRepo";
}
