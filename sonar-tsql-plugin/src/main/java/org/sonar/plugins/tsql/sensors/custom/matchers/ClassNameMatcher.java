package org.sonar.plugins.tsql.sensors.custom.matchers;

import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

public class ClassNameMatcher extends ABaseMatcher {

	private final boolean isStrict;

	public ClassNameMatcher(boolean isStrict) {
		this.isStrict = isStrict;

	}

	public ClassNameMatcher() {
		this(false);
	}

	@Override
	protected boolean innerMatch(RuleImplementation rule, IParsedNode item) {
		final String name = item.getClassName();
		for (final String classNameToCheck : rule.getNames().getTextItem()) {
			if (name.equalsIgnoreCase(classNameToCheck)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean shouldApply(RuleImplementation rule, IParsedNode item) {
		return !rule.getNames().getTextItem().isEmpty() || isStrict;
	}

}
