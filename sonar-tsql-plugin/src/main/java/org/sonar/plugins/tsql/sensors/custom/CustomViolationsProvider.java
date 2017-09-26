package org.sonar.plugins.tsql.sensors.custom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.RuleMatchType;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class CustomViolationsProvider implements IViolationsProvider {

	private final ILinesProvider linesProvider;
	private final INamesChecker checker = new DefaultNamesChecker();
	private static final Logger LOGGER = Loggers.get(CustomViolationsProvider.class);
	private final INodesProvider siblingsProvider = new SiblingsNodesProvider();
	private final INodesProvider childrenProvider = new ChildrenNodesProvider();
	private final INodesProvider parentsProvider = new ParentNodesProvider();
	private boolean isDebug = LOGGER.isDebugEnabled();

	public CustomViolationsProvider(final ILinesProvider linesProvider) {
		this.linesProvider = linesProvider;
	}

	public TsqlIssue[] getIssues(final ParsedNode... nodes) {
		LOGGER.debug(String.format("Have %s nodes for checking", nodes.length));
		final List<TsqlIssue> finalIssues = new LinkedList<>();

		for (final ParsedNode node : nodes) {
			final Map<RuleImplementation, List<ParsedNode>> statuses = new HashMap<>();
			final Rule ruleDefinition = node.getRule();
			final RuleImplementation rule = node.getRule().getRuleImplementation();
			visit(ruleDefinition, rule, null, node, Direction.MAIN, statuses);

			final StringBuilder sb = new StringBuilder();
			boolean shouldSkip = false;
			final List<RuleImplementation> violated = new LinkedList<>();
			for (Entry<RuleImplementation, List<ParsedNode>> st : statuses.entrySet()) {
				final RuleImplementation rrule = st.getKey();
				final List<ParsedNode> vNodes = st.getValue();
				final int found = vNodes.size();

				switch (rrule.getRuleResultType()) {
				case DEFAULT:
					break;
				case FAIL_IF_FOUND:
					if (found > 0) {
						violated.add(rrule);
						sb.append(rrule.getRuleViolationMessage() + "\r\n");
					}
					break;
				case FAIL_IF_NOT_FOUND:
					if (found == 0) {
						violated.add(rrule);
						sb.append(rrule.getRuleViolationMessage() + "\r\n");
					}
					break;
				case SKIP_IF_FOUND:
					if (found > 0) {
						shouldSkip = true;
						violated.add(rrule);
						sb.append(rrule.getRuleViolationMessage() + "\r\n");
					}
					break;
				case SKIP_IF_NOT_FOUND:
					if (found == 0) {
						shouldSkip = true;
						violated.add(rrule);
						sb.append(rrule.getRuleViolationMessage() + "\r\n");
					}
					break;
				case FAIL_IF_LESS_FOUND:
					if (found < rrule.getTimes()) {
						violated.add(rrule);
						sb.append(rrule.getRuleViolationMessage() + "\r\n");
					}
					break;
				case FAIL_IF_MORE_FOUND:
					if (found > rrule.getTimes()) {
						violated.add(rrule);
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

				final TsqlIssue issue = new TsqlIssue();
				issue.setDescription(sb.toString());
				issue.setType(ruleDefinition.getKey());
				issue.setLine(this.linesProvider.getLine(node));
				finalIssues.add(issue);
			}
		}
		return finalIssues.toArray(new TsqlIssue[0]);
	}

	private void visit(Rule ruleDefinition, RuleImplementation rule, RuleImplementation parent, ParsedNode root,
			Direction dir, Map<RuleImplementation, List<ParsedNode>> statuses) {
		final List<ParsedNode> nodesToCheck = new LinkedList<>();
		if (dir == Direction.MAIN) {
			nodesToCheck.add(root);
		}
		if (dir == Direction.CHILD) {
			final List<ParsedNode> items = root.getChildren();
			if (!items.isEmpty()) {
				nodesToCheck.addAll(items);
			} else {
				nodesToCheck.addAll(Arrays.asList(this.childrenProvider.getNodes(root)));
			}

		}
		if (dir == Direction.PARENT) {
			final List<ParsedNode> items = root.getParents();
			if (!items.isEmpty()) {
				nodesToCheck.addAll(items);
			} else {
				nodesToCheck.addAll(Arrays.asList(this.parentsProvider.getNodes(root)));

			}
		}
		if (dir == Direction.SIBLING) {

			final List<ParsedNode> items = root.getSiblings();
			if (!items.isEmpty()) {
				nodesToCheck.addAll(items);
			} else {
				nodesToCheck.addAll(Arrays.asList(this.siblingsProvider.getNodes(root)));
			}

		}
		if (dir == Direction.USE) {
			nodesToCheck.addAll(root.getUses());
		}
		
		if (nodesToCheck.isEmpty()) {
			return;
		}

		final ParsedNode[] candidates = nodesToCheck.toArray(new ParsedNode[0]);

		final List<ParsedNode> violatingNodes = new LinkedList<ParsedNode>();
		for (final ParsedNode node : candidates) {
			if (node.getItem() == null) {
				continue;
			}
			final String className = node.getItem().getClass().getSimpleName();
			boolean shouldAdd = false;
			boolean classNameMatch = checker.containsClassName(rule, className);
			
			final RuleMatchType type = rule.getRuleMatchType();
			switch (type) {
			case CLASS_ONLY:
				if (classNameMatch) {
					shouldAdd = true;
				}
				break;
			case DEFAULT:
				break;
			case FULL:
			case TEXT_AND_CLASS:
			case STRICT:
			case TEXT_ONLY:
				final String txt = node.getText();
				final boolean textIsFound = checker.containsName(rule, txt);
				final boolean nodeContainsName = txt.contains(root.getText());
				final boolean parentsMatch = checker.checkParent(node, root);
				if (type == RuleMatchType.FULL && classNameMatch && textIsFound && nodeContainsName) {
					shouldAdd = true;
				}
			
				if (type == RuleMatchType.STRICT && parentsMatch && classNameMatch && textIsFound && nodeContainsName) {
					shouldAdd = true;
				}
			
				if (type == RuleMatchType.TEXT_ONLY && textIsFound) {
					shouldAdd = true;
				}

				if (type == RuleMatchType.TEXT_AND_CLASS && textIsFound && classNameMatch) {
					shouldAdd = true;
				}
				break;
			default:
				break;
			}

			if (shouldAdd) {
				violatingNodes.add(node);
			}

		}

		statuses.put(rule, violatingNodes);

		for (final RuleImplementation siblingRule : rule.getSiblingsRules().getRuleImplementation()) {
			visit(ruleDefinition, siblingRule, rule, root, Direction.SIBLING, statuses);
		}
		for (final RuleImplementation parentRule : rule.getParentRules().getRuleImplementation()) {
			visit(ruleDefinition, parentRule, rule, root, Direction.PARENT, statuses);
		}

		for (final RuleImplementation childRule : rule.getChildrenRules().getRuleImplementation()) {
			visit(ruleDefinition, childRule, rule, root, Direction.CHILD, statuses);
		}
		for (final RuleImplementation useRule : rule.getUsesRules().getRuleImplementation()) {
			visit(ruleDefinition, useRule, rule, root, Direction.USE, statuses);
		}
	}
}
