package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.rules.custom.Rule;

public class ChildrenFillerTest {

	@Test
	public void test() {
		ParseTree tree = Antlr4Utils.get("select * from dbo.test");
		Rule rule = new Rule();
		ChildrenFiller filler = new ChildrenFiller();
		ParsedNode node = new ParsedNode(tree, rule, "test");
		filler.fill(node);
		Assert.assertEquals(0, node.getParents().size());
		Assert.assertEquals(26, node.getChildren().size());
	}

}
