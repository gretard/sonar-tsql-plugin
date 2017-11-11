package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.helpers.AntrlResult;
import org.sonar.plugins.tsql.rules.custom.RuleMode;
import org.sonar.plugins.tsql.rules.custom.TextCheckType;
import org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

public class CandidateNodesProviderTest {

	@Test
	public void singleNode() {
		AntrlResult result = Antlr4Utils.getFull("SELECT * from dbo.test; SELECT * from dbo.test2;");
		ParseTree parseTree = result.getTree();

		final CandidateNodesProvider visitor = new org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider(
				Antlr4Utils.getSelectAllRule());
		IParsedNode[] candidates = visitor.getNodes(parseTree);
		Assert.assertEquals(2, candidates.length);
	}

	@Test
	public void getByClassTest() {
		AntrlResult result = Antlr4Utils
				.getFull("SELECT * from dbo.test; SELECT * from dbo.test; SELECT * from dbo.test2;");
		ParseTree parseTree = result.getTree();
		org.sonar.plugins.tsql.rules.custom.Rule r = Antlr4Utils.getSelectAllRule();
		r.getRuleImplementation().getTextToFind().getTextItem().clear();

		final CandidateNodesProvider visitor = new org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider(r);
		IParsedNode[] candidates = visitor.getNodes(parseTree);
		Assert.assertEquals(3, candidates.length);
	}

	@Test
	public void getByTextOnly() {
		AntrlResult result = Antlr4Utils
				.getFull("SELECT * from dbo.test; SELECT * from dbo.test; SELECT * from dbo.test2;");
		ParseTree parseTree = result.getTree();
		org.sonar.plugins.tsql.rules.custom.Rule r = Antlr4Utils.getSelectAllRule();
		r.getRuleImplementation().getNames().getTextItem().clear();
		r.getRuleImplementation().setTextCheckType(TextCheckType.CONTAINS);
		final CandidateNodesProvider visitor = new org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider(r);
		IParsedNode[] candidates = visitor.getNodes(parseTree);
		Assert.assertEquals(27, candidates.length);
	}

	@Test
	public void groupNodes() {
		AntrlResult result = Antlr4Utils.getFull("SELECT * from dbo.test; SELECT * from dbo.test2;");
		ParseTree parseTree = result.getTree();
		org.sonar.plugins.tsql.rules.custom.Rule r = Antlr4Utils.getSelectAllRule();
		r.getRuleImplementation().setRuleMode(RuleMode.GROUP);
		final CandidateNodesProvider visitor = new org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider(r);
		IParsedNode[] candidates = visitor.getNodes(parseTree);
		Assert.assertEquals(1, candidates.length);
	}

	@Test
	public void noMatchingNodes() {
		AntrlResult result = Antlr4Utils.getFull("SELECT 1 from dbo.test;");
		ParseTree parseTree = result.getTree();

		final CandidateNodesProvider visitor = new org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider(
				Antlr4Utils.getSelectAllRule());
		IParsedNode[] candidates = visitor.getNodes(parseTree);
		Assert.assertEquals(0, candidates.length);
	}
}
