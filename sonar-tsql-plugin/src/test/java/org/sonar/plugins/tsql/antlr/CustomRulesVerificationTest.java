package org.sonar.plugins.tsql.antlr;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sonar.plugins.tsql.checks.CustomPluginChecks;
import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.helpers.AntlrUtils;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

@RunWith(Parameterized.class)
public class CustomRulesVerificationTest {
	private Rule rule;
	private String text;
	private boolean issuesFound;
	private String key;
	private String ruleName;

	@Parameters(name = "{index}: {1}={2}")
	public static Collection<Object[]> data() {
		final List<Object[]> objects = new LinkedList<>();
		final CustomPluginChecks provider = new CustomPluginChecks();
		final SqlRules rules = provider.getRules();
		for (Rule r : rules.getRule()) {

			for (String c : r.getRuleImplementation().getCompliantRulesCodeExamples().getRuleCodeExample()) {
				objects.add(new Object[] { r, r.getKey(), r.getName(), c, false });
			}

			for (String c : r.getRuleImplementation().getViolatingRulesCodeExamples().getRuleCodeExample()) {
				objects.add(new Object[] { r, r.getKey(), r.getName(), c, true });
			}
		}
		return objects;
	}

	public CustomRulesVerificationTest(Rule rule, String key, String ruleName, String text, boolean issuesFound) {
		this.rule = rule;
		this.key = key;
		this.ruleName = ruleName;
		this.text = text;
		this.issuesFound = issuesFound;

	}

	@Test
	public void test()  throws Throwable{
		Assert.assertNotNull("Rule's debt remiationfunction not specified", this.rule.getRemediationFunction());
		Assert.assertNotNull("Rule's DebtRemediationFunctionCoefficient not specified",
				this.rule.getDebtRemediationFunctionCoefficient());
		Assert.assertEquals(this.rule.getKey(), this.rule.getInternalKey());
		TsqlIssue[] issues = AntlrUtils.verify(this.rule, this.text);
		Assert.assertEquals(String.format("Expected rule %s [%s] for text %s to find issues: %s", this.rule.getName(),
				this.key, this.text, this.issuesFound), this.issuesFound, issues.length != 0);
	}

}
