package org.sonar.plugins.tsql.helpers;

import static org.junit.Assert.*;

import org.junit.Test;

public class Antlr4UtilsTest {

	@Test
	public void testGetFullString() {
		AntrlResult res= Antlr4Utils.getFull("CREATE PROC TEST as begin end");
		Antlr4Utils.print(res.getTree(), 0);
	}
	@Test
	public void testGetFullString2() {
		AntrlResult res= Antlr4Utils.getFull("SELECT * from test WHERE Year(myDate) = 2008 or myDate between '2012-01-01' and '2019-10-10'");
		Antlr4Utils.print(res.getTree(), 0);
	}
	
	@Test
	public void testGetFullString3() {
		AntrlResult res= Antlr4Utils.getFull("SELECT * from test WITH (nolock)");
		Antlr4Utils.print(res.getTree(), 0);
	}
}
