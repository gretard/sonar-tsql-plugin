package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;

public class ChildrenFiller implements IFiller {

	void visit(final ParseTree tree, final ParsedNode root, final Rule rule) {
		int c = tree.getChildCount();

		for (int i = 0; i < c; i++) {
			final ParseTree child = tree.getChild(i);
			root.getChildren()
					.add(new ParsedNode(root.getName(), child, child.getClass().getName(), rule, root.getRepository()));
			visit(child, root, rule);
		}
	}

	@Override
	public void fill(final Rule rule, final ParsedNode... nodes) {
		for (final ParsedNode node : nodes) {
			final ParseTree item = node.getItem();
			if (item == null) {
				return;
			}

			visit(item, node, rule);
		}

	}

}
