package org.sonar.plugins.tsql.sensors.custom;

public interface IFiller {
	void fill(ParsedNode... node);
}
