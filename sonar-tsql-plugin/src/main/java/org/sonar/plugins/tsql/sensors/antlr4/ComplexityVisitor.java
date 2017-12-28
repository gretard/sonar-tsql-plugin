package org.sonar.plugins.tsql.sensors.antlr4;

import org.antlr.tsql.TSqlParser.Dml_clauseContext;
import org.antlr.tsql.TSqlParser.Function_callContext;
import org.antlr.tsql.TSqlParser.Join_partContext;
import org.antlr.tsql.TSqlParser.Order_by_expressionContext;
import org.antlr.tsql.TSqlParser.Search_condition_notContext;
import org.antlr.tsql.TSqlParser.Select_list_elemContext;
import org.antlr.tsql.TSqlParser.Sql_unionContext;
import org.antlr.tsql.TSqlParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

/***
 * Calculates complexity. Based on
 * https://stackoverflow.com/questions/3353634/measuring-the-complexity-of-sql-statements
 */
public class ComplexityVisitor extends TSqlParserBaseVisitor<Integer> {
	int c = 0;

	@Override
	public Integer visit(final ParseTree tree) {

		final int n = tree.getChildCount();
		int s = 0;
		for (int i = 0; i < n; i++) {
			final ParseTree c = tree.getChild(i);
			s += visit(c);
		}
		final Class<? extends ParseTree> classz = tree.getClass();
		if (Sql_unionContext.class.equals(classz)) {
			s++;
		}
		if (Function_callContext.class.isAssignableFrom(classz)) {
			s++;
		}
		if (Join_partContext.class.equals(classz)) {
			s++;
		}
		if (Order_by_expressionContext.class.equals(classz)) {
			s++;
		}
		if (Select_list_elemContext.class.equals(classz)) {
			s++;
		}
		if (Search_condition_notContext.class.equals(classz)) {
			s++;
		}
		if (Dml_clauseContext.class.equals(classz)) {
			s++;
		}
		return s;

	}

}