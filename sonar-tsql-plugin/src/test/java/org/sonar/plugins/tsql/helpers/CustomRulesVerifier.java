package org.sonar.plugins.tsql.helpers;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.rules.custom.CustomRules;
import org.sonar.plugins.tsql.rules.custom.Rule;

public class CustomRulesVerifier {

	@Test
	public void test() {
		CustomRules rules = Antlr4Utils.getCustomRules();
		for (Rule r : rules.getRule()) {
			List<String> compliant = r.getRuleImplementation().getCompliantRulesCodeExamples().getRuleCodeExample();

			for (String s : compliant) {
				boolean res = Antlr4Utils.verify(r, s);
				if (!res) {
					AntrlResult rr = Antlr4Utils.getFull(s);
					Antlr4Utils.print(rr.getTree(), 0, rr.getStream());
				}
				Assert.assertTrue(String.format("%s Expected compliant code for %s", r.getKey(), s), res);
			}
			List<String> nonCompliant = r.getRuleImplementation().getViolatingRulesCodeExamples().getRuleCodeExample();
			for (String s : nonCompliant) {
				boolean res = Antlr4Utils.verify(r, s);
				if (res) {
					Antlr4Utils.print(Antlr4Utils.get(s), 0);
				}
				Assert.assertFalse(String.format("%s Expected violating code for %s", r.getKey(), s), res);

			}
		}
	}
	@Test
	public void test2() {
		AntrlResult rr = Antlr4Utils.getFull("SELECT * FROM dbo.test");
		Antlr4Utils.print(rr.getTree(), 0, rr.getStream());
	}
}
