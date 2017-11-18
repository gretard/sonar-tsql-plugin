package org.sonar.plugins.tsql.sensors.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.sensors.custom.lines.ILinesProvider;
import org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNode;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

public class FoundViolationsAnalyzer {

	private final ILinesProvider linesProvider;

	private final Map<IParsedNode, List<RuleImplementation>> reported = new HashMap<>();

	private static final Logger LOGGER = Loggers.get(FoundViolationsAnalyzer.class);

	public FoundViolationsAnalyzer(ILinesProvider linesProvider) {
		this.linesProvider = linesProvider;
	}

	public List<TsqlIssue> create(final CandidateNode candidate, Map<RuleImplementation, List<IParsedNode>> results) {
		final IParsedNode root = candidate.getNode();
		final Rule baseRule = candidate.getRule();
		boolean skip = false;
		final Map<RuleImplementation, List<IParsedNode>> selected = new HashMap<RuleImplementation, List<IParsedNode>>();
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
					issue.setDescription(baseRule.getRuleImplementation().getRuleViolationMessage());

				} else {
					issue.setDescription(rule.getRuleViolationMessage());

				}
				issue.setType(baseRule.getKey());
				issue.setRepositoryKey(candidate.getKey());
				issue.setLine(this.linesProvider.getLine(node));
				issues.add(issue);
			}

		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(String.format("Found %s issues for rule %s in %s repository on %s node", issues.size(),
					baseRule.getKey(), candidate.getKey(), root.getText()));
		}
		return issues;
	}
}
