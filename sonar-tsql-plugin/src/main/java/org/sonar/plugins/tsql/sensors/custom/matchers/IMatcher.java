package org.sonar.plugins.tsql.sensors.custom.matchers;

import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

public interface IMatcher {
	boolean match(final RuleImplementation rule, final IParsedNode item);
}
