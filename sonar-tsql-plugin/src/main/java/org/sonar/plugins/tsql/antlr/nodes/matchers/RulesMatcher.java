package org.sonar.plugins.tsql.antlr.nodes.matchers;

import org.sonar.plugins.tsql.antlr.nodes.IParsedNode;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.checks.custom.RuleMatchType;

public class RulesMatcher {

	private final IMatcher[] matchers = new IMatcher[] { new DistanceMatcher(), new IndexMatcher() };
	private final IMatcher classNamesMatcher = new ClassNameMatcher();
	private final IMatcher textMatcher = new TextNameMatcher();

	private final IParentMatcher sameParentsMatcher = new ParentsMatcher();
	private final IParentMatcher sameTextMatcher = new SameTextMatcher();

	public boolean match(RuleImplementation rule, IParsedNode parent, IParsedNode node) {

		for (final IMatcher matcher : this.matchers) {
			if (!matcher.match(rule, node)) {
				return false;
			}
		}

		final RuleMatchType type = rule.getRuleMatchType();
		
		boolean classMatch = classNamesMatcher.match(rule, node);
		boolean textMatch = textMatcher.match(rule, node);
		switch (type) {
		case DEFAULT:
		case CLASS_ONLY:
			return classMatch;
		case TEXT_AND_CLASS:
			return classMatch && textMatch;
		case TEXT_ONLY:
			return textMatch;
		case FULL:
			if (parent == null) {
				throw new IllegalArgumentException("Can't do full check as parent is null");
			}
			boolean sameText = sameTextMatcher.isMatch(rule, parent, node);
			return textMatch && classMatch && sameText;
		case STRICT:
			if (parent == null) {
				throw new IllegalArgumentException("Can't do strict check as parent is null");
			}
			return textMatch && classMatch && sameTextMatcher.isMatch(rule, parent, node)
					&& sameParentsMatcher.isMatch(rule, node, parent);
		default:
			break;
		}
		return false;
	}
}
