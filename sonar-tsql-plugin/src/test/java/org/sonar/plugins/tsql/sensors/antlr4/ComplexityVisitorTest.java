package org.sonar.plugins.tsql.sensors.antlr4;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.antlr.AntlrContext;
import org.sonar.plugins.tsql.antlr.visitors.ComplexityVisitor;
import org.sonar.plugins.tsql.antlr.visitors.CustomTreeVisitor;
import org.sonar.plugins.tsql.helpers.AntlrUtils;

public class ComplexityVisitorTest {

	@Test
	public void testComplex() throws Throwable {
		String s = "SELECT SalesOrderID, SUM(LineTotal) AS SubTotal "
				+ "FROM Sales.SalesOrderDetail  left join dbo.x on t1.id = t2.id where a > 5"
				+ "GROUP BY SalesOrderID  " + "HAVING SUM(LineTotal) > 100000.00  " + "ORDER BY SalesOrderID, test ";
		int result = calculate(s);
		Assert.assertEquals(11, result);
	}

	@Test
	public void testSelect() throws Throwable {
		String s = "SELECT SalesOrderID, test, * from dbo.test";
		int result = calculate(s);
		Assert.assertEquals(4, result);
	}

	@Test
	public void testWhere() throws Throwable {
		String s = "SELECT SalesOrderID, test, * from dbo.test where id > 1 and id2 < 9";
		int result = calculate(s);
		Assert.assertEquals(6, result);
	}

	@Test
	public void testUnion() throws Throwable {
		String s = "SELECT a, b from dbo.test union select c,d from dbo.test2";
		int result = calculate(s);
		Assert.assertEquals(6, result);
	}

	@Test
	public void testFunction() throws Throwable {
		String s = "SELECT count(a) from dbo.test";
		int result = calculate(s);
		Assert.assertEquals(3, result);
	}

	@Test
	public void testCase() throws Throwable {
		String s = "SELECT case when a > 0 then 'more' when a <0 then 'less' else '0' end from dbo.test";
		int result = calculate(s);
		Assert.assertEquals(4, result);
	}

	private int calculate(String s) throws Throwable {

		AntlrContext result = AntlrUtils.getRequest(s);
		ComplexityVisitor vv = new ComplexityVisitor();
		CustomTreeVisitor visitor = new CustomTreeVisitor(vv);
		visitor.visit(result.getRoot());
		return vv.getMeasure();

	}

}
