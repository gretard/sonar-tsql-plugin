package org.sonar.plugins.tsql.sensors.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.sensors.custom.lines.ILinesProvider;
import org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;
import org.sonar.plugins.tsql.sensors.custom.nodes.NodeUsesProvider;

public class DefaultCustomRulesViolationsProvider implements ICustomRulesViolationsProvider {

	private Rule[] rules;
	private ILinesProvider linesProvider;

	public DefaultCustomRulesViolationsProvider(ILinesProvider linesProvider, Rule... rules) {
		this.linesProvider = linesProvider;
		this.rules = rules;
	}

	@Override
	public TsqlIssue[] getIssues(ParseTree parseTree) {
		List<TsqlIssue> issues = new ArrayList<>();
		if (parseTree == null) {
			return new TsqlIssue[0];
		}
		for (Rule r : rules) {
			final CandidateNodesProvider visitor = new org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider(
					r);
			IParsedNode[] candidates = visitor.getNodes(parseTree);
			for (IParsedNode candidate : candidates) {
				NodesMatchingRulesProvider m = new NodesMatchingRulesProvider(new NodeUsesProvider(parseTree));
				Map<RuleImplementation, List<IParsedNode>> results = m.check(r.getRuleImplementation(), candidate);
				issues.addAll(create(r, candidate, results));
			}
		}

		return issues.toArray(new TsqlIssue[0]);
	}

	List<TsqlIssue> create(Rule r, IParsedNode root, Map<RuleImplementation, List<IParsedNode>> results) {

		boolean skip = false;
		Map<RuleImplementation, List<IParsedNode>> selected = new HashMap<RuleImplementation, List<IParsedNode>>();

		for (final Entry<RuleImplementation, List<IParsedNode>> st : results.entrySet()) {
			final List<IParsedNode> nodes = st.getValue();
			final RuleImplementation rrule = st.getKey();
			final int found = nodes.size();
			boolean add = false;
			switch (rrule.getRuleResultType()) {
			case DEFAULT:
				break;
			case FAIL_IF_FOUND:
				if (found > 0) {
					add = true;
				}
				break;
			case FAIL_IF_NOT_FOUND:
				if (found == 0) {
					add = true;
				}
				break;
			case SKIP_IF_FOUND:
				if (found > 0) {
					skip = true;
				}
				break;
			case SKIP_IF_NOT_FOUND:
				if (found == 0) {
					skip = true;
				}
				break;
			case FAIL_IF_LESS_FOUND:
				if (found < rrule.getTimes()) {
					add = true;
				}
				break;
			case FAIL_IF_MORE_FOUND:
				if (found > rrule.getTimes()) {
					add = true;
				}
				break;
			default:
				break;
			}
			if (add) {
				selected.putIfAbsent(st.getKey(), new ArrayList<>());
				if (st.getValue().size() == 0) {
					selected.get(st.getKey()).add(root);
				} else {
					selected.get(st.getKey()).addAll(st.getValue());
				}

			}
		}
		List<TsqlIssue> issues = new ArrayList<>();
		if (!skip) {
			for (final Entry<RuleImplementation, List<IParsedNode>> st : selected.entrySet()) {
				RuleImplementation rule = st.getKey();
				for (IParsedNode node : st.getValue()) {
					final TsqlIssue issue = new TsqlIssue();
					if (rule.getRuleViolationMessage() == null) {
						issue.setDescription(r.getRuleImplementation().getRuleViolationMessage());

					} else {
						issue.setDescription(rule.getRuleViolationMessage());

					}
					issue.setType(r.getKey());
					issue.setLine(this.linesProvider.getLine(node));
					issues.add(issue);
				}

			}

		}
		return issues;
	}
}
