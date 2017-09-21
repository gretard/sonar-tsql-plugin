package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.RuleMode;

public class ParsedNodeUsesFillerTest {

	@Test
	public void testSingle() {
		ParseTree tree = Antlr4Utils.get("select * from dbo.test");
		Rule rule = new Rule();
		RuleImplementation impl = new RuleImplementation();
		impl.setRuleMode(RuleMode.SINGLE);
		rule.setRuleImplementation(impl);
		ParsedNode node = new ParsedNode(tree, rule, "a");
		ParsedNodeUsesFiller filler = new ParsedNodeUsesFiller(tree);

	//	filler.fill(rule, node);
	}
	@Test
	public void testSingle2() {
		ParseTree tree = Antlr4Utils.get("select name from dbo.test;select name from dbo.test;");
		Rule rule = new Rule();
		RuleImplementation impl = new RuleImplementation();
		impl.setRuleMode(RuleMode.SINGLE);
		rule.setRuleImplementation(impl);
		ParsedNode node = new ParsedNode("name", tree, rule, "a");
		ParsedNodeUsesFiller filler = new ParsedNodeUsesFiller(tree);

		//filler.fill(rule, node);
	}
	@Test
	public void testGroup() {
		ParseTree tree = Antlr4Utils.get("select * from dbo.test");
		Rule rule = new Rule();
		RuleImplementation impl = new RuleImplementation();
		impl.setRuleMode(RuleMode.GROUP);
		rule.setRuleImplementation(impl);
		ParsedNodeUsesFiller filler = new ParsedNodeUsesFiller(tree);
		ParsedNode node = new ParsedNode("aa", tree, rule, "a");
		//filler.fill(rule, node);
		//Assert.assertEquals(0, node.getUses().size());
	}

	@Test
	public void testGroup2() {
		ParseTree tree = Antlr4Utils.get("select name from dbo.test; select name from dbo.test");
		Rule rule = new Rule();
		RuleImplementation impl = new RuleImplementation();
		impl.setRuleMode(RuleMode.GROUP);
		rule.setRuleImplementation(impl);
		ParsedNodeUsesFiller filler = new ParsedNodeUsesFiller(tree);
		ParsedNode node = new ParsedNode("name", tree, rule, "a");
		//filler.fill(rule, node);
		//Assert.assertEquals(24, node.getUses().size());
	}

	@Test
	public void test2() {
		ParseTree tree = Antlr4Utils.get("select * from dbo.test");
		Rule rule = new Rule();
		RuleImplementation impl = new RuleImplementation();
		impl.setRuleMode(RuleMode.GROUP);
		rule.setRuleImplementation(impl);
		ParsedNodeUsesFiller filler = new ParsedNodeUsesFiller(tree);
		ParsedNode node = new ParsedNode(tree.getChild(0), rule, "a");
		//filler.fill(rule, node);
		//Assert.assertEquals(7, node.getUses().size());
	}

}
