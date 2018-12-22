package org.sonar.plugins.tsql.antlr;

import org.sonar.plugins.tsql.checks.custom.RuleImplementation;

public class CandidateNode {
	private final CandidateRule rule;
	private final IParsedNode nnode;

	public CandidateNode(final CandidateRule rule, final IParsedNode nnode) {
		this.rule = rule;
		this.nnode = nnode;
	}

	public String getRepoKey() {
		return this.rule.getKey();
	}
	public String getKey() {
		return this.rule.getRule().getKey();
	}
	public boolean isAdhoc() {
		return this.rule.isAdHoc();
	}

	public RuleImplementation getRuleImplementation() {
		return this.rule.getRuleImplementation();
	}

	public IParsedNode getNode() {
		return nnode;
	}

}
