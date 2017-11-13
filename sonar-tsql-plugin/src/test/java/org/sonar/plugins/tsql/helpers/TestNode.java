package org.sonar.plugins.tsql.helpers;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

public class TestNode implements IParsedNode {

	private String text;
	private String className;
	private int distance;
	private TestNode parent;
	private int index;
	private int index2;
	
	public void setParent(TestNode parent) {
		this.parent = parent;
	}

	public TestNode(String text, String className, int distance) {
		this.text = text;
		this.className = className;
		this.distance = distance;
	}
	public TestNode(String text, String className, int distance, int index) {
		this.text = text;
		this.className = className;
		this.distance = distance;
		this.index = index;
	}
	public TestNode(String text, String className, int distance, int index, int index2) {
		this.text = text;
		this.className = className;
		this.distance = distance;
		this.index = index;
		this.index2 = index2;
		
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

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return index;
	}

	@Override
	public int getIndex2() {
		// TODO Auto-generated method stub
		return this.index2;
	}

}
