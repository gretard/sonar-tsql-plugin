package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.tsql.TSqlLexer;
import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.sensors.custom.lines.DefaultLinesProvider;
import org.sonar.plugins.tsql.sensors.custom.nodes.ParsedNode;

public class DefaultLinesProviderTest {

	@Test
	public void test() {

		final CharStream charStream = CharStreams.fromString("\r\nSELECT\r\n 1");

		final TSqlLexer lexer = new TSqlLexer(charStream);

		final CommonTokenStream stream = new CommonTokenStream(lexer);

		stream.fill();
		TSqlParser parser = new TSqlParser(stream);
		ParseTree child = parser.tsql_file().getChild(0);
		DefaultLinesProvider lines = new DefaultLinesProvider(stream);
		int line = lines.getLine(new ParsedNode(child));
		Assert.assertEquals(2, line);

	}

}
