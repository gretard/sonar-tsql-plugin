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

				Assert.assertTrue(String.format("%s Expected compliant code for %s", r.getKey(), s), res);
			}
			List<String> nonCompliant = r.getRuleImplementation().getViolatingRulesCodeExamples().getRuleCodeExample();
			for (String s : nonCompliant) {
				boolean res = Antlr4Utils.verify(r, s);

				Assert.assertFalse(String.format("%s Expected violating code for %s", r.getKey(), s), res);

			}
		}
	}
}
