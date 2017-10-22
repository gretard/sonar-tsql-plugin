package org.sonar.plugins.tsql.sensors.custom;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.TestNode;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.RuleMatchType;
import org.sonar.plugins.tsql.rules.custom.TextCheckType;

public class RulesMatcherTest {

	@Test
	public void testClassMatchesRuleFalse() {
		IParsedNode node = new TestNode("testText", "testClassName", 2);
		RuleImplementation rule = new RuleImplementation();
		rule.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		RulesMatcher matcher = new RulesMatcher();
		Assert.assertFalse(matcher.matchesRule(rule, node, null));
	}

	@Test
	public void testClassMatchesRuleTrue() {
		IParsedNode node = new TestNode("testText", "testClassName", 2);
		RuleImplementation rule = new RuleImplementation();
		rule.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		rule.getNames().getTextItem().add("testClassName");
		RulesMatcher matcher = new RulesMatcher();
		Assert.assertTrue(matcher.matchesRule(rule, node, null));
	}

	@Test
	public void testClassAndTextMatchesRuleFalse() {
		IParsedNode node = new TestNode("testText", "testClassName", 2);
		RuleImplementation rule = new RuleImplementation();
		rule.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		rule.getNames().getTextItem().add("testClassName");
		RulesMatcher matcher = new RulesMatcher();
		Assert.assertTrue(matcher.matchesRule(rule, node, null));
	}

	@Test
	public void testTextMatchesRuleTrue() {
		IParsedNode node = new TestNode("testText", "testClassName", 2);
		RuleImplementation rule = new RuleImplementation();
		rule.setRuleMatchType(RuleMatchType.TEXT_ONLY);
		rule.getNames().getTextItem().add("anotherClassName");
		rule.getTextToFind().getTextItem().add("text");
		RulesMatcher matcher = new RulesMatcher();
		Assert.assertTrue(matcher.matchesRule(rule, node, null));
	}

	@Test
	public void testFullMatchesRuleTrue() {
		IParsedNode parentNode = new TestNode("cte", "testClassName", 1);

		IParsedNode node = new TestNode("cte", "testClassName", 2);
		RuleImplementation rule = new RuleImplementation();
		rule.setRuleMatchType(RuleMatchType.FULL);
		rule.getNames().getTextItem().add("testClassName");
		RulesMatcher matcher = new RulesMatcher();
		Assert.assertTrue(matcher.matchesRule(rule, node, parentNode));
	}

	@Test
	public void testStrcictMatchesRuleTrue() {
		IParsedNode parentNode = new TestNode("cte", "testClassName", 1);
		IParsedNode node = new TestNode("cte", "testClassName", 2);
		RuleImplementation rule = new RuleImplementation();
		rule.setRuleMatchType(RuleMatchType.STRICT);
		rule.getNames().getTextItem().add("testClassName");
		RulesMatcher matcher = new RulesMatcher();
		Assert.assertTrue(matcher.matchesRule(rule, node, parentNode));
	}

	@Test
	public void testStrcictMatchesRuleFalse() {
		IParsedNode parentNode = new TestNode("cte", "testClassName", 1);
		IParsedNode node = new TestNode("dasdasdssd", "testClassName", 2);
		RuleImplementation rule = new RuleImplementation();
		rule.setRuleMatchType(RuleMatchType.STRICT);
		rule.getNames().getTextItem().add("testClassName");
		RulesMatcher matcher = new RulesMatcher();
		Assert.assertFalse(matcher.matchesRule(rule, node, parentNode));
	}

	@Test
	public void testClassAndTextMatchesRuleTrue() {
		IParsedNode node = new TestNode("testText", "testClassName", 2);
		RuleImplementation rule = new RuleImplementation();
		rule.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		rule.getNames().getTextItem().add("testClassName");
		rule.getTextToFind().getTextItem().add("text");
		rule.setTextCheckType(TextCheckType.CONTAINS);
		RulesMatcher matcher = new RulesMatcher();
		Assert.assertTrue(matcher.matchesRule(rule, node, null));
	}

	@Test
	public void testDefaultMatchesRuleFalse() {
		IParsedNode node = new TestNode("testText", "testClassName", 2);
		RuleImplementation rule = new RuleImplementation();
		rule.setRuleMatchType(RuleMatchType.DEFAULT);
		rule.getNames().getTextItem().add("testClassName");
		RulesMatcher matcher = new RulesMatcher();
		Assert.assertFalse(matcher.matchesRule(rule, node, null));
	}
}
