package org.sonar.plugins.tsql.antlr.nodes.matchers;

import org.sonar.plugins.tsql.antlr.IParsedNode;
import org.sonar.plugins.tsql.checks.custom.RuleDistanceIndexMatchType;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;

public class DistanceMatcher extends ABaseMatcher {

	@Override
	protected boolean innerMatch(RuleImplementation rule, IParsedNode node) {

		int val = node.getDistance();
		if (rule.getDistanceCheckType() == RuleDistanceIndexMatchType.LESS) {
			if (val > rule.getDistance()) {
				return false;
			}
		}
		if (rule.getDistanceCheckType() == RuleDistanceIndexMatchType.MORE) {
			if (val < rule.getDistance()) {
				return false;
			}
		}
		if (rule.getDistanceCheckType() == RuleDistanceIndexMatchType.EQUALS) {
			if (rule.getDistance() != val) {
				return false;
			}
		}
		return true;

	}

	@Override
	protected boolean shouldApply(RuleImplementation rule, IParsedNode item) {
		return rule.getDistance() != 0;
	}

}
