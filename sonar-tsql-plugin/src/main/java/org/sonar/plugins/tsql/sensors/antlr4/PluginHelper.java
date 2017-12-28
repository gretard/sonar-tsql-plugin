package org.sonar.plugins.tsql.sensors.antlr4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	public static String ruleToString(SqlRules customRules) {

		for (Rule r : customRules.getRule()) {
			List<String> compliant = r.getRuleImplementation().getCompliantRulesCodeExamples().getRuleCodeExample();
			List<String> violating = r.getRuleImplementation().getViolatingRulesCodeExamples().getRuleCodeExample();
			if (compliant.isEmpty() && violating.isEmpty()) {
				continue;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(r.getDescription());
			sb.append("<h2>Code examples</h2>");
			if (!violating.isEmpty()) {
				sb.append("<h3>Non-compliant</h3>");
				for (String x : violating) {
					sb.append("<pre><code>" + x + "</code></pre>");
				}
			}

			if (!compliant.isEmpty()) {
				sb.append("<h3>Compliant</h3>");
				for (String x : compliant) {
					sb.append("<pre><code>" + x + "</code></pre>");
				}
			}
			if (r.getSource() != null && !r.getSource().isEmpty()) {
				sb.append("<h4>Source</h4>");
				sb.append(String.format("<a href='%s'>%s</a>", r.getSource(), r.getSource()));
			}
			r.setDescription(sb.toString());

		}
		String xmlString = "";
		try {
			JAXBContext context = JAXBContext.newInstance(SqlRules.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To
			StringWriter sw = new StringWriter();
			m.marshal(customRules, sw);
			xmlString = sw.toString();

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return xmlString;
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
