package org.sonar.plugins.tsql.antlr.visitors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.plugins.tsql.antlr.AntlrContext;

public interface IParseTreeItemVisitor {
	void apply(ParseTree tree);

	void fillContext(SensorContext context, AntlrContext antlrContext);
}
