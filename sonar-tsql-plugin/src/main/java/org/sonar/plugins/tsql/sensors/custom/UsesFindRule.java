package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;

public class UsesFindRule{

	INamesChecker checker = new DefaultNamesChecker();
	public boolean root(final ParsedNode item, final ParseTree candidate,final  Rule rule) {
		String text = item.getName();
		if (text==null) {
			text = item.getText();
		}
		final RuleImplementation impl = rule.getRuleImplementation();
		if (impl == null || candidate == null || item.getItem() == candidate ) {
			return false;
		}
		
		boolean any = false;
		for (RuleImplementation c : impl.getUsesRules().getRuleImplementation()) {
			if (candidate.getText().contains(text) && checker.containsClassName(c, candidate)) {
				any = true;
			}
		}
		if (any) {
			item.getUses().add(new ParsedNode(text, candidate, candidate.getClass().getSimpleName(), rule,
					item.getRepository()));
			return true;
		}

		
return false;
	}

}
