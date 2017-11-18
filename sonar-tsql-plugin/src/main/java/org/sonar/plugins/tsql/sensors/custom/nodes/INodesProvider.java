package org.sonar.plugins.tsql.sensors.custom.nodes;

public interface INodesProvider<T> {
	IParsedNode[] getNodes(final T node);
}
