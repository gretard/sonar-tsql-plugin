package org.sonar.plugins.tsql.sensors.custom;

import org.sonar.plugins.tsql.rules.custom.Rule;

public interface IFiller {
	void fill(Rule rule, ParsedNode... node);
}
