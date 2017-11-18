package org.sonar.plugins.tsql.sensors.custom.nodes;

import org.sonar.plugins.tsql.checks.custom.Rule;

public class CandidateNode {
	private String key;
	private Rule rule;
	private IParsedNode nnode;

	public String getKey() {
		return key;
	}

	public Rule getRule() {
		return rule;
	}

	public IParsedNode getNode() {
		return nnode;
	}

	public CandidateNode(String key, Rule rule, IParsedNode nnode) {
		this.key = key;
		this.rule = rule;
		this.nnode = nnode;
	}
}
