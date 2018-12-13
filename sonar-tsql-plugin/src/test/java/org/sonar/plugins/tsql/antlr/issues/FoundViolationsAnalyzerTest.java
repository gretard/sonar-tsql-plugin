package org.sonar.plugins.tsql.antlr.issues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.antlr.lines.ILinesProvider;
import org.sonar.plugins.tsql.antlr.nodes.CandidateNode;
import org.sonar.plugins.tsql.antlr.nodes.IParsedNode;
import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.checks.custom.RuleResultType;
import org.sonar.plugins.tsql.helpers.TestNode;

public class FoundViolationsAnalyzerTest {

	@Test
	public void test() {

		FoundViolationsAnalyzer an = getSUT();
		Rule rule = new Rule();
		IParsedNode nnode = new TestNode("test", "test", 0);
		Map<RuleImplementation, List<IParsedNode>> results = new HashMap<>();
		Assert.assertEquals(0, an.create(new CandidateNode("test", rule, nnode), results).size());

	}

	@Test
	public void testFailIfNotFound_0() {

		FoundViolationsAnalyzer an = getSUT();
		Rule rule = new Rule();
		rule.setRuleImplementation(new RuleImplementation());
		IParsedNode nnode = new TestNode("test", "test", 0);
		Map<RuleImplementation, List<IParsedNode>> results = new HashMap<>();
		List<IParsedNode> nodes = new ArrayList<>();
		RuleImplementation ruleImpl = new RuleImplementation();
		ruleImpl.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		results.put(ruleImpl, nodes);
		Assert.assertEquals(1, an.create(new CandidateNode("test", rule, nnode), results).size());

	}

	@Test
	public void testFailIfNotFound_1() {

		FoundViolationsAnalyzer an = getSUT();
		Rule rule = new Rule();
		rule.setRuleImplementation(new RuleImplementation());
		IParsedNode nnode = new TestNode("test", "test", 0);
		Map<RuleImplementation, List<IParsedNode>> results = new HashMap<>();
		List<IParsedNode> nodes = new ArrayList<>();
		nodes.add(nnode);
		RuleImplementation ruleImpl = new RuleImplementation();
		ruleImpl.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		results.put(ruleImpl, nodes);
		Assert.assertEquals(0, an.create(new CandidateNode("test", rule, nnode), results).size());

	}

	@Test
	public void testSkipIfFound_1() {

		FoundViolationsAnalyzer an = getSUT();
		Rule rule = new Rule();
		rule.setRuleImplementation(new RuleImplementation());
		IParsedNode nnode = new TestNode("test", "test", 0);
		Map<RuleImplementation, List<IParsedNode>> results = new HashMap<>();
		List<IParsedNode> nodes = new ArrayList<>();
		nodes.add(nnode);
		RuleImplementation ruleImpl = new RuleImplementation();
		ruleImpl.setRuleResultType(RuleResultType.SKIP_IF_FOUND);
		results.put(ruleImpl, nodes);
		Assert.assertEquals(0, an.create(new CandidateNode("test", rule, nnode), results).size());

	}

	private FoundViolationsAnalyzer getSUT() {
		FoundViolationsAnalyzer an = new FoundViolationsAnalyzer(new ILinesProvider() {

			@Override
			public int getLine(IParsedNode node) {
				return 0;
			}
		});
		return an;
	}

	@Test
	public void testSkipIfFound_0() {

		FoundViolationsAnalyzer an = getSUT();
		Rule rule = new Rule();
		rule.setRuleImplementation(new RuleImplementation());
		IParsedNode nnode = new TestNode("test", "test", 0);
		Map<RuleImplementation, List<IParsedNode>> results = new HashMap<>();
		List<IParsedNode> nodes = new ArrayList<>();
		RuleImplementation ruleImpl0 = new RuleImplementation();
		ruleImpl0.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		RuleImplementation ruleImpl = new RuleImplementation();
		ruleImpl.setRuleResultType(RuleResultType.SKIP_IF_FOUND);
		results.put(ruleImpl, nodes);
		results.put(ruleImpl0, nodes);
		Assert.assertEquals(0, an.create(new CandidateNode("test", rule, nnode), results).size());

	}

	@Test
	public void testSkipIfNotFound_0() {
		FoundViolationsAnalyzer an = getSUT();
		Rule rule = new Rule();
		rule.setRuleImplementation(new RuleImplementation());
		IParsedNode nnode = new TestNode("test", "test", 0);
		Map<RuleImplementation, List<IParsedNode>> results = new HashMap<>();
		List<IParsedNode> nodes = new ArrayList<>();
		RuleImplementation ruleImpl = new RuleImplementation();
		ruleImpl.setRuleResultType(RuleResultType.SKIP_IF_NOT_FOUND);
		RuleImplementation ruleImpl0 = new RuleImplementation();
		ruleImpl0.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		results.put(ruleImpl, nodes);
		results.put(ruleImpl0, nodes);
		Assert.assertEquals(0, an.create(new CandidateNode("test", rule, nnode), results).size());

	}

	@Test
	public void testSkipIfNotFound_1() {

		FoundViolationsAnalyzer an = getSUT();
		Rule rule = new Rule();
		rule.setRuleImplementation(new RuleImplementation());
		IParsedNode nnode = new TestNode("test", "test", 0);
		Map<RuleImplementation, List<IParsedNode>> results = new HashMap<>();
		List<IParsedNode> nodes = new ArrayList<>();
		nodes.add(nnode);
		RuleImplementation ruleImpl = new RuleImplementation();
		ruleImpl.setRuleResultType(RuleResultType.SKIP_IF_NOT_FOUND);
		results.put(ruleImpl, nodes);
		Assert.assertEquals(0, an.create(new CandidateNode("test", rule, nnode), results).size());

	}

	@Test
	public void testFailIfLessFound() {

		FoundViolationsAnalyzer an = getSUT();
		Rule rule = new Rule();
		rule.setRuleImplementation(new RuleImplementation());
		IParsedNode nnode = new TestNode("test", "test", 0);
		Map<RuleImplementation, List<IParsedNode>> results = new HashMap<>();
		List<IParsedNode> nodes = new ArrayList<>();
		nodes.add(nnode);
		RuleImplementation ruleImpl = new RuleImplementation();
		ruleImpl.setRuleResultType(RuleResultType.FAIL_IF_LESS_FOUND);
		results.put(ruleImpl, nodes);
		Assert.assertEquals(0, an.create(new CandidateNode("test", rule, nnode), results).size());

	}

	@Test
	public void testFailIfLessFound2() {

		FoundViolationsAnalyzer an = getSUT();
		Rule rule = new Rule();
		rule.setRuleImplementation(new RuleImplementation());
		IParsedNode nnode = new TestNode("test", "test", 0);
		Map<RuleImplementation, List<IParsedNode>> results = new HashMap<>();
		List<IParsedNode> nodes = new ArrayList<>();
		nodes.add(nnode);
		RuleImplementation ruleImpl = new RuleImplementation();
		ruleImpl.setRuleResultType(RuleResultType.FAIL_IF_LESS_FOUND);
		ruleImpl.setTimes(2);
		results.put(ruleImpl, nodes);
		Assert.assertEquals(1, an.create(new CandidateNode("test", rule, nnode), results).size());

	}

	@Test
	public void testFailIfMoreFound() {

		FoundViolationsAnalyzer an = getSUT();
		Rule rule = new Rule();
		rule.setRuleImplementation(new RuleImplementation());
		IParsedNode nnode = new TestNode("test", "test", 0);
		Map<RuleImplementation, List<IParsedNode>> results = new HashMap<>();
		List<IParsedNode> nodes = new ArrayList<>();
		nodes.add(nnode);
		RuleImplementation ruleImpl = new RuleImplementation();
		ruleImpl.setRuleResultType(RuleResultType.FAIL_IF_MORE_FOUND);
		results.put(ruleImpl, nodes);
		Assert.assertEquals(1, an.create(new CandidateNode("test", rule, nnode), results).size());

	}

	@Test
	public void testFailIfMoreFound2() {

		FoundViolationsAnalyzer an = getSUT();
		Rule rule = new Rule();
		rule.setRuleImplementation(new RuleImplementation());
		IParsedNode nnode = new TestNode("test", "test", 0);
		Map<RuleImplementation, List<IParsedNode>> results = new HashMap<>();
		List<IParsedNode> nodes = new ArrayList<>();
		nodes.add(nnode);
		nodes.add(nnode);

		RuleImplementation ruleImpl = new RuleImplementation();
		ruleImpl.setRuleResultType(RuleResultType.FAIL_IF_MORE_FOUND);
		ruleImpl.setTimes(0);
		results.put(ruleImpl, nodes);
		Assert.assertEquals(1, an.create(new CandidateNode("test", rule, nnode), results).size());

	}

	@Test
	public void testFailIfFound() {

		FoundViolationsAnalyzer an = getSUT();
		Rule rule = new Rule();
		rule.setRuleImplementation(new RuleImplementation());
		IParsedNode nnode = new TestNode("test", "test", 0);
		Map<RuleImplementation, List<IParsedNode>> results = new HashMap<>();
		List<IParsedNode> nodes = new ArrayList<>();
		nodes.add(nnode);
		nodes.add(nnode);

		RuleImplementation ruleImpl = new RuleImplementation();
		ruleImpl.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		results.put(ruleImpl, nodes);
		Assert.assertEquals(1, an.create(new CandidateNode("test", rule, nnode), results).size());

	}

	@Test
	public void testDefault() {

		FoundViolationsAnalyzer an = getSUT();
		Rule rule = new Rule();
		rule.setRuleImplementation(new RuleImplementation());
		IParsedNode nnode = new TestNode("test", "test", 0);
		Map<RuleImplementation, List<IParsedNode>> results = new HashMap<>();
		List<IParsedNode> nodes = new ArrayList<>();
		nodes.add(nnode);
		nodes.add(nnode);

		RuleImplementation ruleImpl = new RuleImplementation();
		ruleImpl.setRuleResultType(RuleResultType.DEFAULT);
		results.put(ruleImpl, nodes);
		Assert.assertEquals(0, an.create(new CandidateNode("test", rule, nnode), results).size());

	}
}
