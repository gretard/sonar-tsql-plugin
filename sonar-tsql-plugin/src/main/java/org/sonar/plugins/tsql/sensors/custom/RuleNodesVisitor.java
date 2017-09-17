package org.sonar.plugins.tsql.sensors.custom;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.sonar.plugins.tsql.antlr4.tsqlBaseVisitor;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.RuleMode;

@SuppressWarnings("rawtypes")
public class RuleNodesVisitor extends tsqlBaseVisitor {
	private final Rule rule;
	private final RuleImplementation ruleImplemention;
	private String key;
	final INamesChecker namesChecker = new DefaultNamesChecker();

	public RuleNodesVisitor(final Rule rule, final String key) {
		this.rule = rule;
		this.key = key;
		this.ruleImplemention = rule.getRuleImplementation();
	}

	final Map<String, org.sonar.plugins.tsql.sensors.custom.ParsedNode> nodes = new HashMap<String, org.sonar.plugins.tsql.sensors.custom.ParsedNode>();

	public org.sonar.plugins.tsql.sensors.custom.ParsedNode[] getNodes() {
		diff.addAll(nodes.values());
		return diff.toArray(new org.sonar.plugins.tsql.sensors.custom.ParsedNode[0]);
	}

	private final List<ParsedNode> diff = new LinkedList<ParsedNode>();

	@Override
	public Object visitChildren(final RuleNode node) {

		final int n = node.getChildCount();
		final String name = node.getText();
		if ((namesChecker.containsClassName(ruleImplemention, node)
				|| (ruleImplemention.getNames().getTextItem().isEmpty() && namesChecker.containsName(ruleImplemention, name)))) {
			final ParsedNode parsedNode = new org.sonar.plugins.tsql.sensors.custom.ParsedNode(node, rule, key);
	
			if (ruleImplemention.getRuleMode() == RuleMode.GROUP) {
				nodes.putIfAbsent(name, parsedNode);
			} else {
				diff.add(parsedNode);
			}

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

}
