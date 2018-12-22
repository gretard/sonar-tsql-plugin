package org.sonar.plugins.tsql.antlr.issues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sonar.plugins.tsql.antlr.CandidateNode;
import org.sonar.plugins.tsql.antlr.IParsedNode;
import org.sonar.plugins.tsql.antlr.lines.ILinesProvider;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class FoundViolationsAnalyzer {

	private final ILinesProvider linesProvider;

	private final Map<IParsedNode, List<RuleImplementation>> reported = new HashMap<>();

	public FoundViolationsAnalyzer(final ILinesProvider linesProvider) {
		this.linesProvider = linesProvider;
	}

	public List<TsqlIssue> analyze(final CandidateNode candidate, Map<RuleImplementation, List<IParsedNode>> results) {
		final IParsedNode root = candidate.getNode();
		final RuleImplementation baseRuleImpl = candidate.getRuleImplementation();
		boolean skip = false;
		final Map<RuleImplementation, List<IParsedNode>> selected = new HashMap<>();
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
				if (st.getValue().isEmpty()) {
					selected.get(st.getKey()).add(root);
				} else {
					selected.get(st.getKey()).addAll(st.getValue());
				}

			}
		}
		final List<TsqlIssue> issues = new ArrayList<>();
		if (skip) {
			return issues;
		}
		for (final Entry<RuleImplementation, List<IParsedNode>> st : selected.entrySet()) {
			final RuleImplementation rule = st.getKey();
			for (final IParsedNode node : st.getValue()) {
				reported.putIfAbsent(node, new ArrayList<>());
				if (reported.get(node).contains(rule)) {
					continue;
				}
				reported.get(node).add(rule);
				final TsqlIssue issue = new TsqlIssue();
				if (rule.getRuleViolationMessage() == null) {
					issue.setDescription(baseRuleImpl.getRuleViolationMessage());

				} else {
					issue.setDescription(rule.getRuleViolationMessage());

				}
				issue.setType(candidate.getKey());
				issue.setExternal(candidate.isAdhoc());
				issue.setRepositoryKey(candidate.getRepoKey());
				issue.setLine(this.linesProvider.getLine(node));
				issues.add(issue);
			}

		}

		return issues;
	}
}
