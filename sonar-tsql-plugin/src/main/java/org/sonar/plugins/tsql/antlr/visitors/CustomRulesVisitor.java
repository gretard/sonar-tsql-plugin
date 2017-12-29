package org.sonar.plugins.tsql.antlr.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.checks.custom.RuleMode;
import org.sonar.plugins.tsql.rules.issues.DefaultIssuesFiller;
import org.sonar.plugins.tsql.rules.issues.IIssuesFiller;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.sensors.antlr4.CandidateRule;
import org.sonar.plugins.tsql.sensors.antlr4.FillerRequest;
import org.sonar.plugins.tsql.sensors.custom.FoundViolationsAnalyzer;
import org.sonar.plugins.tsql.sensors.custom.NodesMatchingRulesProvider;
import org.sonar.plugins.tsql.sensors.custom.lines.DefaultLinesProvider;
import org.sonar.plugins.tsql.sensors.custom.matchers.IMatcher;
import org.sonar.plugins.tsql.sensors.custom.matchers.NodeNameAndOrClassMatcher;
import org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNode;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;
import org.sonar.plugins.tsql.sensors.custom.nodes.NodeUsesProvider;
import org.sonar.plugins.tsql.sensors.custom.nodes.ParsedNode;

public class CustomRulesVisitor implements IParseTreeItemVisitor, ISensorFiller {
	private final Map<String, CandidateNode> groupedNodes = new HashMap<>();
	private final List<CandidateNode> singleNodes = new LinkedList<>();
	private final IMatcher matcher = new NodeNameAndOrClassMatcher();
	private final CandidateRule[] rules;
	private final IIssuesFiller filler = new DefaultIssuesFiller();

	public CustomRulesVisitor(final CandidateRule... rules) {
		this.rules = rules;
	}

	@Override
	public void visit(final ParseTree tree) {
		final ParsedNode parsedNode = new org.sonar.plugins.tsql.sensors.custom.nodes.ParsedNode(tree);

		for (final CandidateRule rule : this.rules) {

			final RuleImplementation ruleImplemention = rule.getRuleImplementation();
			if (matcher.match(ruleImplemention, parsedNode)) {
				final CandidateNode node = new CandidateNode(rule.getKey(), rule.getRule(), parsedNode);
				if (ruleImplemention.getRuleMode() == RuleMode.GROUP) {
					final String name = tree.getText();
					groupedNodes.putIfAbsent(name, node);
				} else {
					singleNodes.add(node);
				}
			}

		}

	}

	private CandidateNode[] getNodes() {
		singleNodes.addAll(groupedNodes.values());
		return singleNodes.toArray(new CandidateNode[0]);
	}

	@Override
	public void fill(SensorContext sensorContext, FillerRequest fillerRequest) {
		final InputFile file = fillerRequest.getFile();
		final CandidateNode[] candidates = this.getNodes();
		final FoundViolationsAnalyzer an = new FoundViolationsAnalyzer(
				new DefaultLinesProvider(fillerRequest.getStream()));
		final NodesMatchingRulesProvider m = new NodesMatchingRulesProvider(
				new NodeUsesProvider(fillerRequest.getRoot()));
		final List<TsqlIssue> issues = new ArrayList<TsqlIssue>();
		for (final CandidateNode candidate : candidates) {
			final Map<RuleImplementation, List<IParsedNode>> results = m.check(candidate);
			final List<TsqlIssue> foundIssues = an.create(candidate, results);
			issues.addAll(foundIssues);
		}
		filler.fill(sensorContext, file, issues.toArray(new TsqlIssue[0]));

	}

}
