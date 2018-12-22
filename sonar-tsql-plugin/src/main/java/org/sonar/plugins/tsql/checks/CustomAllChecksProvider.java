package org.sonar.plugins.tsql.checks;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.config.Configuration;
import org.sonar.api.rules.RuleType;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.antlr.CandidateRule;
import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.SqlRules;

public class CustomAllChecksProvider {

	private final CustomUserChecksProvider provider = new CustomUserChecksProvider();

	private final CustomPluginChecks pluginChecksProvider = new CustomPluginChecks();
	private static final Logger LOGGER = Loggers.get(CustomAllChecksProvider.class);

	public CandidateRule[] getChecks(final SensorContext context) {
		final Configuration config = context.config();
		final boolean skipCustomRules = config.getBoolean(Constants.PLUGIN_SKIP_CUSTOM_RULES).orElse(false);
		final String baseDir = context.fileSystem().baseDir().getAbsolutePath();

		final String[] paths = config.getStringArray(Constants.PLUGIN_CUSTOM_RULES_PATH);
		final String rulesPrefix = config.get(Constants.PLUGIN_CUSTOM_RULES_PREFIX).orElse(".customRules");

		final List<SqlRules> rules = new ArrayList<>();

		rules.addAll(provider.getRules(baseDir, rulesPrefix, paths).values());

		if (!skipCustomRules) {
			final SqlRules customRules = pluginChecksProvider.getRules();
			rules.add(customRules);
		}

		for (final SqlRules sqlRules : rules) {
			if (sqlRules.isIsAdhoc()) {
				for (final Rule r : sqlRules.getRule()) {
					context.newAdHocRule().description(r.getDescription()).engineId(sqlRules.getRepoKey())
							.ruleId(r.getKey()).type(RuleType.valueOf(r.getRuleType())).name(r.getName())
							.severity(Severity.valueOf(r.getSeverity())).save();
				}
			}
		}

		final SqlRules[] finalRules = rules.toArray(new SqlRules[0]);

		final CandidateRule[] candidateRules = convert(finalRules);
		LOGGER.info(String.format("Total %s custom rules repositories with total %s checks", rules.size(),
				candidateRules.length));
		return candidateRules;

	}

	private static CandidateRule[] convert(final SqlRules... rules) {
		final List<CandidateRule> convertedRules = new ArrayList<>();
		for (final SqlRules r : rules) {
			for (final Rule rule : r.getRule()) {
				convertedRules.add(new CandidateRule(r.getRepoKey(), rule, r.isIsAdhoc()));
			}
		}
		return convertedRules.toArray(new CandidateRule[0]);
	}
}
