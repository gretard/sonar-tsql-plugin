package org.sonar.plugins.tsql.antlr.nodes;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.sonar.plugins.tsql.antlr.CandidateNode;
import org.sonar.plugins.tsql.antlr.CandidateRule;
import org.sonar.plugins.tsql.antlr.IParsedNode;
import org.sonar.plugins.tsql.antlr.issues.NodesMatchingRulesProvider;
import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.helpers.TestNode;

import junit.framework.Assert;

public class NodesMatchingRulesProviderTest {
	NodesMatchingRulesProvider sut = new NodesMatchingRulesProvider(new INodesProvider<IParsedNode>() {

		@Override
		public IParsedNode[] getNodes(IParsedNode node) {
			return new IParsedNode[] { new TestNode("test", "test", 2) };
		}
	});

	@Test
	public void testCheck() {
		Rule rule = new Rule();
		RuleImplementation child = new RuleImplementation();
		RuleImplementation imp = new RuleImplementation();
		imp.getChildrenRules().getRuleImplementation().add(child);
		rule.setRuleImplementation(imp);
		CandidateRule candidateRule = new CandidateRule("test", rule);
		IParsedNode nnode = new TestNode("test", "testClass", 1);
		Map<RuleImplementation, List<IParsedNode>> results = sut.check(new CandidateNode(candidateRule, nnode));
		Assert.assertEquals(2, results.size());
		Assert.assertEquals(1, results.get(rule.getRuleImplementation()).size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckNull() {
		Map<RuleImplementation, List<IParsedNode>> results = sut.check(null);
	}

}
