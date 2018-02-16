package org.sonar.plugins.tsql.antlr.nodes;

import org.antlr.v4.runtime.tree.ParseTree;

public interface IParsedNode {

	public String getText();

	public String getClassName();

	public int getDistance();

	public int getIndex();

	public int getIndex2();

	public ParseTree getItem();

	public IParsedNode[] getParents();

	public IParsedNode[] getChildren();

	public IParsedNode[] getSiblings();

	public IParsedNode getControlFlowParent();

}
