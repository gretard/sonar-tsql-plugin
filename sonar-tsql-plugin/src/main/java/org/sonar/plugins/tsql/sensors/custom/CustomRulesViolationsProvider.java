package org.sonar.plugins.tsql.sensors.custom;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.SqlRules;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.sensors.custom.lines.DefaultLinesProvider;

public class CustomRulesViolationsProvider implements ICustomRulesViolationsProvider {
	final IViolationsProvider customIssuesProvider;
	final IParsedNodesProvider parsedNodesProvider;
	private SqlRules[] customRules;

	public CustomRulesViolationsProvider(final CommonTokenStream stream, final SqlRules... customRules) {
		this(new NodesMatchingRulesProvider(), new CustomViolationsProvider(new DefaultLinesProvider(stream)), customRules);
	}

	public CustomRulesViolationsProvider(final IParsedNodesProvider parsedNodesProvider,
			final IViolationsProvider provider, final SqlRules... customRules) {
		this.customIssuesProvider = provider;
		this.parsedNodesProvider = parsedNodesProvider;
		this.customRules = customRules;
	}

	public TsqlIssue[] getIssues(final ParseTree root) {

		final List<ParsedNode> foundCandidates = new LinkedList<>();
		for (final SqlRules rules : customRules) {

			final ParsedNode[] candidates = parsedNodesProvider.getNodes(rules.getRepoKey(), root,
					rules.getRule().toArray(new Rule[0]));
			foundCandidates.addAll(Arrays.asList(candidates));

		}
		final TsqlIssue[] issues = customIssuesProvider.getIssues(foundCandidates.toArray(new ParsedNode[0]));
		return issues;
	}
}
