package org.sonar.plugins.tsql.rules;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.rules.parsers.SqlCodeGuardIssuesParser;

public class CgSqlIssuesParserTest {

	@Test
	public void testParser() {

		String file = this.getClass().getClassLoader().getResource("cgSample.xml").getFile();
		SqlCodeGuardIssuesParser parser = new SqlCodeGuardIssuesParser();

		TsqlIssue[] issues = parser.parse(new File(file));
		Assert.assertNotNull("Returned issues was null", issues);
		Assert.assertEquals("Expected 1 issues", 1, issues.length);
		TsqlIssue issue = issues[0];
		Assert.assertEquals("Descpriptions did not match",
				"Script should end with GO",
				issue.getDescription());
		Assert.assertEquals("File path", "C:\\TestTable.sql", issue.getFilePath());
		Assert.assertEquals("Line did not match", 6, issue.getLine());
		Assert.assertEquals("Descpriptions did not match", "SC001", issue.getType());
	

	}

}
