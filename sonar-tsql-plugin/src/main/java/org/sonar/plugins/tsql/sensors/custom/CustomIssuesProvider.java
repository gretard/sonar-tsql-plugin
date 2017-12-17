package org.sonar.plugins.tsql.sensors.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.sensors.antlr4.CandidateRule;
import org.sonar.plugins.tsql.sensors.custom.lines.DefaultLinesProvider;
import org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNode;
import org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;
import org.sonar.plugins.tsql.sensors.custom.nodes.NodeUsesProvider;

public class CustomIssuesProvider {
	private static final Logger LOGGER = Loggers.get(FoundViolationsAnalyzer.class);
	private CandidateRule[] rules;

	public CustomIssuesProvider(CandidateRule... rules) {
		this.rules = rules;

	}

	public TsqlIssue[] getIssues(final CommonTokenStream stream) {
		final TSqlParser parser = new TSqlParser(stream);

		if (!LOGGER.isDebugEnabled()) {
			parser.removeErrorListeners();
		}
		final ParseTree root = parser.tsql_file();
		final CandidateNodesProvider candidatesProvider = new org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider(
				rules);
		final FoundViolationsAnalyzer an = new FoundViolationsAnalyzer(new DefaultLinesProvider(stream));
		final CandidateNode[] candidates = candidatesProvider.getNodes(root);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(String.format("Found %s candidates matching custom rules", candidates.length));
		}
		final NodesMatchingRulesProvider m = new NodesMatchingRulesProvider(new NodeUsesProvider(root));
		final List<TsqlIssue> issues = new ArrayList<TsqlIssue>();
		for (CandidateNode candidate : candidates) {
			final Map<RuleImplementation, List<IParsedNode>> results = m.check(candidate);
			final List<TsqlIssue> foundIssues = an.create(candidate, results);
			issues.addAll(foundIssues);
		}
		final TsqlIssue[] finalIssues = issues.toArray(new TsqlIssue[0]);
		return finalIssues;
	}
}
