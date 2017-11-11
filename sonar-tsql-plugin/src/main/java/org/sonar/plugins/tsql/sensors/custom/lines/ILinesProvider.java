package org.sonar.plugins.tsql.sensors.custom.lines;

import org.sonar.plugins.tsql.sensors.custom.ParsedNode;

public interface ILinesProvider {
	int getLine(ParsedNode node);
}
