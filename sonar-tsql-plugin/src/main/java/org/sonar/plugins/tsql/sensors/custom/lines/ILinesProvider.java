package org.sonar.plugins.tsql.sensors.custom.lines;

import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;
import org.sonar.plugins.tsql.sensors.custom.nodes.ParsedNode;

public interface ILinesProvider {
	int getLine(IParsedNode node);
}
