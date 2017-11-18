package org.sonar.plugins.tsql.sensors.custom.matchers;

import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

public interface IParentMatcher {
	boolean isMatch(RuleImplementation rule, IParsedNode parent, IParsedNode child);
}
