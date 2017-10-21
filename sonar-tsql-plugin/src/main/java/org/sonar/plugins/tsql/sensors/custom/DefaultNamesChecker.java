package org.sonar.plugins.tsql.sensors.custom;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Cfl_statementContext;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.TextCheckType;

public class DefaultNamesChecker implements INamesChecker {

	@Override
	public boolean containsName(final RuleImplementation rule, final String text) {
		final List<String> textToFind = rule.getTextToFind().getTextItem();
		final TextCheckType type = rule.getTextCheckType();
		if (StringUtils.isEmpty(text)) {
			return false;
		}
		for (final String s : textToFind) {
			switch (type) {
			case DEFAULT:
			case CONTAINS:
				if (StringUtils.containsIgnoreCase(text, s)) {
					return true;
				}
				break;

			case REGEXP:
				if (text.matches(s)) {
					return true;
				}
				break;
			case STRICT:
				if (text.equals(s)) {
					return true;
				}
				break;
			default:
				break;
			}

		}
		return false;
	}

	@Override
	public boolean containsClassName(final RuleImplementation rule, final String text) {
		for (final String name : rule.getNames().getTextItem()) {
			if (text.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsClassName(final RuleImplementation rule, final ParseTree node) {
		final String className = node.getClass().getSimpleName();

		return containsClassName(rule, className);
	}

	@Override
	public boolean containsClassName(final RuleImplementation rule, final ParsedNode node) {
		final String className = node.getClassName();

		return containsClassName(rule, className);
	}

	@Override
	public boolean checkParent(final ParsedNode node, final ParsedNode root) {
		final ParseTree parent1 = getParent(node);
		final ParseTree parent2 = getParent(root);
		if (parent1 == parent2) {
			return true;
		}
		return false;
	}

	private ParseTree getParent(final ParsedNode node) {
		if (node == null) {
			return null;
		}
		ParseTree parent1 = node.getItem().getParent();
		while (parent1 != null) {
			if (parent1 instanceof Cfl_statementContext) {
				return parent1;
			}
			parent1 = parent1.getParent();
		}
		return null;
	}

}
