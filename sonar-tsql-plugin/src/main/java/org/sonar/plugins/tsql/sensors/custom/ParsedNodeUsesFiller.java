package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.sonar.plugins.tsql.antlr4.tsqlBaseVisitor;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleMode;

public class ParsedNodeUsesFiller extends tsqlBaseVisitor implements IFiller {

	private final ParseTree tree;

	public ParsedNodeUsesFiller(ParseTree tree) {
		this.tree = tree;
	}

	final UsesFindRule main = new UsesFindRule();
	private ParsedNode[] nodes;
	private Rule rule;

	@Override
	public Object visitChildren(final RuleNode node) {

		final int n = node.getChildCount();

		for (ParsedNode x : this.nodes) {

			main.root(x, node, rule);
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
	public void fill(final Rule rule, final ParsedNode... nodes) {
		if (rule.getRuleImplementation().getRuleMode() == RuleMode.GROUP) {
			this.nodes = nodes;
			this.rule = rule;
			visit(this.tree);
		}
	}

}
