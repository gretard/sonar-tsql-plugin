package org.sonar.plugins.tsql.helpers;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.sensors.custom.IParsedNode;

public class TestNode implements IParsedNode {

	private String text;
	private String className;
	private int distance;

	public TestNode(String text, String className, int distance) {
		this.text = text;
		this.className = className;
		this.distance = distance;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public int getDistance() {
		return distance;
	}

	@Override
	public ParseTree getItem() {
		return null;
	}

	@Override
	public Rule getRule() {
		return null;
	}

}
