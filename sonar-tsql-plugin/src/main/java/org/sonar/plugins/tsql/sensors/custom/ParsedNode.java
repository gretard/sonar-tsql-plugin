package org.sonar.plugins.tsql.sensors.custom;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;

public class ParsedNode {
	private final ParseTree item;

	private final Rule rule;

	private final String repositoryKey;
	private String name;

	private int distance;

	private final String className;

	final List<ParsedNode> siblings = new LinkedList<>();
	final List<ParsedNode> uses = new LinkedList<>();

	final List<ParsedNode> parents = new LinkedList<>();
	final List<ParsedNode> children = new LinkedList<>();

	public ParsedNode(final String name, final ParseTree item, final Rule rule, final String repositoryKey) {
		this.name = name;
		this.item = item;
		this.rule = rule;
		this.repositoryKey = repositoryKey;
		if (item == null) {
			this.className = name;
		} else {
			this.className = item.getClass().getSimpleName();
		}

	}

	public ParsedNode(final ParseTree item, final Rule rule, final String repositoryKey) {
		this.name = null;
		this.item = item;
		this.rule = rule;
		this.className = item.getClass().getSimpleName();
		this.repositoryKey = repositoryKey;
	}

	public ParsedNode(final String name, final ParseTree item, final String className, final Rule rule,
			final String repositoryKey) {
		this.name = name;
		this.item = item;
		this.rule = rule;
		this.repositoryKey = repositoryKey;
		this.className = className;
	}

	public String getText() {
		if (this.item == null) {
			return this.name;
		}
		return this.item.getText();
	}

	public ParsedNode(final String name, final ParseTree item, final String className, final Rule rule,
			final String repositoryKey, int distance) {
		// this.name = name;
		this.item = item;
		this.rule = rule;
		this.repositoryKey = repositoryKey;
		this.distance = distance;
		this.className = className;
	}

	public ParseTree getItem() {
		return item;
	}

	public Rule getRule() {
		return rule;
	}

	public String getName() {
		return name;
	}

	public String getClassName() {
		return className;
	}

	public String getRepository() {
		return repositoryKey;
	}

	public List<ParsedNode> getSiblings() {
		return siblings;
	}

	public List<ParsedNode> getUses() {
		return uses;
	}

	public List<ParsedNode> getParents() {
		return parents;
	}

	public List<ParsedNode> getChildren() {
		return children;
	}

}
