package org.sonar.plugins.tsql.helpers;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.rules.custom.CustomRules;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.sensors.custom.CustomRulesViolationsProvider;

public class CustomRulesVerifier {
	@Test
	public void test() {
		CustomRules rules = Antlr4Utils.getCustomRules();
		for (Rule r : rules.getRule()) {
			List<String> compliant = r.getRuleImplementation().getCompliantRulesCodeExamples().getRuleCodeExample();

			for (String s : compliant) {
				AntrlResult result = Antlr4Utils.getFull(s);
				CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream());
				ParseTree root = result.getTree();
				CustomRules customRules = new CustomRules();
				customRules.setRepoKey("test");
				customRules.setRepoName("test");
				customRules.getRule().add(r);
				TsqlIssue[] issues = provider.getIssues(root, customRules);
				Assert.assertEquals(String.format("%s Expected compliant code for %s", r.getKey(), s), 0, issues.length);
			}
			List<String> nonCompliant = r.getRuleImplementation().getViolatingRulesCodeExamples().getRuleCodeExample();
			for (String s : nonCompliant) {
				AntrlResult result = Antlr4Utils.getFull(s);
				CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream());
				ParseTree root = result.getTree();
				CustomRules customRules = new CustomRules();
				customRules.setRepoKey("test");
				customRules.setRepoName("test");
				customRules.getRule().add(r);
				TsqlIssue[] issues = provider.getIssues(root, customRules);
				Assert.assertTrue(String.format("%s Expected non-compliant code for %s", r.getKey(), s), issues.length > 0);
			}
		}
	}
}
