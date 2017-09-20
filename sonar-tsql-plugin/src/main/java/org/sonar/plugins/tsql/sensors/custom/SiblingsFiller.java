package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;

public class SiblingsFiller implements IFiller {

	@Override
	public void fill(final Rule rule, final ParsedNode... nodes) {
		for (final ParsedNode node : nodes) {
			final ParseTree item = node.getItem();
			if (item == null) {
				return;
			}
			final ParseTree parent = item.getParent();
			if (parent == null) {
				continue;
			}
			final int c = parent.getChildCount();
			for (int i = 0; i < c; i++) {
				final ParseTree sibling = parent.getChild(i);
				if (sibling == item) {
					continue;
				}
				node.getParents().add(new ParsedNode(sibling,  rule,
						node.getRepository()));
			}

		}
	}

}
