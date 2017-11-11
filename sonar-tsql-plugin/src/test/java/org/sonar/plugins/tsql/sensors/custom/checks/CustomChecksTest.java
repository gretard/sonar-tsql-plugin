package org.sonar.plugins.tsql.sensors.custom.checks;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.RuleMode;
import org.sonar.plugins.tsql.rules.custom.RuleResultType;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class CustomChecksTest {

	@Test
	public void testSargRule() {
		Rule r = Antlr4Utils.getSargRule();
		String s = "SELECT * from dbo.test where name like '%test%' ;";
		TsqlIssue[] issues = Antlr4Utils.verify2(r, s);
		Antlr4Utils.print(Antlr4Utils.get(s), 0);
		Assert.assertEquals(1, issues.length);

	}

	@Test
	public void testInsertIntoRule() {
		Rule r = Antlr4Utils.getInsertRule();
		String s = "INSERT INTO dbo.test VALUES (1,2);";
		TsqlIssue[] issues = Antlr4Utils.verify2(r, s);
		Antlr4Utils.print(Antlr4Utils.get(s), 0);
		Assert.assertEquals(1, issues.length);

	}

	@Test
	public void testUseRule() {
		Rule r = Antlr4Utils.getDeclareRule();
		String s = "DECLARE @Grohhup nvarchar(50) = 'tt'; DECLARE @Group2 nvarchar(50); DECLARE @Grou55 nvarchar(50); Set @Group2 = 'sss';";
		TsqlIssue[] issues = Antlr4Utils.verify2(r, s);
		Antlr4Utils.print(Antlr4Utils.get(s), 0);
		Assert.assertEquals(1, issues.length);

	}

	@Test
	public void testSargInCount() {
		Rule r = Antlr4Utils.getSargRule();
		String s = "if (SELECT count(name) from dbo.test) > 0";
		TsqlIssue[] issues = Antlr4Utils.verify2(r, s);
		Antlr4Utils.print(Antlr4Utils.get(s), 0);
		Assert.assertEquals(1, issues.length);

	}

}
