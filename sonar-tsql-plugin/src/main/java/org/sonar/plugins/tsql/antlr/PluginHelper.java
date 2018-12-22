package org.sonar.plugins.tsql.antlr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.antlr.tsql.TSqlLexer;
import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.input.BOMInputStream;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.lines.SourceLinesProvider;

public final class PluginHelper {

	

	


	public static AntlrContext createRequestFromStream(final InputFile file, final Charset encoding,
			final CharStream mainStream, InputStream fileInputStream) {
		final SourceLinesProvider linesProvider = new SourceLinesProvider();
		final CharStream charStream = new CaseChangingCharStream(mainStream, true);
		final TSqlLexer lexer = new TSqlLexer(charStream);

		lexer.removeErrorListeners();

		final CommonTokenStream stream = new CommonTokenStream(lexer);

		stream.fill();
		final TSqlParser parser = new TSqlParser(stream);
		parser.removeErrorListeners();
		final ParseTree root = parser.tsql_file();
		final AntlrContext antrlFile = new AntlrContext(file, stream, root,
				linesProvider.getLines(fileInputStream, encoding));
		return antrlFile;
	}
}
