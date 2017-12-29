package org.sonar.plugins.tsql.antlr.nodes.matchers;

import org.sonar.plugins.tsql.antlr.nodes.IParsedNode;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;

public class NodeNameAndOrClassMatcher implements IMatcher {

	private final IMatcher classMatcher = new ClassNameMatcher();
	private final IMatcher textMatcher = new TextNameMatcher();

	@Override
	public boolean match(RuleImplementation rule, IParsedNode item) {
		return this.classMatcher.match(rule, item) && this.textMatcher.match(rule, item);
	}
}
