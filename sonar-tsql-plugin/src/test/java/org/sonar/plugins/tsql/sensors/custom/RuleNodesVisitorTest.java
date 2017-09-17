package org.sonar.plugins.tsql.sensors.custom;

import static org.junit.Assert.*;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.antlr4.tsqlLexer;
import org.sonar.plugins.tsql.antlr4.tsqlParser;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Select_statementContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Tsql_fileContext;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.RuleMode;
import org.sonar.plugins.tsql.rules.custom.RuleResultType;

public class RuleNodesVisitorTest {

	@Test
	public void testFindByClassName() {
		final CharStream charStream = CharStreams.fromString("SELECT *, aa, 1, 2 FROM dbo.test; SELECT * from dbo.x");
		final tsqlLexer lexer = new tsqlLexer(charStream);
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		final tsqlParser parser = new tsqlParser(tokens);
		final Tsql_fileContext tree = parser.tsql_file();
		Rule rule = new Rule();
		RuleImplementation impl = new RuleImplementation();
		impl.getNames().getTextItem().add(Select_statementContext.class.getSimpleName());
		rule.setRuleImplementation(impl);
		RuleNodesVisitor visitor = new RuleNodesVisitor(rule, "test");
		visitor.visit(tree);
		ParsedNode[] nodes = visitor.getNodes();
		Assert.assertEquals(2, nodes.length);

	}

	@Test
	public void testFindByText() {
		final CharStream charStream = CharStreams.fromString("SELECT *, aa, 1, 2 FROM dbo.test");
		final tsqlLexer lexer = new tsqlLexer(charStream);
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		final tsqlParser parser = new tsqlParser(tokens);
		final Tsql_fileContext tree = parser.tsql_file();
		Rule rule = new Rule();
		RuleImplementation impl = new RuleImplementation();
		//impl.setInSameFlow(true);
		impl.setRuleMode(RuleMode.GROUP);
		impl.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		// impl.setClassName(Select_statementContext.class.getSimpleName());
		impl.getTextToFind().getTextItem().add("1,2");
		rule.setRuleImplementation(impl);
		RuleNodesVisitor visitor = new RuleNodesVisitor(rule, "test");
		visitor.visit(tree);
		ParsedNode[] nodes = visitor.getNodes();
		Assert.assertEquals(3, nodes.length);
	}

}
