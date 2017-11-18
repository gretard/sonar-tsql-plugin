package org.sonar.plugins.tsql.sensors.custom.matchers;

import org.sonar.plugins.tsql.checks.custom.RuleDistanceIndexMatchType;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

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
