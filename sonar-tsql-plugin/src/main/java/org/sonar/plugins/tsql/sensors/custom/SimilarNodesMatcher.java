package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.sensors.custom.names.DefaultNamesChecker;
import org.sonar.plugins.tsql.sensors.custom.names.INamesChecker;

public class SimilarNodesMatcher {

	final INamesChecker checker = new DefaultNamesChecker();

	public boolean isMatch(final ParsedNode item, final ParseTree candidate, final Rule rule) {
		
		final RuleImplementation impl = rule.getRuleImplementation();
		if (impl == null || candidate == null || item.getItem() == candidate) {
			return false;
		}
		String text = item.getName();
		if (text == null) {
			text = item.getText();
		}
		final String candidateText = candidate.getText();
		if (!candidateText.contains(text)) {
			return false;
		}
		final RuleImplementation[] rules = impl.getUsesRules().getRuleImplementation()
				.toArray(new RuleImplementation[0]);
		for (final RuleImplementation c : rules) {
			if (checker.containsClassName(c, candidate)) {
				item.getUses().add(new ParsedNode(candidate, rule, item.getRepository()));
				return true;
			}
		}

		return false;
	}

}
