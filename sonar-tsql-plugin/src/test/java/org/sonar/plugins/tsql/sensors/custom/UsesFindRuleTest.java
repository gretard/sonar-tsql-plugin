package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.RuleMode;

public class UsesFindRuleTest {

	@Test
	public void test() {

		ParseTree tree = Antlr4Utils.get("select * from dbo.test");
		Rule rule = new Rule();
		RuleImplementation impl = new RuleImplementation();
		impl.setRuleMode(RuleMode.SINGLE);
		rule.setRuleImplementation(impl);
		ParsedNode node = new ParsedNode(tree.getChild(0), rule, "a");

		UsesFindRule finder = new UsesFindRule();

		finder.root(node, tree, rule);

		//Assert.assertEquals(1, node.getUses().size());
	}

	@Test
	public void test2() {
		ParseTree tree = Antlr4Utils.get("select * from dbo.test");
		Rule rule = new Rule();
		RuleImplementation impl = new RuleImplementation();
		impl.setRuleMode(RuleMode.SINGLE);
		rule.setRuleImplementation(impl);
		ParsedNode node = new ParsedNode(tree, rule, "a");

		UsesFindRule finder = new UsesFindRule();

		finder.root(node, tree, rule);

		Assert.assertEquals(0, node.getUses().size());
	}

}
