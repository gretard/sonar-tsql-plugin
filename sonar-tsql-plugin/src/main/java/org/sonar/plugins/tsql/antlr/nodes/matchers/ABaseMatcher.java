package org.sonar.plugins.tsql.antlr.nodes.matchers;

import org.sonar.plugins.tsql.antlr.IParsedNode;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;

public abstract class ABaseMatcher implements IMatcher {

	protected abstract boolean innerMatch(RuleImplementation rule, IParsedNode item);

	protected abstract boolean shouldApply(RuleImplementation rule, IParsedNode item);

	@Override
	public boolean match(RuleImplementation rule, IParsedNode item) {
		if (rule == null || item == null) {
			return false;
		}
		if (shouldApply(rule, item)) {
			return innerMatch(rule, item);
		}

		return true;
	}

}
