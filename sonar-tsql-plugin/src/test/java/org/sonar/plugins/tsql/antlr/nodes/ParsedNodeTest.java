package org.sonar.plugins.tsql.antlr.nodes;

import org.antlr.tsql.TSqlParser.Cfl_statementContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.AntlrUtils;

public class ParsedNodeTest {

	@Test
	public void testGetChildren() {
		String s = "SELECT * from dbo.test where name like '%test%' ;";
		ParseTree tree = AntlrUtils.getRequest(s).getRoot();
		ParsedNode node = new ParsedNode(tree);
		IParsedNode[] nodes = node.getChildren();

		Assert.assertEquals(43, nodes.length);

	}

	@Test
	public void testControlFlowParent() {
		String s = "IF @a > 0  SELECT 1 else SELECT 2;";
		ParseTree tree = AntlrUtils.getRequest(s).getRoot();
		ParsedNode node = new ParsedNode(tree.getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0));
		IParsedNode parentNode = node.getControlFlowParent();
		Assert.assertNotNull(parentNode);
		Assert.assertEquals(Cfl_statementContext.class.getSimpleName(), parentNode.getClassName());
	}

	@Test
	public void testControlFlowParentNotContrl() {
		String s = "SELECT 1;";
		ParseTree tree = AntlrUtils.getRequest(s).getRoot();
		ParsedNode node = new ParsedNode(tree.getChild(0).getChild(0));
		IParsedNode parentNode = node.getControlFlowParent();
		Assert.assertNotNull(parentNode);
		Assert.assertEquals(null, parentNode.getClassName());
	}

	@Test
	public void testGetItemNull() {
		ParsedNode node = new ParsedNode(null);
		Assert.assertEquals(0, node.getChildren().length);
		Assert.assertEquals(0, node.getParents().length);
		Assert.assertEquals(0, node.getSiblings().length);
		Assert.assertNotNull(node.getControlFlowParent());
		Assert.assertNull(node.getText());
	}

	@Test
	public void testSiblings() {
		String s = "SELECT * from dbo.test where name like '%test%' ;";
		ParseTree tree = AntlrUtils.getRequest(s).getRoot().getChild(0).getChild(0).getChild(0).getChild(0);
		ParsedNode node = new ParsedNode(tree);
		IParsedNode[] nodes = node.getSiblings();
		Assert.assertEquals(39, nodes.length);

	}

	@Test
	public void testParents() {
		String s = "SELECT * from dbo.test where name like '%test%' ;";
		ParseTree tree = AntlrUtils.getRequest(s).getRoot().getChild(0).getChild(0).getChild(0).getChild(0);
		ParsedNode node = new ParsedNode(tree);
		IParsedNode[] nodes = node.getParents();
		Assert.assertEquals(4, nodes.length);

	}
}
