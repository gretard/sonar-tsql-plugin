package org.sonar.plugins.tsql.sensors.custom;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.CustomRules;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class CustomRulesViolationsProvider implements ICustomRulesViolationsProvider {

	public CustomRulesViolationsProvider(final CommonTokenStream stream) {
		this(new NodesMatchingRulesProvider(), new CustomViolationsProvider(new DefaultLinesProvider(stream)));
	}

	public CustomRulesViolationsProvider(final IParsedNodesProvider parsedNodesProvider,
			final IViolationsProvider provider) {
		this.provider = provider;
		this.test = parsedNodesProvider;
	}

	final IViolationsProvider provider;
	final IParsedNodesProvider test;

	public TsqlIssue[] getIssues(final ParseTree root, final CustomRules... customRules) {

		final List<ParsedNode> c = new LinkedList<>();
		for (final CustomRules rules : customRules) {

			final ParsedNode[] candidates = test.getNodes(rules.getRepoKey(), root,
					rules.getRule().toArray(new Rule[0]));
			c.addAll(Arrays.asList(candidates));

		}
		final TsqlIssue[] issues = provider.getIssues(c.toArray(new ParsedNode[0]));
		return issues;
	}
}
