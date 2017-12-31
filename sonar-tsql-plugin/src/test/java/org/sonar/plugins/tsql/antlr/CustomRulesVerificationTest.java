package org.sonar.plugins.tsql.antlr;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.helpers.AntlrUtils;
import org.sonar.plugins.tsql.rules.definitions.CustomPluginChecksProvider;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

@RunWith(Parameterized.class)
public class CustomRulesVerificationTest {
	private Rule rule;
	private String text;
	private boolean issuesFound;
	private String key;

	@Parameters(name = "{index}: {1}={3}")
	public static Collection<Object[]> data() {
		final List<Object[]> objects = new LinkedList<>();
		final CustomPluginChecksProvider provider = new CustomPluginChecksProvider();
		final SqlRules rules = provider.getRules();
		for (Rule r : rules.getRule()) {

			for (String c : r.getRuleImplementation().getCompliantRulesCodeExamples().getRuleCodeExample()) {
				objects.add(new Object[] { r, r.getName(), c, false });
			}

			for (String c : r.getRuleImplementation().getViolatingRulesCodeExamples().getRuleCodeExample()) {
				objects.add(new Object[] { r, r.getName(), c, true });
			}
		}
		return objects;
	}

	public CustomRulesVerificationTest(Rule rule, String key, String text, boolean issuesFound) {
		this.rule = rule;
		this.key = key;
		this.text = text;
		this.issuesFound = issuesFound;

	}

	@Test
	public void test() {
		Assert.assertNotNull("Rule's debt remiationfunction not specified", this.rule.getRemediationFunction());
		Assert.assertNotNull("Rule's DebtRemediationFunctionCoefficient not specified", this.rule.getDebtRemediationFunctionCoefficient());
		
		TsqlIssue[] issues = AntlrUtils.verify(this.rule, this.text);
		Assert.assertEquals(String.format("Expected rule %s for text %s to find issues: %s", this.rule.getName(),
				this.text, this.issuesFound), this.issuesFound, issues.length != 0);
	}

}
