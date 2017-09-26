package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.rules.custom.Rule;

public class ChildrenNodesProviderTest {

	@Test
	public void test() {
		ChildrenNodesProvider provider = new ChildrenNodesProvider();
		Assert.assertEquals(0, provider.getNodes(null).length);
	}

	@Test
	public void test2() {
		ParseTree tree = Antlr4Utils.get("select * from dbo.test");
		Rule rule = new Rule();
		ChildrenNodesProvider provider = new ChildrenNodesProvider();
		ParsedNode node = new ParsedNode(tree, rule, "test");
		Assert.assertEquals(26, provider.getNodes(node).length);
	}

}
