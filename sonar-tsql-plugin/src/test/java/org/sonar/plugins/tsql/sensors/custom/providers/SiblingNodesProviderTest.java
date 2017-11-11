package org.sonar.plugins.tsql.sensors.custom.providers;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.sensors.custom.ParsedNode;
import org.sonar.plugins.tsql.sensors.custom.nodes.SiblingsNodesProvider;

public class SiblingNodesProviderTest {

	@Test
	public void test() {
		SiblingsNodesProvider provider = new SiblingsNodesProvider();
		Assert.assertEquals(0, provider.getNodes(null).length);
	}

	@Test
	public void test2() {
		ParseTree tree = Antlr4Utils.get("select * from dbo.test");
		Rule rule = new Rule();
		SiblingsNodesProvider provider = new SiblingsNodesProvider();
		ParsedNode node = new ParsedNode(tree.getChild(0).getChild(0), rule, "test");
		Assert.assertEquals(1, provider.getNodes(node).length);
	}

}
