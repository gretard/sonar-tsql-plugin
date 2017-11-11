package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.TextCheckType;
import org.sonar.plugins.tsql.sensors.custom.names.DefaultNamesChecker;

public class DefaultNamesCheckerTest {

	@Test
	public void testClassMatch() {
		ParseTree node = Antlr4Utils.get("select * from test");
		RuleImplementation rule = new RuleImplementation();
		rule.getNames().getTextItem().add("Tsql_fileContext");
		DefaultNamesChecker namesChecker = new DefaultNamesChecker();
		Assert.assertTrue(namesChecker.containsClassName(rule, node));
	}

	@Test
	public void testClassMatchFalse() {
		ParseTree node = Antlr4Utils.get("select * from test");
		RuleImplementation rule = new RuleImplementation();
		rule.getNames().getTextItem().add("2Tsql_fileContext");
		DefaultNamesChecker namesChecker = new DefaultNamesChecker();
		Assert.assertFalse(namesChecker.containsClassName(rule, node));
	}

	@Test
	public void testClassMatchRuleClassNameNull() {
		ParseTree node = Antlr4Utils.get("select * from test");
		RuleImplementation rule = new RuleImplementation();
		DefaultNamesChecker namesChecker = new DefaultNamesChecker();
		Assert.assertFalse(namesChecker.containsClassName(rule, node));
	}

	@Test
	public void testContainsNameEmptyList() {
		RuleImplementation rule = new RuleImplementation();
		DefaultNamesChecker namesChecker = new DefaultNamesChecker();
	//	Assert.assertFalse(namesChecker.containsName(rule, "a"));
	}

	@Test
	public void testContainsNameContains() {
		RuleImplementation rule = new RuleImplementation();
		rule.getTextToFind().getTextItem().add("a");
		rule.setTextCheckType(TextCheckType.CONTAINS);
		DefaultNamesChecker namesChecker = new DefaultNamesChecker();
		Assert.assertTrue(namesChecker.containsName(rule, "aa"));
	}

	@Test
	public void testContainsNameStrictFalse() {
		RuleImplementation rule = new RuleImplementation();
		rule.getTextToFind().getTextItem().add("a");
		rule.setTextCheckType(TextCheckType.STRICT);
		DefaultNamesChecker namesChecker = new DefaultNamesChecker();
		Assert.assertFalse(namesChecker.containsName(rule, "aa"));
	}

	@Test
	public void testContainsNameStrictTrue() {
		RuleImplementation rule = new RuleImplementation();
		rule.getTextToFind().getTextItem().add("aa");
		rule.getTextToFind().getTextItem().add("bb");
		rule.setTextCheckType(TextCheckType.STRICT);
		DefaultNamesChecker namesChecker = new DefaultNamesChecker();
		Assert.assertTrue(namesChecker.containsName(rule, "aa"));
	}

	@Test
	public void testContainsNameRegexTrue() {
		RuleImplementation rule = new RuleImplementation();
		rule.getTextToFind().getTextItem().add("aa");
		rule.getTextToFind().getTextItem().add("bb");
		rule.setTextCheckType(TextCheckType.REGEXP);
		DefaultNamesChecker namesChecker = new DefaultNamesChecker();
		Assert.assertTrue(namesChecker.containsName(rule, "aa"));
	}

	@Test
	public void testContainsNameRegexTrue2() {
		RuleImplementation rule = new RuleImplementation();
		rule.getTextToFind().getTextItem().add("a+");
		rule.getTextToFind().getTextItem().add("bb");
		rule.setTextCheckType(TextCheckType.REGEXP);
		DefaultNamesChecker namesChecker = new DefaultNamesChecker();
		Assert.assertTrue(namesChecker.containsName(rule, "aa"));
	}

	@Test
	public void testContainsNameRegexFalse() {
		RuleImplementation rule = new RuleImplementation();
		rule.getTextToFind().getTextItem().add("a+o");
		rule.getTextToFind().getTextItem().add("bb");
		rule.setTextCheckType(TextCheckType.REGEXP);
		DefaultNamesChecker namesChecker = new DefaultNamesChecker();
		Assert.assertFalse(namesChecker.containsName(rule, "aa"));
	}

}
