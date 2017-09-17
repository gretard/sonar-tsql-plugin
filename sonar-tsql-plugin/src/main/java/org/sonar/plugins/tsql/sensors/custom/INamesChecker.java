package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;

public interface INamesChecker {
	boolean containsName(RuleImplementation rule, String text);

	boolean containsClassName(RuleImplementation rule, String text);

	boolean containsClassName(RuleImplementation rule, ParseTree node);
	
	boolean containsClassName(RuleImplementation rule, ParsedNode node);

	boolean checkParent(ParsedNode node, ParsedNode root);

}
