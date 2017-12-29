package org.sonar.plugins.tsql.sensors.antlr4;

import org.antlr.tsql.TSqlParser.Cfl_statementContext;
import org.antlr.tsql.TSqlParser.Search_condition_notContext;
import org.antlr.tsql.TSqlParser.Try_catch_statementContext;
import org.antlr.tsql.TSqlParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

/***
 * */
public class CComplexityVisitor extends TSqlParserBaseVisitor<Object> {
	int complexity = 1;

	public int getComplexity() {
		return complexity;
	}

	@Override
	public Object visit(ParseTree tree) {

		final int n = tree.getChildCount();
		for (int i = 0; i < n; i++) {
			final ParseTree c = tree.getChild(i);
			visit(c);
		}
		final Class<? extends ParseTree> classz = tree.getClass();
		if (Search_condition_notContext.class.equals(classz)) {
			complexity++;
		}

		if (Try_catch_statementContext.class.equals(classz)) {
			complexity++;
		}

		return null;
	}

	@Override
	public Object visitCfl_statement(Cfl_statementContext ctx) {
		complexity++;
		super.visitCfl_statement(ctx);
		return null;
	}

}