package org.sonar.plugins.tsql.sensors.custom.matchers;

import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.TextCheckType;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

public class NodesMatcher {

	public boolean matchesClassName(RuleImplementation rule, String name) {
		for (String s : rule.getNames().getTextItem()) {
			if (name.equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsSameText(IParsedNode node, IParsedNode parent) {
		String text1 = node.getText();
		String text2 = parent.getText();

		return StringUtils.contains(text1, text2) || StringUtils.contains(text2, text1);

	}

	public boolean parentsMatch(IParsedNode node, IParsedNode node2) {
		IParsedNode parent1 = node.getControlFlowParent();
		IParsedNode parent2 = node2.getControlFlowParent();

		if (parent1 == null || parent2 == null) {
			return false;
		}
		return node.getControlFlowParent().getItem() == node2.getControlFlowParent().getItem();
	}

	public boolean match(RuleImplementation rule, ParseTree item) {
		boolean checkClass = !rule.getNames().getTextItem().isEmpty();
		boolean checkNames = !rule.getTextToFind().getTextItem().isEmpty();
		if (checkClass && checkNames) {
			return this.matchesClassName(rule, item.getClass().getSimpleName())
					&& this.matchesText(rule, item.getText());
		}

		if (checkClass) {
			return this.matchesClassName(rule, item.getClass().getSimpleName());
		}
		return this.matchesText(rule, item.getText());

	}

	public boolean matchesText(RuleImplementation rule, String text) {

		final TextCheckType type = rule.getTextCheckType();
		if (rule.getTextToFind().getTextItem().isEmpty()) {
			return true;

		}
		for (String s : rule.getTextToFind().getTextItem()) {
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
}
