package org.sonar.plugins.tsql.sensors.custom.nodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.helpers.TestNode;

public class NodeUsesProviderTest {

	@Test
	public void testGetUsesNodes() {
		String s = "SELECT *,test from dbo.test where name like '%test%' ;";
		ParseTree tree = Antlr4Utils.get(s);
		NodeUsesProvider provider = new NodeUsesProvider(tree);
		IParsedNode[] nodes = provider.getNodes(new TestNode("test", null, 0));
		Assert.assertEquals(28, nodes.length);
	}

	@Test
	public void testGetUsesNodesNull() {
		String s = "SELECT *,test from dbo.test where name like '%test%' ;";
		ParseTree tree = Antlr4Utils.get(s);
		NodeUsesProvider provider = new NodeUsesProvider(tree);
		IParsedNode[] nodes = provider.getNodes(null);
		Assert.assertEquals(0, nodes.length);
	}

}
