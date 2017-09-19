package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.rules.custom.Rule;

public class ParentsFillerTest {

	@Test
	public void test() {

		ParseTree tree = Antlr4Utils.get("select * from dbo.test");
		Rule rule = new Rule();
		ParentsFiller filler = new ParentsFiller();
		ParsedNode node = new ParsedNode(tree.getChild(0), rule, "test");
		filler.fill(rule, node);
		Assert.assertEquals(1, node.getParents().size());
		Assert.assertEquals(0, node.getChildren().size());
	}

	@Test
	public void test2() {

		ParseTree tree = Antlr4Utils.get("select * from dbo.test");
		Rule rule = new Rule();
		ParentsFiller filler = new ParentsFiller();
		ParsedNode node = new ParsedNode(tree, rule, "test");
		filler.fill(rule, node);
		Assert.assertEquals(0, node.getParents().size());
		Assert.assertEquals(0, node.getChildren().size());
	}
}
