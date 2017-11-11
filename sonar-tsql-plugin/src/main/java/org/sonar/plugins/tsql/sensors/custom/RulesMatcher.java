package org.sonar.plugins.tsql.sensors.custom;

import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.RuleMatchType;
import org.sonar.plugins.tsql.sensors.custom.names.DefaultNamesChecker;
import org.sonar.plugins.tsql.sensors.custom.names.INamesChecker;

public class RulesMatcher {

	private final INamesChecker checker = new DefaultNamesChecker();

	public boolean matchesRule(final RuleImplementation rule, final IParsedNode node, final IParsedNode parentNode) {
		boolean shouldAdd = false;
		final boolean classNameMatch = checker.containsClassName(rule, node);
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
			if (type == RuleMatchType.TEXT_ONLY && textIsFound) {
				shouldAdd = true;
				break;
			}

			if (type == RuleMatchType.TEXT_AND_CLASS && textIsFound && classNameMatch) {
				shouldAdd = true;
				break;
			}
			final boolean parentsMatch = checker.checkParent(node, parentNode);
			final boolean nodeContainsName = checker.containsNames(rule, node, parentNode);
			if (type == RuleMatchType.FULL && classNameMatch && textIsFound && nodeContainsName) {
				shouldAdd = true;
				break;
			}
			if (type == RuleMatchType.STRICT && classNameMatch && textIsFound && nodeContainsName && parentsMatch) {
				shouldAdd = true;
				break;
			}

			break;
		default:
			break;
		}
		return shouldAdd;
	}

}