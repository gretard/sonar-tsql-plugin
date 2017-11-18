package org.sonar.plugins.tsql.sensors.custom.nodes;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("rawtypes")
public class NodeUsesProvider extends AbstractParseTreeVisitor implements INodesProvider<IParsedNode> {

	private final ParseTree root;
	private final List<IParsedNode> nodes = new ArrayList<>();

	private String tempText;

	public NodeUsesProvider(final ParseTree root) {
		this.root = root;
	}

	@Override
	public IParsedNode[] getNodes(final IParsedNode node) {
		this.nodes.clear();
		this.tempText = null;
		if (node == null) {
			return new IParsedNode[0];
		}
		this.tempText = node.getText();
		visit(root);
		IParsedNode[] results = this.nodes.toArray(new IParsedNode[0]);
		this.nodes.clear();
		this.tempText = null;
		return results;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitChildren(final RuleNode node) {

		final int n = node.getChildCount();

		for (int i = 0; i < n; i++) {
			final ParseTree c = node.getChild(i);
			c.accept(this);

		}
		final String textToFind = node.getText();
		if (StringUtils.containsIgnoreCase(textToFind, tempText)
				|| StringUtils.containsIgnoreCase(tempText, textToFind)) {
			nodes.add(new ParsedNode(node));
		}
		return null;

	}

}
