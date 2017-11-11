package org.sonar.plugins.tsql.sensors.custom.matchers;

import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.RuleMatchType;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

public class RulesMatcher {

	private final NodesMatcher checker = new NodesMatcher();

	public boolean match(RuleImplementation rule, IParsedNode parent, IParsedNode node) {
		final RuleMatchType type = rule.getRuleMatchType();

		if (rule.getDistance() > 0 && parent != null) {
			int delta = parent.getDistance() - node.getDistance();
			if (rule.getDistance() > delta) {
				return false;
			}
		}
		boolean classMatch = checker.matchesClassName(rule, node.getClassName());
		boolean textMatch = checker.matchesText(rule, node.getText());
		if (type == RuleMatchType.DEFAULT || type == RuleMatchType.CLASS_ONLY) {
			return classMatch;
		}

		switch (type) {
		case TEXT_AND_CLASS:
			return classMatch && textMatch;
		case TEXT_ONLY:
			return textMatch;
		case FULL:
			return textMatch && classMatch && checker.containsSameText(node, parent);
		case STRICT:
			if (parent == null) {
				throw new IllegalArgumentException("Can't do strict check as parent is null");
			}
			return textMatch && classMatch && checker.containsSameText(node, parent)
					&& checker.parentsMatch(node, parent);
		default:
			break;
		}
		return false;
	}
}
