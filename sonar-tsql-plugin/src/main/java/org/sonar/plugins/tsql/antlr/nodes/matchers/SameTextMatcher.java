package org.sonar.plugins.tsql.antlr.nodes.matchers;

import org.apache.commons.lang3.StringUtils;
import org.sonar.plugins.tsql.antlr.IParsedNode;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;

public class SameTextMatcher implements IParentMatcher {

	@Override
	public boolean isMatch(RuleImplementation rule, IParsedNode parent, IParsedNode child) {
		String text1 = child.getText();
		String text2 = parent.getText();

		return StringUtils.contains(text1, text2) || StringUtils.contains(text2, text1);

	}

}
