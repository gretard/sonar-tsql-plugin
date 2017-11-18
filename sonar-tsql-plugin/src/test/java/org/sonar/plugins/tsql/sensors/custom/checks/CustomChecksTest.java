package org.sonar.plugins.tsql.sensors.custom.checks;

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
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class CustomChecksTest {

	@Test
	public void testSargRule() {
		Rule r = Antlr4Utils.getSargRule();
		String s = "select * from dbo.test where name in (SELECT name from dbo.test where name like '%test%' or name2 like 'test5%' or year(name) > 2008);";
		TsqlIssue[] issues = Antlr4Utils.verify2(r, s);
		Assert.assertEquals(2, issues.length);

	}

	@Test
	public void testExecRule() {
		Rule r = Antlr4Utils.getExecRule();
		String s = "EXEC ('SELECT 1');";
		// Antlr4Utils.print(Antlr4Utils.get(s), 0);
		TsqlIssue[] issues = Antlr4Utils.verify2(r, s);
		Assert.assertEquals(1, issues.length);

	}

	@Test
	public void testPrimarYkey() {
		Rule r = Antlr4Utils.getPKRule();
		String s2 = "CREATE TABLE Production.TransactionHistoryArchive1  "
				+ "(     TransactionID int NOT NULL,     CONSTRAINT PaK_TransactionHistoryArchive_TransactionID PRIMARY KEY CLUSTERED (TransactionID)  "
				+ ");  ";
		String s = "ALTER TABLE Production.TransactionHistoryArchive  ADD CONSTRAINT PaK_TransactionHistoryArchive_TransactionID PRIMARY KEY CLUSTERED (TransactionID);  ";
	//	Antlr4Utils.print(Antlr4Utils.get(s), 0);
		TsqlIssue[] issues = Antlr4Utils.verify2(r, s);
		Assert.assertEquals(1, issues.length);

	}

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
		rImpl.getCompliantRulesCodeExamples().getRuleCodeExample().add("SELECT * from dbo.test where name like '%test%';");
		rImpl.getViolatingRulesCodeExamples().getRuleCodeExample().add("SELECT * from dbo.test where name like '%test%'");

		String s = "SELECT * from dbo.test where name like '%test%';";
		//System.out.println(Antlr4Utils.ruleImplToString(r));
		TsqlIssue[] issues = Antlr4Utils.verify2(r, s);
		
		//TsqlIssue[] issues = Antlr4Utils.verifyWithPrinting(r, s);
		Assert.assertEquals(0, issues.length);
	//	Antlr4Utils.print(Antlr4Utils.get("SELECT * from dbo.test;"), 0);

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
		TsqlIssue[] issues = Antlr4Utils.verify2(r, s);
		Assert.assertEquals(1, issues.length);

	}

	@Test
	public void testInsertIntoRule() {
		Rule r = Antlr4Utils.getInsertRule();
		String s = "INSERT INTO dbo.test VALUES (1,2);";
		TsqlIssue[] issues = Antlr4Utils.verify2(r, s);
		Assert.assertEquals(1, issues.length);

	}

	@Test
	public void testUseRule() {
		Rule r = Antlr4Utils.getDeclareRule();
		String s = "DECLARE @group1 nvarchar(50) = 'tt'; DECLARE @group2 nvarchar(50); DECLARE @group3 nvarchar(50); Set @Group2 = 'sss';";
		TsqlIssue[] issues = Antlr4Utils.verify2(r, s);
		Assert.assertEquals(1, issues.length);

	}

}
