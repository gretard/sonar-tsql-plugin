package org.sonar.plugins.tsql.rules.definitions;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.sensors.antlr4.CandidateRule;
import org.sonar.plugins.tsql.sensors.antlr4.PluginHelper;

public class CustomAllChecksProvider {

	private final CustomUserChecksProvider provider = new CustomUserChecksProvider();

	private final CustomPluginChecksProvider pluginChecksProvider = new CustomPluginChecksProvider();
	private final Settings settings;
	private static final Logger LOGGER = Loggers.get(CustomAllChecksProvider.class);

	public CustomAllChecksProvider(final Settings settings) {
		this.settings = settings;
	}

	public CandidateRule[] getChecks(final String baseDir) {
		final boolean skipCustomRules = settings.getBoolean(Constants.PLUGIN_SKIP_CUSTOM_RULES);

		final String[] paths = settings.getStringArray(Constants.PLUGIN_CUSTOM_RULES_PATH);
		final String rulesPrefix = settings.getString(Constants.PLUGIN_CUSTOM_RULES_PREFIX);
		final List<SqlRules> rules = new ArrayList<>();
		if (!skipCustomRules) {
			final SqlRules customRules = pluginChecksProvider.getRules();
			rules.add(customRules);
		}

		rules.addAll(provider.getRules(baseDir, rulesPrefix, paths).values());
		final SqlRules[] finalRules = rules.toArray(new SqlRules[0]);

		final CandidateRule[] candidateRules = PluginHelper.convert(finalRules);
		LOGGER.info(String.format("Total %s custom rules repositories with total %s checks", rules.size(),
				candidateRules.length));
		return candidateRules;

	}
}
