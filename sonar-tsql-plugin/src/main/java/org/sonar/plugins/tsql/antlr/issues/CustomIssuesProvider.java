package org.sonar.plugins.tsql.antlr.issues;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sonar.plugins.tsql.antlr.AntlrContext;
import org.sonar.plugins.tsql.antlr.CandidateNode;
import org.sonar.plugins.tsql.antlr.IParsedNode;
import org.sonar.plugins.tsql.antlr.lines.DefaultLinesProvider;
import org.sonar.plugins.tsql.antlr.nodes.NodeUsesProvider;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class CustomIssuesProvider {

	public TsqlIssue[] getIssues(AntlrContext antlrContext, CandidateNode[] candidates) {
		final org.sonar.plugins.tsql.antlr.issues.FoundViolationsAnalyzer foundViolationsAnalyzer = new FoundViolationsAnalyzer(
				new DefaultLinesProvider(antlrContext.getStream()));

		final NodesMatchingRulesProvider nodesMatchingRulesProvider = new NodesMatchingRulesProvider(
				new NodeUsesProvider(antlrContext.getRoot()));

		final List<TsqlIssue> issues = new ArrayList<>();

		for (final CandidateNode candidate : candidates) {
			final Map<RuleImplementation, List<IParsedNode>> results = nodesMatchingRulesProvider.check(candidate);
			final List<TsqlIssue> foundIssues = foundViolationsAnalyzer.analyze(candidate, results);
			issues.addAll(foundIssues);
		}
		return issues.toArray(new TsqlIssue[0]);
	}
}
