package org.sonar.plugins.tsql.sensors.custom.nodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;

public class ParsedNodeTest {

	@Test
	public void testGetChildren() {
		String s = "SELECT * from dbo.test where name like '%test%' ;";
		ParseTree tree = Antlr4Utils.get(s);
		ParsedNode node = new ParsedNode(tree);
		IParsedNode[] nodes = node.getChildren();

		Assert.assertEquals(43, nodes.length);
		
	}

	@Test
	public void testSiblings() {
		String s = "SELECT * from dbo.test where name like '%test%' ;";
		ParseTree tree = Antlr4Utils.get(s).getChild(0).getChild(0).getChild(0).getChild(0);
		ParsedNode node = new ParsedNode(tree);
		IParsedNode[] nodes = node.getSiblings();
		Assert.assertEquals(39, nodes.length);
	
	}

	@Test
	public void testParents() {
		String s = "SELECT * from dbo.test where name like '%test%' ;";
		ParseTree tree = Antlr4Utils.get(s).getChild(0).getChild(0).getChild(0).getChild(0);
		ParsedNode node = new ParsedNode(tree);
		IParsedNode[] nodes = node.getParents();
		Assert.assertEquals(4, nodes.length);
		
	}
}