package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;

public class UsesFindRule{

	
	public boolean root(final ParsedNode item, final ParseTree candidate,final  Rule rule) {

		final RuleImplementation impl = rule.getRuleImplementation();
		if (impl == null || candidate == null || item.getItem() == candidate 
				|| !candidate.getText().contains(item.getName())) {
			return false;
		}

		item.getUses().add(new ParsedNode(item.getName(), candidate, candidate.getClass().getSimpleName(), rule,
				item.getRepository()));
		return true;

	}

}
