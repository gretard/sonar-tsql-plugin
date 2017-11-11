package org.sonar.plugins.tsql.sensors.custom.names;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.sensors.custom.IParsedNode;

public interface INamesChecker {
	boolean containsName(RuleImplementation rule, String text);
	boolean containsNames(RuleImplementation rule, IParsedNode node, IParsedNode parent);
	boolean containsClassName(RuleImplementation rule, String text);

	boolean containsClassName(RuleImplementation rule, ParseTree node);
	
	boolean containsClassName(RuleImplementation rule, IParsedNode node);

	boolean checkParent(IParsedNode node, IParsedNode root);

}
