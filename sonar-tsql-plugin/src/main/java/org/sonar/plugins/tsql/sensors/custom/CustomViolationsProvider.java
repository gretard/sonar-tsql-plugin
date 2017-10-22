package org.sonar.plugins.tsql.sensors.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class CustomViolationsProvider implements IViolationsProvider {

	private final ILinesProvider linesProvider;
	private static final Logger LOGGER = Loggers.get(CustomViolationsProvider.class);
	private final INodesProvider siblingsProvider = new SiblingsNodesProvider();
	private final INodesProvider childrenProvider = new ChildrenNodesProvider();
	private final INodesProvider parentsProvider = new ParentNodesProvider();
	private boolean isDebug = LOGGER.isDebugEnabled();
	private final RulesMatcher matcher = new RulesMatcher();

	public CustomViolationsProvider(final ILinesProvider linesProvider) {
		this.linesProvider = linesProvider;
	}
	
	private void initStatuses(Map<RuleImplementation, List<ParsedNode>> statuses, RuleImplementation rule) {
		statuses.putIfAbsent(rule, new ArrayList<>());
		for (RuleImplementation i :rule.getChildrenRules().getRuleImplementation()) {
			initStatuses(statuses, i);
		}
		for (RuleImplementation i :rule.getParentRules().getRuleImplementation()) {
			initStatuses(statuses, i);
		}
		for (RuleImplementation i :rule.getUsesRules().getRuleImplementation()) {
			initStatuses(statuses, i);
		}
		for (RuleImplementation i :rule.getParentRules().getRuleImplementation()) {
			initStatuses(statuses, i);
		}
	}
	public TsqlIssue[] getIssues(final ParsedNode... nodes) {

		LOGGER.debug(String.format("Have %s nodes for checking", nodes.length));
		final List<TsqlIssue> finalIssues = new LinkedList<>();

		for (final ParsedNode node : nodes) {
		
			final Rule ruleDefinition = node.getRule();
			final String ruleKey = ruleDefinition.getKey();
			final RuleImplementation rule = node.getRule().getRuleImplementation();
			final Map<RuleImplementation, List<ParsedNode>> statuses = new HashMap<>();
			initStatuses(statuses, rule );
			visit(ruleDefinition, rule, null, node, null, statuses);
			final StringBuilder sb = new StringBuilder();
			boolean shouldSkip = false;
			final List<RuleImplementation> violated = new LinkedList<>();
			final Map<RuleImplementation, ParsedNode> finalNodes = new HashMap<>();
			for (final Entry<RuleImplementation, List<ParsedNode>> st : statuses.entrySet()) {

				final RuleImplementation rrule = st.getKey();
				final List<ParsedNode> vNodes = st.getValue();
				final int found = vNodes.size();
				switch (rrule.getRuleResultType()) {
				case DEFAULT:
					break;
				case FAIL_IF_FOUND:
					if (found > 0) {
						violated.add(rrule);

						finalNodes.put(rrule, vNodes.get(vNodes.size() - 1));
						sb.append(rrule.getRuleViolationMessage() + "\r\n");
					}
					break;
				case FAIL_IF_NOT_FOUND:
					if (found == 0) {
						violated.add(rrule);
						finalNodes.put(rrule, node);
						sb.append(rrule.getRuleViolationMessage() + "\r\n");
					}
					break;
				case SKIP_IF_FOUND:
					if (found > 0) {
						shouldSkip = true;
						violated.add(rrule);
						sb.append(rrule.getRuleViolationMessage() + "\r\n");
						if (found == 0) {
							finalNodes.put(rrule, node);
						} else {
							finalNodes.put(rrule, vNodes.get(vNodes.size() - 1));
						}
					}
					break;
				case SKIP_IF_NOT_FOUND:
					if (found == 0) {
						shouldSkip = true;
						violated.add(rrule);
						if (found == 0) {
							finalNodes.put(rrule, node);
						} else {
							finalNodes.put(rrule, vNodes.get(vNodes.size() - 1));
						}
						sb.append(rrule.getRuleViolationMessage() + "\r\n");
					}
					break;
				case FAIL_IF_LESS_FOUND:
					if (found < rrule.getTimes()) {
						violated.add(rrule);
						if (found == 0) {
							finalNodes.put(rrule, node);
						} else {
							finalNodes.put(rrule, vNodes.get(vNodes.size() - 1));
						}

						sb.append(rrule.getRuleViolationMessage() + "\r\n");
					}
					break;
				case FAIL_IF_MORE_FOUND:
					if (found > rrule.getTimes()) {
						violated.add(rrule);
						if (found == 0) {
							finalNodes.put(rrule, node);
						} else {
							finalNodes.put(rrule, vNodes.get(vNodes.size() - 1));
						}
						sb.append(rrule.getRuleViolationMessage() + "\r\n");
					}
					break;
				default:
					break;
				}
			}
			if (isDebug) {
				LOGGER.debug(String.format("Found %s violations on rule %s for %s", violated.size(),
						ruleDefinition.getKey(), node.getText()));
			}

			if (!shouldSkip && violated.size() > 0) {
				for (final Entry<RuleImplementation, ParsedNode> st : finalNodes.entrySet()) {
					add(finalIssues, st.getValue(), ruleKey, st.getKey(), st.getKey().getRuleViolationMessage());
				}
			}
		}
		return finalIssues.toArray(new TsqlIssue[0]);
	}

	private void add(final List<TsqlIssue> finalIssues, final ParsedNode node, final String key,
			RuleImplementation violatedRule, String msg) {
		final TsqlIssue issue = new TsqlIssue();
		issue.setDescription(msg);
		issue.setType(key);
		issue.setLine(this.linesProvider.getLine(node));
		finalIssues.add(issue);
	}

	private void visit(Rule ruleDefinition, RuleImplementation rule, RuleImplementation parent, ParsedNode node,
			ParsedNode parentNode, Map<RuleImplementation, List<ParsedNode>> statuses) {
		final List<ParsedNode> violatingNodes = new LinkedList<ParsedNode>();
		statuses.putIfAbsent(rule, violatingNodes);

		boolean shouldAdd = matcher.matchesRule(rule, node, parentNode);

		if (shouldAdd) {
			statuses.get(rule).add(node);

			for (final ParsedNode nnode : this.siblingsProvider.getNodes(node)) {
				for (final RuleImplementation vRule : rule.getSiblingsRules().getRuleImplementation()) {
					visit(ruleDefinition, vRule, rule, nnode, node, statuses);
				}
			}
			for (final ParsedNode nnode : this.parentsProvider.getNodes(node)) {
				for (final RuleImplementation vRule : rule.getParentRules().getRuleImplementation()) {
					visit(ruleDefinition, vRule, rule, nnode, node, statuses);
				}
			}
			for (final ParsedNode nnode : this.childrenProvider.getNodes(node)) {
				for (final RuleImplementation vRule : rule.getChildrenRules().getRuleImplementation()) {
					visit(ruleDefinition, vRule, rule, nnode, node, statuses);
				}
			}

			for (final ParsedNode nnode : node.getUses()) {
				for (final RuleImplementation vRule : rule.getUsesRules().getRuleImplementation()) {
					visit(ruleDefinition, vRule, rule, nnode, node, statuses);
				}
			}

		}

	}

}
