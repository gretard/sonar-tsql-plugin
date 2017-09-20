package org.sonar.plugins.tsql.helpers;

import static org.junit.Assert.*;

import org.junit.Test;

public class Antlr4UtilsTest {

	@Test
	public void testGetFullString() {
		AntrlResult res= Antlr4Utils.getFull("CREATE PROC TEST as begin end");
		Antlr4Utils.print(res.getTree(), 0);
	}

}
