package org.sonar.plugins.tsql.antlr.nodes;

import java.util.ArrayList;
import java.util.List;

import org.antlr.tsql.TSqlParser.Cfl_statementContext;
import org.antlr.v4.runtime.tree.ParseTree;

public class ParsedNode implements IParsedNode {

	private final ParseTree item;

	private final int distance;

	private final String className;

	private final int index;

	private final int index2;

	public ParsedNode(final ParseTree item) {
		this.item = item;
		this.className = (item != null ? item.getClass().getSimpleName() : null);
		this.index = 0;
		this.index2 = 0;
		this.distance = 0;
	}

	public ParsedNode(final ParseTree item, int level, int index, int index2) {
		this.item = item;
		this.index = index;
		this.index2 = index2;
		this.className = item.getClass().getSimpleName();
		this.distance = level;
	}

	public int getIndex() {
		return index;
	}

	public int getIndex2() {
		return index2;
	}

	public String getText() {
		if (this.item == null) {
			return null;
		}
		return this.item.getText();
	}

	public ParseTree getItem() {
		return this.item;
	}

	public String getClassName() {
		return this.className;
	}

	public IParsedNode[] getSiblings() {
		final List<IParsedNode> nodes = new ArrayList<>();
		if (this.item == null || this.item.getParent() == null) {
			return nodes.toArray(new IParsedNode[0]);
		}
		ParseTree parseTreeItem = this.item.getParent();
		visit(nodes, parseTreeItem, 0);
		return nodes.toArray(new IParsedNode[0]);

	}

	public IParsedNode[] getParents() {
		List<IParsedNode> nodes = new ArrayList<>();
		if (this.item == null) {
			return nodes.toArray(new IParsedNode[0]);
		}
		ParseTree parseTreeItem = this.item.getParent();
		int i = 0;
		while (parseTreeItem != null) {
			i++;
			nodes.add(new ParsedNode(parseTreeItem, i, 1, -1));
			parseTreeItem = parseTreeItem.getParent();
		}

		return nodes.toArray(new IParsedNode[0]);
	}

	public IParsedNode[] getChildren() {
		final List<IParsedNode> nodes = new ArrayList<>();
		if (this.item == null) {
			return nodes.toArray(new IParsedNode[0]);
		}
		final ParseTree parseTreeItem = this.item;
		visit(nodes, parseTreeItem, 0);
		return nodes.toArray(new IParsedNode[0]);
	}

	void visit(final List<IParsedNode> nodes, final ParseTree tree, int level) {
		if (tree == null) {
			return;
		}
		final int newLevel = level + 1;
		final int c = tree.getChildCount();
		int j = c * -1;
		for (int i = 0; i < c; i++) {
			final ParseTree child = tree.getChild(i);
			final ParsedNode node = new ParsedNode(child, newLevel, i + 1, j++);
			nodes.add(node);
			visit(nodes, child, newLevel);
		}
	}

	@Override
	public int getDistance() {
		return this.distance;
	}

	@Override
	public IParsedNode getControlFlowParent() {
		if (this.item == null) {
			return new ParsedNode(null);
		}
		ParseTree parent1 = item.getParent();
		while (parent1 != null) {
			if (parent1 instanceof Cfl_statementContext) {
				return new ParsedNode(parent1);
			}
			parent1 = parent1.getParent();
		}
		return new ParsedNode(null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParsedNode other = (ParsedNode) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		return true;
	}

}
