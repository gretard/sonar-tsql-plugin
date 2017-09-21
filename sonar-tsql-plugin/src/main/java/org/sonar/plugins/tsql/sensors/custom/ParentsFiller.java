package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;

public class ParentsFiller implements IFiller {

	@Override
	public void fill(final ParsedNode... nodes) {
		for (final ParsedNode node : nodes) {
			final ParseTree item = node.getItem();
			if (item == null) {
				return;
			}
			ParseTree parent = item.getParent();
			int d = 0;
			while (parent != null) {
				d++;
				node.getParents().add(new ParsedNode(parent, node.getRule(), node.getRepository(), d));
				parent = parent.getParent();
			}
		}
	}

}
