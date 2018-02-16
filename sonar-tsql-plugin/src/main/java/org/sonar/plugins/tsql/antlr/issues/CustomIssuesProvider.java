package org.sonar.plugins.tsql.antlr.issues;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sonar.plugins.tsql.antlr.FillerRequest;
import org.sonar.plugins.tsql.antlr.lines.DefaultLinesProvider;
import org.sonar.plugins.tsql.antlr.nodes.CandidateNode;
import org.sonar.plugins.tsql.antlr.nodes.IParsedNode;
import org.sonar.plugins.tsql.antlr.nodes.NodeUsesProvider;
import org.sonar.plugins.tsql.antlr.nodes.NodesMatchingRulesProvider;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class CustomIssuesProvider {
	public TsqlIssue[] getIssues(FillerRequest fillerRequest, CandidateNode[] candidates) {
		final org.sonar.plugins.tsql.antlr.issues.FoundViolationsAnalyzer an = new FoundViolationsAnalyzer(
				new DefaultLinesProvider(fillerRequest.getStream()));
		final NodesMatchingRulesProvider m = new NodesMatchingRulesProvider(
				new NodeUsesProvider(fillerRequest.getRoot()));
		final List<TsqlIssue> issues = new ArrayList<TsqlIssue>();
		for (final CandidateNode candidate : candidates) {
			final Map<RuleImplementation, List<IParsedNode>> results = m.check(candidate);
			final List<TsqlIssue> foundIssues = an.create(candidate, results);
			issues.addAll(foundIssues);
		}
		return issues.toArray(new TsqlIssue[0]);
	}
}
