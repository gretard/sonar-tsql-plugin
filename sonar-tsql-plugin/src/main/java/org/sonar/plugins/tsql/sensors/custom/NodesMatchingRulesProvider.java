package org.sonar.plugins.tsql.sensors.custom;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;

public class NodesMatchingRulesProvider implements IParsedNodesProvider {

	public ParsedNode[] getNodes(final String repoKey, final ParseTree root, final Rule... rules) {
		final List<ParsedNode> candidates = new LinkedList<>();
		final IFiller[] fillers = new IFiller[] { new ParsedNodeUsesFiller(root) };

		for (final Rule rule : rules) {
			if (rule.getRuleImplementation() == null) {
				continue;
			}
			final RuleNodesVisitor visitor = new org.sonar.plugins.tsql.sensors.custom.RuleNodesVisitor(rule, repoKey);
			visitor.visit(root);

			final ParsedNode[] foundCandidates = visitor.getNodes();
		
			candidates.addAll(Arrays.asList(foundCandidates));
		}
		final ParsedNode[] finalCandidates = candidates.toArray(new ParsedNode[0]);
		for (final IFiller filler : fillers) {
			filler.fill(finalCandidates);
		}
		
		return finalCandidates;
	}
}
