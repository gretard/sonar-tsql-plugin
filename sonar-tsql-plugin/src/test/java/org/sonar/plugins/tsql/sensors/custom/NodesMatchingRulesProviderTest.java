package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleMode;

public class NodesMatchingRulesProviderTest {

	@Test
	public void test() {
		ParseTree root = Antlr4Utils.get("select * from dbo.test; select * from dbo.test; ");
		NodesMatchingRulesProvider provider = new NodesMatchingRulesProvider();
		Rule rule = Antlr4Utils.getSelectAllRule();
		ParsedNode[] results = provider.getNodes("test", root, rule);
		Assert.assertEquals(2, results.length);
	}
	@Test
	public void test2() {
		ParseTree root = Antlr4Utils.get("select * from dbo.test; select * from dbo.test; ");
		NodesMatchingRulesProvider provider = new NodesMatchingRulesProvider();
		Rule rule = Antlr4Utils.getSelectAllRule();
		rule.getRuleImplementation().setRuleMode(RuleMode.GROUP);
		ParsedNode[] results = provider.getNodes("test", root, rule);
		Assert.assertEquals(1, results.length);
	}
}
