package org.antlr.tsql;

import static org.junit.Assert.*;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.sonar.plugins.tsql.antlr4.tsqlLexer;
import org.sonar.plugins.tsql.antlr4.tsqlParser;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.helpers.AntrlResult;

public class TSqlParserTest {

	@Test
	public void test() {
		final CharStream charStream = CharStreams.fromString("SELECT * FROM aaal.test.yba".toUpperCase());
		final TSqlLexer lexer = new TSqlLexer(charStream);
		final CommonTokenStream stream = new CommonTokenStream(lexer);
		stream.fill();
		final TSqlParser parser = new TSqlParser(stream);
		
		ParseTree node = parser.tsql_file();
		Antlr4Utils.print(node, 0);
	}

}
