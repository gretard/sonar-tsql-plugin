package org.sonar.plugins.tsql.antlr;

import org.antlr.tsql.TSqlParser.Comparison_operatorContext;
import org.antlr.tsql.TSqlParser.PredicateContext;
import org.antlr.tsql.TSqlParser.Primitive_expressionContext;
import org.antlr.tsql.TSqlParser.Select_statementContext;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.checks.CustomPluginChecks;
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
	public void testCheckEndingWithSemicolon() throws Throwable {
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
	public void testCheckEndingWIthSemicolon2() throws Throwable {
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

	@Test
	public void testIndexRule() throws Throwable {
		String s = "CREATE UNIQUE INDEX ix_test ON Persons (LastName, FirstName);";
		Rule sut = CustomPluginChecks.getIndexNamingRule();
		AntlrUtils.print(s);

		TsqlIssue[] issues = AntlrUtils.verify(sut, s);

		Assert.assertEquals(0, issues.length);
	}

	@Test
	public void testIndexRuleViolating() throws Throwable {
		String s = "CREATE UNIQUE INDEX test ON Persons (LastName, FirstName);";
		Rule sut = CustomPluginChecks.getIndexNamingRule();
		AntlrUtils.print(s);

		TsqlIssue[] issues = AntlrUtils.verify(sut, s);

		Assert.assertEquals(1, issues.length);
	}

	@Test
	public void testWhereOrRule() throws Throwable {
		String s = "select * from dbo.test where a = 4 or x = 5 OR f = 8 Or t = 0;";
		Rule sut = CustomPluginChecks.getWhereWithOrVsUnionRule();
		AntlrUtils.print(s);

		TsqlIssue[] issues = AntlrUtils.verify(sut, s);

		Assert.assertEquals(3, issues.length);
	}

	@Test
	public void testUnionVsUnionAllRule() throws Throwable {
		String s = "select * from dbo.test union select * from dbo.test2;";
		Rule sut = CustomPluginChecks.getUnionVsUnionALLRule();
		AntlrUtils.print(s);

		TsqlIssue[] issues = AntlrUtils.verify(sut, s);

		Assert.assertEquals(1, issues.length);
	}

	@Test
	public void testgetExistsVsInRule() throws Throwable {
		String s = "select * from dbo.test where name IN (select id from dbo.names);";
		Rule sut = CustomPluginChecks.getExistsVsInRule();
		AntlrUtils.print(s);

		TsqlIssue[] issues = AntlrUtils.verify(sut, s);

		Assert.assertEquals(1, issues.length);
	}

	@Test
	public void testgetExistsVsInRule2() throws Throwable {
		String s = "select * from dbo.test where name in (1, 2, 3);";
		Rule sut = CustomPluginChecks.getExistsVsInRule();
		AntlrUtils.print(s);

		TsqlIssue[] issues = AntlrUtils.verify(sut, s);

		Assert.assertEquals(0, issues.length);
	}

	@Test
	public void testOrderByRuleOrder() throws Throwable {
		String s = "select * from dbo.test order by name asc, surname;";
		Rule sut = CustomPluginChecks.getOrderByRuleWithoutAscDesc();
		AntlrUtils.print(s);

		TsqlIssue[] issues = AntlrUtils.verify(sut, s);

		Assert.assertEquals(1, issues.length);
	}

	@Test
	public void testNullNotNull() throws Throwable {
		Rule r = new Rule();
		r.setKey("Example1");
		r.setInternalKey("Example1");
		r.setDescription("Select statement should end with semicolon");
		r.setName("Select statement should end with semicolon");
		RuleImplementation rImpl = new RuleImplementation();
		rImpl.getNames().getTextItem().add(PredicateContext.class.getSimpleName());
		rImpl.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		r.setRuleImplementation(rImpl);
		RuleImplementation child = new RuleImplementation();
		child.setDistanceCheckType(RuleDistanceIndexMatchType.EQUALS);
		child.setIndexCheckType(RuleDistanceIndexMatchType.EQUALS);
		child.getTextToFind().getTextItem().add("!=");
		child.getTextToFind().getTextItem().add("<>");
		child.getTextToFind().getTextItem().add("=");
		child.setTextCheckType(TextCheckType.STRICT);
		child.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		child.setRuleResultType(RuleResultType.SKIP_IF_NOT_FOUND);
		child.getNames().getTextItem().add(Comparison_operatorContext.class.getSimpleName());

		RuleImplementation childNull = new RuleImplementation();

		childNull.getTextToFind().getTextItem().add("NULL");
		childNull.setTextCheckType(TextCheckType.STRICT);
		childNull.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		childNull.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		childNull.getNames().getTextItem().add(Primitive_expressionContext.class.getSimpleName());

		rImpl.getChildrenRules().getRuleImplementation().add(child);
		rImpl.getChildrenRules().getRuleImplementation().add(childNull);
		rImpl.getCompliantRulesCodeExamples().getRuleCodeExample().add("SELECT * from dbo.test where name IS NULL;");
		rImpl.getCompliantRulesCodeExamples().getRuleCodeExample()
				.add("SELECT * from dbo.test where name IS NOT NULL;");
		rImpl.getViolatingRulesCodeExamples().getRuleCodeExample().add("SELECT * from dbo.test where name = null");
		rImpl.getViolatingRulesCodeExamples().getRuleCodeExample().add("SELECT * from dbo.test where name != null");
		rImpl.getViolatingRulesCodeExamples().getRuleCodeExample().add("SELECT * from dbo.test where name <> null");
		String s = "SELECT * from dbo.test where (name = null) or name <> null or name != null or name is null";
		AntlrUtils.print(s);

		TsqlIssue[] issues = AntlrUtils.verify(r, s);

		Assert.assertEquals(3, issues.length);

	}

}
