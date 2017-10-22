package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;

public interface IParsedNode {
	public String getText();

	public String getClassName();

	public int getDistance();

	public ParseTree getItem();

	public Rule getRule();
}
