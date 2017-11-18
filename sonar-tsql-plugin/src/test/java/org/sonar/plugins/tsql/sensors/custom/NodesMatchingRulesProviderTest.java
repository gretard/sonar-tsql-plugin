package org.sonar.plugins.tsql.sensors.custom;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.checks.custom.RuleMatchType;
import org.sonar.plugins.tsql.checks.custom.TextCheckType;
import org.sonar.plugins.tsql.helpers.TestNode;
import org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNode;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

public class NodesMatchingRulesProviderTest {

	@Test(expected = IllegalArgumentException.class)
	public void testCheck() {
		NodesMatchingRulesProvider test = new NodesMatchingRulesProvider(null);
		test.check(null);
	}

	@Test
	public void testCheckNodeEmptyClassAndTextNames() {
		TestNode node = new TestNode("testText", "test", 0);
		RuleImplementation impl = new RuleImplementation();
		Rule rule = new Rule();
		rule.setRuleImplementation(impl);
		CandidateNode sutNode = new CandidateNode("test", rule, node);
		NodesMatchingRulesProvider test = new NodesMatchingRulesProvider(null);
		Map<RuleImplementation, List<IParsedNode>> results = test.check(sutNode);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(1, results.get(impl).size());
	}

	@Test
	public void testCheckNodeMatchesClass() {
		TestNode node = new TestNode("testText", "test", 0);
		RuleImplementation impl = new RuleImplementation();
		impl.getNames().getTextItem().add("test");
		Rule rule = new Rule();
		rule.setRuleImplementation(impl);
		CandidateNode sutNode = new CandidateNode("test", rule, node);
		NodesMatchingRulesProvider test = new NodesMatchingRulesProvider(null);
		Map<RuleImplementation, List<IParsedNode>> results = test.check(sutNode);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(1, results.get(impl).size());
	}

	@Test
	public void testCheckNodeNotMatchesClass() {
		TestNode node = new TestNode("testText", "test", 0);
		RuleImplementation impl = new RuleImplementation();
		impl.getNames().getTextItem().add("test0");
		Rule rule = new Rule();
		rule.setRuleImplementation(impl);
		CandidateNode sutNode = new CandidateNode("test", rule, node);
		NodesMatchingRulesProvider test = new NodesMatchingRulesProvider(null);
		Map<RuleImplementation, List<IParsedNode>> results = test.check(sutNode);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(0, results.get(impl).size());
	}

	@Test
	public void testCheckNodeMatchesText() {
		TestNode node = new TestNode("testText", "test", 0);
		RuleImplementation impl = new RuleImplementation();
		impl.getTextToFind().getTextItem().add("testText");
		impl.setRuleMatchType(RuleMatchType.TEXT_ONLY);
		Rule rule = new Rule();
		rule.setRuleImplementation(impl);
		CandidateNode sutNode = new CandidateNode("test", rule, node);
		NodesMatchingRulesProvider test = new NodesMatchingRulesProvider(null);
		Map<RuleImplementation, List<IParsedNode>> results = test.check(sutNode);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(1, results.get(impl).size());
	}

	@Test
	public void testCheckNodeNotMatchesText() {
		TestNode node = new TestNode("testText", "test", 0);
		RuleImplementation impl = new RuleImplementation();
		impl.getTextToFind().getTextItem().add("aaaa");
		impl.setTextCheckType(TextCheckType.STRICT);
		impl.setRuleMatchType(RuleMatchType.TEXT_ONLY);

		Rule rule = new Rule();
		rule.setRuleImplementation(impl);
		CandidateNode sutNode = new CandidateNode("test", rule, node);
		NodesMatchingRulesProvider test = new NodesMatchingRulesProvider(null);
		Map<RuleImplementation, List<IParsedNode>> results = test.check(sutNode);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(0, results.get(impl).size());
	}

	@Test
	public void testCheckNodeMatchesFull() {
		TestNode nodeParent = new TestNode("testTextParent", "parentClassName", 0);
		TestNode node = new TestNode("testText", "test", 0);
		node.getInnerParents().add(nodeParent);
		RuleImplementation impl = new RuleImplementation();
		impl.getNames().getTextItem().add("test");
		impl.setRuleMatchType(RuleMatchType.CLASS_ONLY);

		RuleImplementation implParent = new RuleImplementation();
		implParent.setRuleMatchType(RuleMatchType.FULL);

		impl.getParentRules().getRuleImplementation().add(implParent);

		Rule rule = new Rule();
		rule.setRuleImplementation(impl);
		CandidateNode sutNode = new CandidateNode("test", rule, node);
		NodesMatchingRulesProvider test = new NodesMatchingRulesProvider(null);
		Map<RuleImplementation, List<IParsedNode>> results = test.check(sutNode);
		Assert.assertEquals(2, results.size());
		Assert.assertEquals(1, results.get(impl).size());
		Assert.assertEquals(1, results.get(implParent).size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckNodeNotMatchesFull() {
		TestNode nodeParent = new TestNode("testTextParent", "parentClassName", 0);
		TestNode node = new TestNode("testText", "test", 0);
		node.getInnerParents().add(nodeParent);
		RuleImplementation impl = new RuleImplementation();
		impl.getNames().getTextItem().add("test");
		impl.setRuleMatchType(RuleMatchType.FULL);
		Rule rule = new Rule();
		rule.setRuleImplementation(impl);
		CandidateNode sutNode = new CandidateNode("test", rule, node);
		NodesMatchingRulesProvider test = new NodesMatchingRulesProvider(null);
		test.check(sutNode);
	}
}
