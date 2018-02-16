package org.sonar.plugins.tsql.sensors.antlr4;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.antlr.FillerRequest;
import org.sonar.plugins.tsql.antlr.visitors.ComplexityVisitor;
import org.sonar.plugins.tsql.antlr.visitors.CustomTreeVisitor;
import org.sonar.plugins.tsql.helpers.AntlrUtils;

public class ComplexityVisitorTest {

	@Test
	public void testComplex() throws IOException {
		String s = "SELECT SalesOrderID, SUM(LineTotal) AS SubTotal "
				+ "FROM Sales.SalesOrderDetail  left join dbo.x on t1.id = t2.id where a > 5"
				+ "GROUP BY SalesOrderID  " + "HAVING SUM(LineTotal) > 100000.00  " + "ORDER BY SalesOrderID, test ";
		int result = calculate(s);
		Assert.assertEquals(11, result);
	}

	@Test
	public void testSelect() throws IOException {
		String s = "SELECT SalesOrderID, test, * from dbo.test";
		int result = calculate(s);
		Assert.assertEquals(4, result);
	}

	@Test
	public void testWhere() throws IOException {
		String s = "SELECT SalesOrderID, test, * from dbo.test where id > 1 and id2 < 9";
		int result = calculate(s);
		Assert.assertEquals(6, result);
	}

	@Test
	public void testUnion() throws IOException {
		String s = "SELECT a, b from dbo.test union select c,d from dbo.test2";
		int result = calculate(s);
		Assert.assertEquals(6, result);
	}

	@Test
	public void testFunction() throws IOException {
		String s = "SELECT count(a) from dbo.test";
		int result = calculate(s);
		Assert.assertEquals(3, result);
	}

	@Test
	public void testCase() throws IOException {
		String s = "SELECT case when a > 0 then 'more' when a <0 then 'less' else '0' end from dbo.test";
		int result = calculate(s);
		Assert.assertEquals(4, result);
	}

	private int calculate(String s) {

		FillerRequest result = AntlrUtils.getRequest(s);
		ComplexityVisitor vv = new ComplexityVisitor();
		CustomTreeVisitor visitor = new CustomTreeVisitor(vv);
		visitor.visit(result.getRoot());
		//AntlrUtils.print(result.getRoot(), 0, result.getStream());
		return vv.getMeasure();

	}

}
