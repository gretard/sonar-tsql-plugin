package org.sonar.plugins.tsql.antlr.nodes.matchers;

import org.sonar.plugins.tsql.antlr.nodes.IParsedNode;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;

public interface IParentMatcher {
	boolean isMatch(RuleImplementation rule, IParsedNode parent, IParsedNode child);
}
