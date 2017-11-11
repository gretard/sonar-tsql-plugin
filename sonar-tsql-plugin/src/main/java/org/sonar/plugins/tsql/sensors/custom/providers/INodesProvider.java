package org.sonar.plugins.tsql.sensors.custom.providers;

import org.sonar.plugins.tsql.sensors.custom.ParsedNode;

public interface INodesProvider {
	ParsedNode[] getNodes(final ParsedNode node);
}
