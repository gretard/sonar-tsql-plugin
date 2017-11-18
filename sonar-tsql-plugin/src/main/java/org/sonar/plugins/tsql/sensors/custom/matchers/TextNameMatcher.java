package org.sonar.plugins.tsql.sensors.custom.matchers;

import org.apache.commons.lang3.StringUtils;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.checks.custom.TextCheckType;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

public class TextNameMatcher extends ABaseMatcher {
	private final boolean isStrict;

	public TextNameMatcher(boolean isStrict) {
		this.isStrict = isStrict;
	}

	public TextNameMatcher() {
		this(false);
	}

	@Override
	protected boolean innerMatch(RuleImplementation rule, IParsedNode item) {
		final TextCheckType type = rule.getTextCheckType();

		final String text = item.getText();

		for (final String searchItem : rule.getTextToFind().getTextItem()) {
			switch (type) {
			case DEFAULT:
			case CONTAINS:
				if (StringUtils.containsIgnoreCase(text, searchItem)) {
					return true;
				}
				break;

			case REGEXP:
				if (text.matches(searchItem)) {
					return true;
				}
				break;
			case STRICT:
				if (text.equalsIgnoreCase(searchItem)) {
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
	protected boolean shouldApply(RuleImplementation rule, IParsedNode item) {
		return !rule.getTextToFind().getTextItem().isEmpty() || isStrict;
	}

}
