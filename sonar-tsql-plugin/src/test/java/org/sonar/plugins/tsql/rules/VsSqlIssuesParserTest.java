package org.sonar.plugins.tsql.rules;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.rules.parsers.VsSqlIssuesParser;

public class VsSqlIssuesParserTest {

	@Test
	public void testParser() {

		String file = this.getClass().getClassLoader().getResource("vsSample.xml").getFile();
		VsSqlIssuesParser parser = new VsSqlIssuesParser();

		TsqlIssue[] issues = parser.parse(new File(file));
		Assert.assertNotNull("Returned issues was null", issues);
		Assert.assertEquals("Expected a single issue", 1, issues.length);
		TsqlIssue issue = issues[0];
		Assert.assertEquals("Descpriptions did not match",
				"The shape of the result set produced by a SELECT * statement will change if the underlying table or view structure changes.",
				issue.getDescription());
		Assert.assertEquals("File path", "c:\\Database1\\Procedure1.sql", issue.getFilePath());
		Assert.assertEquals("Line did not match", 6, issue.getLine());
		Assert.assertEquals("Descpriptions did not match", "Microsoft.Rules.Data.SR0001", issue.getType());

	}

}
