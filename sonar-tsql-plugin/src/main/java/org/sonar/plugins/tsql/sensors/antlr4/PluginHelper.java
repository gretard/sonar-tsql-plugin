package org.sonar.plugins.tsql.sensors.antlr4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.antlr.tsql.TSqlLexer;
import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.sensors.custom.lines.SourceLinesProvider;

public class PluginHelper {
	public static CandidateRule[] convert(final SqlRules... rules) {
		final List<CandidateRule> convertedRules = new ArrayList<>();
		for (SqlRules r : rules) {
			for (Rule rule : r.getRule()) {
				convertedRules.add(new CandidateRule(r.getRepoKey(), rule));
			}
		}
		return convertedRules.toArray(new CandidateRule[0]);
	}
	
	public static FillerRequest createRequest(final SourceLinesProvider linesProvider, final InputFile file, final Charset encoding)
			throws IOException, FileNotFoundException {
		final CharStream mainStream = CharStreams.fromPath(file.path(), encoding);
		final CharStream charStream = new CaseChangingCharStream(mainStream, true);
		final TSqlLexer lexer = new TSqlLexer(charStream);

		lexer.removeErrorListeners();

		final CommonTokenStream stream = new CommonTokenStream(lexer);

		stream.fill();
		final TSqlParser parser = new TSqlParser(stream);
		parser.removeErrorListeners();
		final ParseTree root = parser.tsql_file();
		final FillerRequest antrlFile = new FillerRequest(file, stream, root,
				linesProvider.getLines(new FileInputStream(file.file()), encoding));
		return antrlFile;
	}
}
