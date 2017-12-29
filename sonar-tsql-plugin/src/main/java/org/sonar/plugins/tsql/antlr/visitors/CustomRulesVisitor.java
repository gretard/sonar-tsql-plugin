package org.sonar.plugins.tsql.antlr.visitors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.plugins.tsql.antlr.CandidateRule;
import org.sonar.plugins.tsql.antlr.FillerRequest;
import org.sonar.plugins.tsql.antlr.issues.CustomIssuesProvider;
import org.sonar.plugins.tsql.antlr.nodes.CandidateNode;
import org.sonar.plugins.tsql.antlr.nodes.ParsedNode;
import org.sonar.plugins.tsql.antlr.nodes.matchers.IMatcher;
import org.sonar.plugins.tsql.antlr.nodes.matchers.NodeNameAndOrClassMatcher;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.checks.custom.RuleMode;
import org.sonar.plugins.tsql.rules.issues.DefaultIssuesFiller;
import org.sonar.plugins.tsql.rules.issues.IIssuesFiller;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class CustomRulesVisitor implements IParseTreeItemVisitor, ISensorFiller {
	private final Map<String, CandidateNode> groupedNodes = new HashMap<>();
	private final List<CandidateNode> singleNodes = new LinkedList<>();
	private final IMatcher matcher = new NodeNameAndOrClassMatcher();
	private final CandidateRule[] rules;
	private final IIssuesFiller filler = new DefaultIssuesFiller();
	private final CustomIssuesProvider issuesProvider = new CustomIssuesProvider();

	public CustomRulesVisitor(final CandidateRule... rules) {
		this.rules = rules;
	}

	@Override
	public void visit(final ParseTree tree) {
		final ParsedNode parsedNode = new org.sonar.plugins.tsql.antlr.nodes.ParsedNode(tree);

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

	public CandidateNode[] getNodes() {
		singleNodes.addAll(groupedNodes.values());
		return singleNodes.toArray(new CandidateNode[0]);
	}

	@Override
	public void fill(SensorContext sensorContext, FillerRequest fillerRequest) {
		final InputFile file = fillerRequest.getFile();
		TsqlIssue[] foundIssues = issuesProvider.getIssues(fillerRequest, this.getNodes());
		filler.fill(sensorContext, file, foundIssues);

	}

}
