package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.antlr4.tsqlLexer;
import org.sonar.plugins.tsql.antlr4.tsqlParser;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Tsql_fileContext;
import org.sonar.plugins.tsql.sensors.custom.lines.DefaultLinesProvider;
import org.sonar.plugins.tsql.sensors.custom.nodes.ParsedNode;

public class DefaultLinesProviderTest {

	@Test
	public void test() {
		final CharStream charStream = CharStreams.fromString("\r\n\r\nselect 2");
		final tsqlLexer lexer = new tsqlLexer(charStream);
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		final tsqlParser parser = new tsqlParser(tokens);
		final Tsql_fileContext tree = parser.tsql_file();
		ParseTree child = tree.getChild(0);
		DefaultLinesProvider lines = new DefaultLinesProvider(tokens);
		int line = lines.getLine(new ParsedNode(child));
		Assert.assertEquals(3, line);
	}

}
