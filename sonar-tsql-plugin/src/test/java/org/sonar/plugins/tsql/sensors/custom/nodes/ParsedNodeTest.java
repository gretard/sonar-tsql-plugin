package org.sonar.plugins.tsql.sensors.custom.nodes;

import java.util.List;

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
		List<IParsedNode> nodes = node.getChildren();

		Assert.assertEquals(43, nodes.size());
		for (IParsedNode n : nodes) {
			System.out.println(n.getText() + " " + n.getText() + " " + n.getClassName() + " " + n.getDistance() + " "
					+ n.getIndex()+" "+n.getIndex2());
		}
	//	Antlr4Utils.print(tree, 0);
	}
	@Test
	public void testSiblings() {
		String s = "SELECT * from dbo.test where name like '%test%' ;";
		ParseTree tree = Antlr4Utils.get(s).getChild(0).getChild(0).getChild(0).getChild(0);
		ParsedNode node = new ParsedNode(tree);
		List<IParsedNode> nodes = node.getSiblings();
		Assert.assertEquals(39, nodes.size());
		for (IParsedNode n : nodes) {
			System.out.println(n.getText() + " " + n.getText() + " " + n.getClassName() + " " + n.getDistance() + " "
					+ n.getIndex());
		}
		//Antlr4Utils.print(tree, 0);
	}
	@Test
	public void testParents() {
		String s = "SELECT * from dbo.test where name like '%test%' ;";
		ParseTree tree = Antlr4Utils.get(s).getChild(0).getChild(0).getChild(0).getChild(0);
		ParsedNode node = new ParsedNode(tree);
		List<IParsedNode> nodes = node.getParents();
		Assert.assertEquals(4, nodes.size());
		for (IParsedNode n : nodes) {
			System.out.println(n.getText() + " " + n.getText() + " " + n.getClassName() + " " + n.getDistance() + " "
					+ n.getIndex());
		}
	}
}
