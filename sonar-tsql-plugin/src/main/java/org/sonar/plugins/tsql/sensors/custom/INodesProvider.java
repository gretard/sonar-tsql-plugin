package org.sonar.plugins.tsql.sensors.custom;

public interface INodesProvider {
	ParsedNode[] getNodes(final ParsedNode node);
}
