package org.sonar.plugins.tsql.antlr.visitors;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

@SuppressWarnings("rawtypes")
public class CustomTreeVisitor extends AbstractParseTreeVisitor {

	private final IParseTreeItemVisitor[] visitors;

	public CustomTreeVisitor(final IParseTreeItemVisitor... visitors) {
		this.visitors = visitors;
	}

	@Override
	public Object visit(final ParseTree tree) {

		final int n = tree.getChildCount();

		for (int i = 0; i < n; i++) {
			final ParseTree c = tree.getChild(i);
			visit(c);
		}
		for (final IParseTreeItemVisitor visitor : this.visitors) {
			visitor.visit(tree);
		}

		return null;
	}

}
