package org.sonar.plugins.tsql.sensors.custom.nodes;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

public interface IParsedNode {
	
	public String getText();

	public String getClassName();

	public int getDistance();

	public ParseTree getItem();

	public List<IParsedNode> getParents();

	public List<IParsedNode> getChildren();

	public List<IParsedNode> getSiblings();
	
	public IParsedNode getControlFlowParent();

}