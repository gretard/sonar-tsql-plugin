package org.sonar.plugins.tsql.sensors.custom;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.sonar.plugins.tsql.antlr4.tsqlBaseVisitor;
import org.sonar.plugins.tsql.rules.custom.RuleMode;

public class ParsedNodeUsesFiller extends tsqlBaseVisitor implements IFiller {

	private final ParseTree tree;
	private final UsesFindRule main = new UsesFindRule();
	private ParsedNode[] nodes;

	public ParsedNodeUsesFiller(final ParseTree tree) {
		this.tree = tree;
	}

	@Override
	public Object visitChildren(final RuleNode node) {

		final int n = node.getChildCount();

		for (final ParsedNode x : this.nodes) {

			main.root(x, node, x.getRule());

		}

		for (int i = 0; i < n; i++) {
			if (!shouldVisitNextChild(node, defaultResult())) {
				break;
			}
			final ParseTree c = node.getChild(i);
			c.accept(this);

		}
		return null;

	}

	@Override
	public void fill(final ParsedNode... nodes) {
		{
			final List<ParsedNode> tmp = new LinkedList<>();
			for (final ParsedNode n : nodes) {
				if (n.getRule().getRuleImplementation().getRuleMode() == RuleMode.GROUP) {
					tmp.add(n);
				}
			}
			if (tmp.isEmpty()) {
				return;
			}
			this.nodes = tmp.toArray(new ParsedNode[0]);
			visit(this.tree);

		}
	}

}
