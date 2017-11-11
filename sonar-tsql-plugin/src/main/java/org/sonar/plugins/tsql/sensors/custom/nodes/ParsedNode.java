package org.sonar.plugins.tsql.sensors.custom.nodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Cfl_statementContext;

public class ParsedNode implements IParsedNode {
	private final ParseTree item;

	private int distance;

	private final String className;

	final List<ParsedNode> siblings = new LinkedList<>();
	final List<ParsedNode> uses = new LinkedList<>();

	final List<ParsedNode> parents = new LinkedList<>();
	final List<ParsedNode> children = new LinkedList<>();

	public ParsedNode(final ParseTree item) {
		this.item = item;
		this.className = item.getClass().getSimpleName();
	}

	public ParsedNode(final ParseTree item, int distance) {
		this.item = item;
		this.className = item.getClass().getSimpleName();
		this.distance = distance;
	}

	public String getText() {
		if (this.item == null) {
			return null;
		}
		return this.item.getText();
	}

	public ParseTree getItem() {
		return item;
	}

	public String getClassName() {
		return className;
	}

	public List<IParsedNode> getSiblings() {
		List<IParsedNode> nodes = new ArrayList<>();
		if (this.item == null || this.item.getParent() == null) {
			return nodes;
		}
		ParseTree parseTreeItem = this.item.getParent();
		visit(nodes, parseTreeItem);
		return nodes;

	}

	public List<IParsedNode> getUses() {
		return new ArrayList<IParsedNode>();
	}

	public List<IParsedNode> getParents() {
		List<IParsedNode> nodes = new ArrayList<>();
		if (this.item == null) {
			return nodes;
		}
		ParseTree parseTreeItem = this.item.getParent();
		int i = 0;
		while (parseTreeItem != null) {
			i--;
			nodes.add(new ParsedNode(parseTreeItem, i));
			parseTreeItem = parseTreeItem.getParent();
		}

		return nodes;
	}

	public List<IParsedNode> getChildren() {
		List<IParsedNode> nodes = new ArrayList<>();
		if (this.item == null) {
			return nodes;
		}
		ParseTree parseTreeItem = this.item;
		visit(nodes, parseTreeItem);
		return nodes;
	}

	void visit(List<IParsedNode> nodes, final ParseTree tree) {
		if (tree == null) {
			return;
		}

		final int c = tree.getChildCount();

		for (int i = 0; i < c; i++) {
			final ParseTree child = tree.getChild(i);
			final ParsedNode node = new ParsedNode(child, i);
			nodes.add(node);
			visit(nodes, child);
		}
	}

	@Override
	public int getDistance() {
		return this.distance;
	}

	@Override
	public IParsedNode getControlFlowParent() {
		ParseTree parent1 = item.getParent();
		while (parent1 != null) {
			if (parent1 instanceof Cfl_statementContext) {
				return new ParsedNode(parent1);
			}
			parent1 = parent1.getParent();
		}
		return new ParsedNode(null);
	}

}
