package org.sonar.plugins.tsql.sensors.custom.providers;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.sensors.custom.ParsedNode;

public class ParentNodesProvider implements INodesProvider {

	@Override
	public ParsedNode[] getNodes(final ParsedNode node) {
		final List<ParsedNode> nodes = new ArrayList<ParsedNode>();

		if (node == null || node.getItem() == null) {
			return nodes.toArray(new ParsedNode[0]);
		}

		final ParseTree item = node.getItem();

		ParseTree parent = item.getParent();
		if (parent == null) {
			return new ParsedNode[0];
		}
		int d = 0;
		while (parent != null) {
			d++;
			nodes.add(new ParsedNode(parent, node.getRule(), node.getRepository(), d));
			parent = parent.getParent();
		}

		return nodes.toArray(new ParsedNode[0]);
	}

}
