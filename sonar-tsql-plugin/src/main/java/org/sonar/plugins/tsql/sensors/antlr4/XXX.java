package org.sonar.plugins.tsql.sensors.antlr4;

import org.antlr.tsql.TSqlParser.Dml_clauseContext;
import org.antlr.tsql.TSqlParser.Join_partContext;
import org.antlr.tsql.TSqlParser.Order_by_expressionContext;
import org.antlr.tsql.TSqlParser.Search_condition_notContext;
import org.antlr.tsql.TSqlParser.Select_list_elemContext;
import org.antlr.tsql.TSqlParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

public class XXX extends TSqlParserBaseVisitor<Integer> {
	int c = 0;

	@Override
	public Integer visit(final ParseTree tree) {

		final int n = tree.getChildCount();
		int s = 0;
		for (int i = 0; i < n; i++) {
			final ParseTree c = tree.getChild(i);
			s += visit(c);
		}
		if (Join_partContext.class.equals(tree.getClass())) {
			s++;
		}
		//
		if (Order_by_expressionContext.class.equals(tree.getClass())) {
			s++;
		}
		if (Select_list_elemContext.class.equals(tree.getClass())) {
			s++;
		}
		if (Search_condition_notContext.class.equals(tree.getClass())) {
			s++;
		}
		if (Dml_clauseContext.class.equals(tree.getClass())) {
			s++;// s + 1;
		}
		return s;

	}

}