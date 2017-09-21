package org.sonar.plugins.tsql.helpers;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.rules.custom.CustomRules;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class CustomRulesVerifier {

	@Test
	public void test() {
		CustomRules rules = Antlr4Utils.getCustomRules();
		for (Rule r : rules.getRule()) {
			List<String> compliant = r.getRuleImplementation().getCompliantRulesCodeExamples().getRuleCodeExample();

			for (String s : compliant) {
				TsqlIssue[] issues = Antlr4Utils.verify2(r, s);
				boolean res = 	(issues.length == 0);
				if (!res) {
					AntrlResult rr = Antlr4Utils.getFull(s);
					Antlr4Utils.print(rr.getTree(), 0, rr.getStream());
				}
				for (TsqlIssue i : issues) {
					System.out.println(i.getDescription());
				}
				Assert.assertTrue(String.format("%s Expected compliant code for %s", r.getKey(), s), res);
			}
			List<String> nonCompliant = r.getRuleImplementation().getViolatingRulesCodeExamples().getRuleCodeExample();
			for (String s : nonCompliant) {
				TsqlIssue[] issues = Antlr4Utils.verify2(r, s);
				boolean res = 	(issues.length > 0);
				if (!res) {
					Antlr4Utils.print(Antlr4Utils.get(s), 0);
				}
				for (TsqlIssue i : issues) {
					System.out.println(i.getDescription());
				}
				Assert.assertTrue(String.format("%s Expected violating code for %s", r.getKey(), s), res);

			}
		}
	}
	@Test
	public void test2() {
		AntrlResult rr = Antlr4Utils.getFull("SELECT * FROM dbo.test");
		Antlr4Utils.print(rr.getTree(), 0, rr.getStream());
	}
}
