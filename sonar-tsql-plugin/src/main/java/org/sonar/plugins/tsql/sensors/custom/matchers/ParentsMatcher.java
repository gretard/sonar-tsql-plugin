package org.sonar.plugins.tsql.sensors.custom.matchers;

import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

public class ParentsMatcher implements IParentMatcher {

	@Override
	public boolean isMatch(RuleImplementation rule, IParsedNode parent, IParsedNode child) {
		IParsedNode parent1 = parent.getControlFlowParent();
		IParsedNode parent2 = child.getControlFlowParent();

		if (parent1 == null || parent2 == null) {
			return false;
		}
		return parent.getControlFlowParent().getItem() == child.getControlFlowParent().getItem();

	}

}
