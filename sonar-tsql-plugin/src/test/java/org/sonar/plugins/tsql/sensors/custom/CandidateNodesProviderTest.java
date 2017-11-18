package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.checks.custom.RuleMode;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.checks.custom.TextCheckType;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.helpers.AntrlResult;
import org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNode;
import org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider;

public class CandidateNodesProviderTest {

	@Test
	public void singleNode() {
		AntrlResult result = Antlr4Utils
				.getFull("SELECT * from dbo.test; SELECT name from dbo.test; SELECT * from dbo.test2;");
		ParseTree parseTree = result.getTree();
		SqlRules rules = new SqlRules();
		org.sonar.plugins.tsql.checks.custom.Rule r = Antlr4Utils.getSelectAllRule();
		rules.getRule().add(r);
		final CandidateNodesProvider visitor = new org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider(
				rules);
		CandidateNode[] candidates = visitor.getNodes(parseTree);
		Assert.assertEquals(2, candidates.length);
	}

	@Test
	public void getByClassTest() {
		AntrlResult result = Antlr4Utils.getFull(
				"SELECT * from dbo.test; SELECT name from dbo.test; SELECT * from dbo.test; SELECT * from dbo.test2;");
		ParseTree parseTree = result.getTree();

		org.sonar.plugins.tsql.checks.custom.Rule r = Antlr4Utils.getSelectAllRule();
		r.getRuleImplementation().getTextToFind().getTextItem().clear();
		SqlRules rules = new SqlRules();
		rules.getRule().add(r);
		final CandidateNodesProvider visitor = new org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider(
				rules);
		CandidateNode[] candidates = visitor.getNodes(parseTree);
		Assert.assertEquals(4, candidates.length);
	}

	@Test
	public void getByTextOnly() {
		AntrlResult result = Antlr4Utils
				.getFull("SELECT * from dbo.test; SELECT * from dbo.test; SELECT * from dbo.test2;");
		ParseTree parseTree = result.getTree();
		org.sonar.plugins.tsql.checks.custom.Rule r = Antlr4Utils.getSelectAllRule();
		r.getRuleImplementation().getNames().getTextItem().clear();
		r.getRuleImplementation().setTextCheckType(TextCheckType.CONTAINS);
		SqlRules rules = new SqlRules();
		rules.getRule().add(r);
		final CandidateNodesProvider visitor = new org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider(
				rules);
		CandidateNode[] candidates = visitor.getNodes(parseTree);
		Assert.assertEquals(30, candidates.length);
	}

	@Test
	public void groupNodes() {
		AntrlResult result = Antlr4Utils
				.getFull("SELECT name from dbo.test; SELECT * from dbo.test; SELECT * from dbo.test2;");
		ParseTree parseTree = result.getTree();
		org.sonar.plugins.tsql.checks.custom.Rule r = Antlr4Utils.getSelectAllRule();
		r.getRuleImplementation().setRuleMode(RuleMode.GROUP);
		SqlRules rules = new SqlRules();
		rules.getRule().add(r);
		final CandidateNodesProvider visitor = new org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider(
				rules);
		CandidateNode[] candidates = visitor.getNodes(parseTree);
		Assert.assertEquals(1, candidates.length);
	}

	@Test
	public void noMatchingNodes() {
		AntrlResult result = Antlr4Utils.getFull("SELECT 1 from dbo.test;");
		ParseTree parseTree = result.getTree();
		org.sonar.plugins.tsql.checks.custom.Rule r = Antlr4Utils.getSelectAllRule();
		SqlRules rules = new SqlRules();
		rules.getRule().add(r);
		final CandidateNodesProvider visitor = new org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider(
				rules);
		CandidateNode[] candidates = visitor.getNodes(parseTree);
		Assert.assertEquals(0, candidates.length);
	}

	@Test
	public void nullNode() {
		SqlRules rules = new SqlRules();
		final CandidateNodesProvider visitor = new org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider(
				rules);
		CandidateNode[] candidates = visitor.getNodes(null);
		Assert.assertEquals(0, candidates.length);
	}
}
