package org.sonar.plugins.tsql.helpers;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

public class TestNode implements IParsedNode {

	private String text;
	private String className;
	private int distance;
	private TestNode parent;
	
	public void setParent(TestNode parent) {
		this.parent = parent;
	}

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
	public List<IParsedNode> getParents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IParsedNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IParsedNode> getSiblings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IParsedNode getControlFlowParent() {
		return parent;
	}

}
