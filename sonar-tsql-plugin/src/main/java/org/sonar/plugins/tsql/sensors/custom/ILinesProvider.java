package org.sonar.plugins.tsql.sensors.custom;

public interface ILinesProvider {
	int getLine(ParsedNode node);
}
