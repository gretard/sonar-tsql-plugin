package org.sonar.plugins.tsql.antlr.nodes;

public interface INodesProvider<T> {
	IParsedNode[] getNodes(final T node);
}
