package org.sonar.plugins.tsql.antlr.nodes;

import org.sonar.plugins.tsql.antlr.IParsedNode;

public interface INodesProvider<T> {
	IParsedNode[] getNodes(final T node);
}
