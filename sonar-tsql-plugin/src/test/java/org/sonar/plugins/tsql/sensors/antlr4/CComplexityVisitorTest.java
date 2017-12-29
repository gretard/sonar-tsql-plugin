package org.sonar.plugins.tsql.sensors.antlr4;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.helpers.AntrlResult;

public class CComplexityVisitorTest {

	@Test
	public void testComplex() throws IOException {
		String s = "IF DATENAME(weekday, GETDATE()) IN (N'Saturday', N'Sunday')       SELECT 'Weekend'; ELSE  "
				+ "  SELECT 'Weekday';";
		int result = calculate(s);
		Assert.assertEquals(2, result);
	}

	@Test
	public void testCase() throws IOException {
		String s = "SELECT      CASE         WHEN MIN(value) <= 0 THEN 0         WHEN MAX(1/value) >= 100 THEN 1 ELSE 4   END "
				+ "FROM testTable ;  ";
		int result = calculate(s);
		Assert.assertEquals(3, result);
	}
	@Test
	public void testSelect() throws IOException {
		String s = "SELECT      * from [dbo].[test] where a > 0 or b < 0 and x > 5;";
			
		int result = calculate(s);
		Assert.assertEquals(4, result);
	}

	@Test
	public void testReturn() throws IOException {
		String s = "CREATE PROCEDURE checkstate @param varchar(11)  AS  IF (SELECT StateProvince FROM Person.vAdditionalContactInfo WHERE ContactID = @param) = 'WA'      RETURN 1  ELSE  RETURN 2; GO  ";
		int result = calculate(s);
		Assert.assertEquals(3, result);
	}

	@Test
	public void testTryCatch() throws IOException {
		String s = "BEGIN TRY      SELECT 1/0;  END TRY  BEGIN CATCH  END CATCH;   ";
		int result = calculate(s);
		Assert.assertEquals(2, result);
	}

	@Test
	public void testWhile() throws IOException {
		String s = "WHILE @@FETCH_STATUS = 0     BEGIN        FETCH NEXT FROM Employee_Cursor;     END;  ";
		int result = calculate(s);
		Assert.assertEquals(2, result);
	}

	@Test
	public void testCase2() throws IOException {
		String s = "SELECT case when a > 0 then 'more' when a <0 then 'less' else '0' end from dbo.test";
		int result = calculate(s);
		Assert.assertEquals(3, result);
	}

	private int calculate(String s) {
		AntrlResult result = Antlr4Utils.getFull(s);
		CComplexityVisitor cc = new CComplexityVisitor();
		cc.visit(result.getTree());
		int r = cc.getComplexity();
		 Antlr4Utils.print(result.getTree(), 0);
		return r;
	}

}
