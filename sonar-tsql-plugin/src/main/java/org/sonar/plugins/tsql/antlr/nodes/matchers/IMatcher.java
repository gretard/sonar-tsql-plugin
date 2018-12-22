package org.sonar.plugins.tsql.antlr.nodes.matchers;

import org.sonar.plugins.tsql.antlr.IParsedNode;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;

public interface IMatcher {
	boolean match(final RuleImplementation rule, final IParsedNode item);
}
