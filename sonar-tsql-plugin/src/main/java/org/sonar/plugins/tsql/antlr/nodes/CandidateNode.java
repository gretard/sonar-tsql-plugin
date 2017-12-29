package org.sonar.plugins.tsql.antlr.nodes;

import org.sonar.plugins.tsql.checks.custom.Rule;

public class CandidateNode {
	private final String key;
	private final Rule rule;
	private final IParsedNode nnode;

	public CandidateNode(final String key, final Rule rule, final IParsedNode nnode) {
		this.key = key;
		this.rule = rule;
		this.nnode = nnode;
	}

	public String getKey() {
		return key;
	}

	public Rule getRule() {
		return rule;
	}

	public IParsedNode getNode() {
		return nnode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((nnode == null) ? 0 : nnode.hashCode());
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CandidateNode other = (CandidateNode) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (nnode == null) {
			if (other.nnode != null)
				return false;
		} else if (!nnode.equals(other.nnode))
			return false;
		if (rule == null) {
			if (other.rule != null)
				return false;
		} else if (!rule.equals(other.rule))
			return false;
		return true;
	}

}
