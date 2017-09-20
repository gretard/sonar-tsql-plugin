package org.sonar.plugins.tsql.sensors.custom;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;

public class ParentNodesProvider implements INodesProvider {

	@Override
	public ParsedNode[] getNodes(ParsedNode node) {
		List<ParsedNode> nodes = new ArrayList<ParsedNode>();

		final ParseTree item = node.getItem();

		ParseTree parent = item.getParent();
		if (parent == null) {
			return new ParsedNode[0];
		}
		int d = 0;
		while (parent != null) {
			d++;
			nodes.add(new ParsedNode(node.getName(), parent, parent.getClass().getSimpleName(), node.getRule(),
					node.getRepository(), d));
			parent = parent.getParent();
		}

		return nodes.toArray(new ParsedNode[0]);
	}

}
