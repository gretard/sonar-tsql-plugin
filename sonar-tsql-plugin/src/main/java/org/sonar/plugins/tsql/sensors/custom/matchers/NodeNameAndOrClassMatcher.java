package org.sonar.plugins.tsql.sensors.custom.matchers;

import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

public class NodeNameAndOrClassMatcher implements IMatcher {

	private final IMatcher classMatcher = new ClassNameMatcher();
	private final IMatcher textMatcher = new TextNameMatcher();

	@Override
	public boolean match(RuleImplementation rule, IParsedNode item) {
		return this.classMatcher.match(rule, item) && this.textMatcher.match(rule, item);
	}
}
