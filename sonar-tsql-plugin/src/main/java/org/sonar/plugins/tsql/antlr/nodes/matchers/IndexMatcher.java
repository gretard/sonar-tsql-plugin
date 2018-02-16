package org.sonar.plugins.tsql.antlr.nodes.matchers;

import org.sonar.plugins.tsql.antlr.nodes.IParsedNode;
import org.sonar.plugins.tsql.checks.custom.RuleDistanceIndexMatchType;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;

public class IndexMatcher extends ABaseMatcher {

	@Override
	protected boolean innerMatch(RuleImplementation rule, IParsedNode node) {
		if (rule.getIndex() > 0) {
			int val = node.getIndex();
			if (rule.getIndexCheckType() == RuleDistanceIndexMatchType.LESS) {
				if (val > rule.getIndex()) {
					return false;
				}
			}
			if (rule.getIndexCheckType() == RuleDistanceIndexMatchType.EQUALS) {
				if (val != rule.getIndex()) {
					return false;
				}
			}
			if (rule.getIndexCheckType() == RuleDistanceIndexMatchType.MORE) {
				if (rule.getIndex() < val) {
					return false;
				}
			}

		}
		if (rule.getIndex() < 0) {
			int val = node.getIndex2();
			if (rule.getIndexCheckType() == RuleDistanceIndexMatchType.LESS) {
				if (val > rule.getIndex()) {
					return false;
				}
			}
			if (rule.getIndexCheckType() == RuleDistanceIndexMatchType.EQUALS) {
				if (val != rule.getIndex()) {
					return false;
				}
			}
			if (rule.getIndexCheckType() == RuleDistanceIndexMatchType.MORE) {
				if (rule.getIndex() < val) {
					return false;
				}
			}

		}
		return true;

	}

	@Override
	protected boolean shouldApply(RuleImplementation rule, IParsedNode item) {
		return rule.getIndex() != 0;
	}

}
