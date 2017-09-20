package org.sonar.plugins.tsql.sensors.custom;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;

public class ChildrenNodesProvider implements INodesProvider {

	@Override
	public ParsedNode[] getNodes(ParsedNode node) {
		List<ParsedNode> nodes = new ArrayList<ParsedNode>();
		visit(nodes, node);
		return nodes.toArray(new ParsedNode[0]);
	}

	void visit(List<ParsedNode> nodes, final ParsedNode root) {
		ParseTree tree = root.getItem();
		int c = root.getItem().getChildCount();

		for (int i = 0; i < c; i++) {
			final ParseTree child = tree.getChild(i);
			ParsedNode node = new ParsedNode(child, root.getRule(), root.getRepository());
			nodes.add(node);
			visit(nodes, node);
		}
	}
}
