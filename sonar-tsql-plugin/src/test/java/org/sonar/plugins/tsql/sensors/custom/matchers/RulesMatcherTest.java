package org.sonar.plugins.tsql.sensors.custom.matchers;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.TestNode;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.RuleMatchType;

public class RulesMatcherTest {

	private TestNode testNode = new TestNode("test", "testClass", 1);
	private TestNode testNode2 = new TestNode("test", "testClass", 1);

	@Test
	public void testNoMatch() {
		RulesMatcher matcher = new RulesMatcher();
		RuleImplementation rule = new RuleImplementation();
		Assert.assertFalse(matcher.match(rule, null, testNode));
	}

	@Test
	public void testMathchesClass() {
		RulesMatcher matcher = new RulesMatcher();
		RuleImplementation rule = new RuleImplementation();
		rule.getNames().getTextItem().add("testClass");
		Assert.assertTrue(matcher.match(rule, null, testNode));
	}

	@Test
	public void testNotMathchesClass() {
		RulesMatcher matcher = new RulesMatcher();
		RuleImplementation rule = new RuleImplementation();
		rule.getNames().getTextItem().add("testClass2");
		Assert.assertFalse(matcher.match(rule, null, testNode));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMatchesStrict() {
		RulesMatcher matcher = new RulesMatcher();
		RuleImplementation rule = new RuleImplementation();
		rule.setRuleMatchType(RuleMatchType.STRICT);
		rule.getNames().getTextItem().add("testClass2");
		Assert.assertFalse(matcher.match(rule, null, testNode));
	}

	@Test
	public void testMatchesStrict2() {
		RulesMatcher matcher = new RulesMatcher();
		RuleImplementation rule = new RuleImplementation();
		rule.setRuleMatchType(RuleMatchType.STRICT);
		rule.getNames().getTextItem().add("testClass2");
		TestNode parent = new TestNode("xxx", "ttt", 3);
		testNode.setParent(parent);
		testNode2.setParent(parent);
		Assert.assertFalse(matcher.match(rule, testNode2, testNode));
	}

	@Test
	public void testMatchesFullNotMatchTextDifferent() {
		RulesMatcher matcher = new RulesMatcher();
		RuleImplementation rule = new RuleImplementation();
		rule.setRuleMatchType(RuleMatchType.STRICT);
		rule.getNames().getTextItem().add("testClass2");
		TestNode parent = new TestNode("xxx", "ttt", 3);
		Assert.assertFalse(matcher.match(rule, testNode2, parent));
	}

	@Test
	public void testMatchesFullMatchTextSame() {
		RulesMatcher matcher = new RulesMatcher();
		RuleImplementation rule = new RuleImplementation();
		rule.setRuleMatchType(RuleMatchType.FULL);
		rule.getNames().getTextItem().add("testClass2");
		TestNode parent = new TestNode("test", "ttt", 3);
		Assert.assertFalse(matcher.match(rule, testNode2, parent));
	}
}
