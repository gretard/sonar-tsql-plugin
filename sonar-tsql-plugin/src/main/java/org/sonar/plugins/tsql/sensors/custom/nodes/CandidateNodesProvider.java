package org.sonar.plugins.tsql.sensors.custom.nodes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.RuleMode;
import org.sonar.plugins.tsql.sensors.custom.matchers.NodesMatcher;

@SuppressWarnings("rawtypes")
public class CandidateNodesProvider extends AbstractParseTreeVisitor implements INodesProvider<ParseTree> {
	
	private final RuleImplementation ruleImplemention;
	private final Map<String, org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode> groupedNodes = new HashMap<>();
	private final List<IParsedNode> singleNodes = new LinkedList<>();
	private final NodesMatcher checker = new NodesMatcher();

	public CandidateNodesProvider(final Rule rule) {
		this.ruleImplemention = rule.getRuleImplementation();
	}

	private org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode[] getNodes() {
		singleNodes.addAll(groupedNodes.values());
		return singleNodes.toArray(new org.sonar.plugins.tsql.sensors.custom.nodes.ParsedNode[0]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitChildren(final RuleNode node) {

		final String name = node.getText();
		if (checker.match(ruleImplemention, node)) {
			final ParsedNode parsedNode = new org.sonar.plugins.tsql.sensors.custom.nodes.ParsedNode(node);

			if (ruleImplemention.getRuleMode() == RuleMode.GROUP) {
				groupedNodes.putIfAbsent(name, parsedNode);
			} else {
				singleNodes.add(parsedNode);
			}

		}
		final int n = node.getChildCount();

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
	public IParsedNode[] getNodes(ParseTree node) {
		this.visit(node);
		return this.getNodes();
	}

}
