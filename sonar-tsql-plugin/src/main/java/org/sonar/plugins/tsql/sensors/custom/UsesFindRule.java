package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;

public class UsesFindRule{

	
	public boolean root(final ParsedNode item, final ParseTree candidate,final  Rule rule) {
		String text = item.getName();
		if (text==null) {
			text = item.getText();
		}
		final RuleImplementation impl = rule.getRuleImplementation();
		if (impl == null || candidate == null || item.getItem() == candidate 
				|| !candidate.getText().contains(text)) {
			return false;
		}

		item.getUses().add(new ParsedNode(text, candidate, candidate.getClass().getSimpleName(), rule,
				item.getRepository()));
		return true;

	}

}
