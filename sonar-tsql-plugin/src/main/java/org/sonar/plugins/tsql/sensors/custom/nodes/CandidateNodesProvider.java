package org.sonar.plugins.tsql.sensors.custom.nodes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.checks.custom.RuleMode;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.sensors.antlr4.CandidateRule;
import org.sonar.plugins.tsql.sensors.antlr4.PluginHelper;
import org.sonar.plugins.tsql.sensors.custom.matchers.IMatcher;
import org.sonar.plugins.tsql.sensors.custom.matchers.NodeNameAndOrClassMatcher;

@SuppressWarnings("rawtypes")
public class CandidateNodesProvider extends AbstractParseTreeVisitor {

	private final Map<String, CandidateNode> groupedNodes = new HashMap<>();
	private final List<CandidateNode> singleNodes = new LinkedList<>();
	private final IMatcher matcher;
	private final CandidateRule[] rules;

	public CandidateNodesProvider(final SqlRules... rules) {
		this(new NodeNameAndOrClassMatcher(), PluginHelper.convert(rules));
	}

	public CandidateNodesProvider(final CandidateRule... rules) {
		this(new NodeNameAndOrClassMatcher(), rules);
	}

	public CandidateNodesProvider(final IMatcher matcher, final CandidateRule... rules) {
		this.rules = rules;
		this.matcher = matcher;
	}

	private CandidateNode[] getNodes() {
		singleNodes.addAll(groupedNodes.values());
		return singleNodes.toArray(new CandidateNode[0]);
	}

	@Override
	public Object visit(final ParseTree tree) {

		final int n = tree.getChildCount();

		for (int i = 0; i < n; i++) {
			final ParseTree c = tree.getChild(i);
			visit(c);
		}
		final ParsedNode parsedNode = new org.sonar.plugins.tsql.sensors.custom.nodes.ParsedNode(tree);

		for (final CandidateRule rule : this.rules) {

			final RuleImplementation ruleImplemention = rule.getRuleImplementation();
			if (matcher.match(ruleImplemention, parsedNode)) {
				final CandidateNode node = new CandidateNode(rule.getKey(), rule.getRule(), parsedNode);
				if (ruleImplemention.getRuleMode() == RuleMode.GROUP) {
					final String name = tree.getText();
					groupedNodes.putIfAbsent(name, node);
				} else {
					singleNodes.add(node);
				}
			}

		}

		return null;
	}

	public CandidateNode[] getNodes(final ParseTree node) {
		if (node == null) {
			return new CandidateNode[0];
		}
		this.visit(node);
		return this.getNodes();
	}

}
