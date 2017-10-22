package org.sonar.plugins.tsql.sensors.custom;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.helpers.AntrlResult;
import org.sonar.plugins.tsql.rules.custom.SqlRules;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleResultType;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class CustomRulesViolationsProviderTest {

	@Test
	public void testGetIssuesInsertTrue() {

		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("test");
		customRules.getRule().add(Antlr4Utils.getInsertRule());

		AntrlResult result = Antlr4Utils.getFull("insert into dbo.test values (1,2), (2,3)");
		CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream(), customRules);
		ParseTree root = result.getTree();
		TsqlIssue[] issues = provider.getIssues(root);

		Assert.assertEquals(1, issues.length);
	}

	@Test
	public void testGetIssuesInsertSkipRule() {

		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("test");
		Rule rule = Antlr4Utils.getInsertRule();
		rule.getRuleImplementation().getChildrenRules().getRuleImplementation().get(0)
				.setRuleResultType(RuleResultType.SKIP_IF_FOUND);
		customRules.getRule().add(rule);

		AntrlResult result = Antlr4Utils.getFull("insert into dbo.test values (1,2), (2,3)");
		CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream(), customRules);
		ParseTree root = result.getTree();
		TsqlIssue[] issues = provider.getIssues(root);

		Assert.assertEquals(0, issues.length);
	}

	@Test
	public void testGetIssuesInsertFalse() {

		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("test");
		customRules.getRule().add(Antlr4Utils.getInsertRule());

		AntrlResult result = Antlr4Utils.getFull("insert into dbo.test (a,v) values (1,2), (2,3)");
		CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream(),customRules);
		ParseTree root = result.getTree();
		TsqlIssue[] issues = provider.getIssues(root);

		Assert.assertEquals(0, issues.length);
	}

	@Test
	public void testGetOrderBy() {

		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("test");
		customRules.getRule().add(Antlr4Utils.getOrderByRule());

		AntrlResult result = Antlr4Utils.getFull("select * from dbo.test order by 1");
		CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream(), customRules);
		ParseTree root = result.getTree();
		TsqlIssue[] issues = provider.getIssues(root);

		Assert.assertEquals(1, issues.length);
	}
	@Test
	public void testExecRule() {

		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("test");
		customRules.getRule().add(Antlr4Utils.getExecRule());

		AntrlResult result = Antlr4Utils.getFull("exec ('select 1'); EXEC Sales.usp_GetSalesYTD;");
		CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream(), customRules);
		ParseTree root = result.getTree();
		TsqlIssue[] issues = provider.getIssues(root);

		Assert.assertEquals(1, issues.length);
	}
	@Test
	public void testGetOrderByFalse() {
		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("test");
		customRules.getRule().add(Antlr4Utils.getOrderByRule());

		AntrlResult result = Antlr4Utils.getFull("select * from dbo.test order by test asc");
		CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream(), customRules);
		ParseTree root = result.getTree();
		TsqlIssue[] issues = provider.getIssues(root);
		Assert.assertEquals(0, issues.length);
	}

	@Test
	public void testGetMultipleDeclarations() {
		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("test");
		customRules.getRule().add(Antlr4Utils.getMultipleDeclarations());

		AntrlResult result = Antlr4Utils.getFull(
				"DECLARE vendor_cursor CURSOR FOR SELECT Vendor ID, Name  FROM Purchasing.Vendor  WHERE PreferredVendorStatus = 1  ORDER BY VendorID;DECLARE vendor_cursor CURSOR FOR SELECT Vendor ID, Name  FROM Purchasing.Vendor  WHERE PreferredVendorStatus = 1  ORDER BY VendorID;");
		CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream(), customRules);
		ParseTree root = result.getTree();
		TsqlIssue[] issues = provider.getIssues(root);
		Assert.assertEquals(0, issues.length);
	}

	@Test
	public void testGetMultipleDeclarationsFalse() {
		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("test");
		customRules.getRule().add(Antlr4Utils.getMultipleDeclarations());

		AntrlResult result = Antlr4Utils.getFull(
				"DECLARE vendor_cursor CURSOR FOR SELECT Vendor ID, Name  FROM Purchasing.Vendor  WHERE PreferredVendorStatus = 1  ORDER BY VendorID;");
		CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream(), customRules);
		ParseTree root = result.getTree();
		TsqlIssue[] issues = provider.getIssues(root);
		Assert.assertEquals(0, issues.length);
	}

	@Test
	public void testGetSameParent() {
		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("test");
		customRules.getRule().add(Antlr4Utils.getSameFlow());

		AntrlResult result = Antlr4Utils.getFull(
				"DECLARE vendor_cursor CURSOR FOR SELECT Vendor ID, Name  FROM Purchasing.Vendor; BEGIN  CLOSE vendor_cursor; END;  ");
		CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream(), customRules);
		ParseTree root = result.getTree();
		TsqlIssue[] issues = provider.getIssues(root);
		Assert.assertEquals(2, issues.length);
	}
	
	@Test
	public void testGetSameParent2() throws IOException {
		final InputStream stream = this.getClass().getResourceAsStream("/testFiles/cursorFlow.sql");

		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("test");
		customRules.getRule().add(Antlr4Utils.getSameFlow());

		AntrlResult result = Antlr4Utils.getFull(				stream);
		CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream(), customRules);
		ParseTree root = result.getTree();
		TsqlIssue[] issues = provider.getIssues(root);
		Assert.assertEquals(4, issues.length);
	}
}
