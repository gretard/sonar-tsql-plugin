package org.sonar.plugins.tsql.antlr;

import org.antlr.tsql.TSqlParser.Select_statementContext;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.RuleDistanceIndexMatchType;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.checks.custom.RuleMatchType;
import org.sonar.plugins.tsql.checks.custom.RuleResultType;
import org.sonar.plugins.tsql.checks.custom.TextCheckType;
import org.sonar.plugins.tsql.helpers.AntlrUtils;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class CustomChecksTest {

	@Test
	public void testCheckEndingWithSemicolon() {
		Rule r = new Rule();
		r.setKey("Example1");
		r.setInternalKey("Example1");
		r.setDescription("Select statement should end with semicolon");
		r.setName("Select statement should end with semicolon");
		RuleImplementation rImpl = new RuleImplementation();
		rImpl.getNames().getTextItem().add(Select_statementContext.class.getSimpleName());
		rImpl.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		r.setRuleImplementation(rImpl);
		RuleImplementation child = new RuleImplementation();
		child.setDistance(1);
		child.setIndex(-1);
		child.setDistanceCheckType(RuleDistanceIndexMatchType.EQUALS);
		child.setIndexCheckType(RuleDistanceIndexMatchType.EQUALS);
		child.getTextToFind().getTextItem().add(";");
		child.setTextCheckType(TextCheckType.STRICT);
		child.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		child.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		child.getNames().getTextItem().add(TerminalNodeImpl.class.getSimpleName());
		rImpl.getChildrenRules().getRuleImplementation().add(child);
		rImpl.getCompliantRulesCodeExamples().getRuleCodeExample()
				.add("SELECT * from dbo.test where name like '%test%';");
		rImpl.getViolatingRulesCodeExamples().getRuleCodeExample()
				.add("SELECT * from dbo.test where name like '%test%'");

		String s = "SELECT * from dbo.test where name like '%test%';";
		TsqlIssue[] issues = AntlrUtils.verify(r, s);

		Assert.assertEquals(0, issues.length);

	}

	@Test
	public void testCheckEndingWIthSemicolon2() {
		Rule r = new Rule();
		RuleImplementation rImpl = new RuleImplementation();
		rImpl.getNames().getTextItem().add(Select_statementContext.class.getSimpleName());
		rImpl.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		r.setRuleImplementation(rImpl);
		RuleImplementation child = new RuleImplementation();
		child.setDistance(1);
		child.setIndex(-1);
		child.setDistanceCheckType(RuleDistanceIndexMatchType.EQUALS);
		child.setIndexCheckType(RuleDistanceIndexMatchType.EQUALS);
		child.getTextToFind().getTextItem().add(";");
		child.setTextCheckType(TextCheckType.STRICT);
		child.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		child.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		child.getNames().getTextItem().add(TerminalNodeImpl.class.getSimpleName());
		rImpl.getChildrenRules().getRuleImplementation().add(child);
		String s = "SELECT * from dbo.test where name like '%test%'";
		TsqlIssue[] issues = AntlrUtils.verify(r, s);
		Assert.assertEquals(1, issues.length);

	}

}
