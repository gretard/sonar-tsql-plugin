package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.rules.custom.Rule;

public class SiblingsFillerTest {

	@Test
	public void test() {
		ParseTree tree = Antlr4Utils.get("select * from dbo.test");
		Rule rule = new Rule();
		SiblingsFiller filler = new SiblingsFiller();
		ParsedNode node = new ParsedNode(tree.getChild(0).getChild(0), rule, "test");
		filler.fill(node);
		Assert.assertEquals(0, node.getSiblings().size());
		Assert.assertEquals(0, node.getChildren().size());
	}

}
