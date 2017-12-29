package org.sonar.plugins.tsql.antlr.visitors;

import org.antlr.v4.runtime.tree.ParseTree;

public interface IParseTreeItemVisitor extends ISensorFiller{
	void visit(ParseTree tree);
}
