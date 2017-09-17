package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.rules.custom.Rule;

public interface IParsedNodesProvider {
	public ParsedNode[] getNodes(String repoKey, ParseTree root, Rule... rules);
}
