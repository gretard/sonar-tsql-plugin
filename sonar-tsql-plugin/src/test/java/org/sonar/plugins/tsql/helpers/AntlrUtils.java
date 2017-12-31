package org.sonar.plugins.tsql.helpers;

import java.nio.charset.Charset;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.sonar.plugins.tsql.antlr.CandidateRule;
import org.sonar.plugins.tsql.antlr.FillerRequest;
import org.sonar.plugins.tsql.antlr.PluginHelper;
import org.sonar.plugins.tsql.antlr.issues.CustomIssuesProvider;
import org.sonar.plugins.tsql.antlr.nodes.CandidateNode;
import org.sonar.plugins.tsql.antlr.visitors.CustomRulesVisitor;
import org.sonar.plugins.tsql.antlr.visitors.CustomTreeVisitor;
import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.rules.definitions.CustomUserChecksProvider;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class AntlrUtils {
	public static SqlRules[] read(String path) {
		CustomUserChecksProvider provider = new CustomUserChecksProvider();
		return provider.getRules(null, "", path).values().toArray(new SqlRules[0]);
	}

	public static void print(String text) {
		FillerRequest request = getRequest(text);
		print(request.getRoot(), 0, request.getStream());
	}

	public static void print(final ParseTree node, final int level, CommonTokenStream stream) {
		final Interval sourceInterval = node.getSourceInterval();

		final Token firstToken = stream.get(sourceInterval.a);

		int line = firstToken.getLine();
		int charStart = firstToken.getCharPositionInLine();

		int endLine = line;
		int endChar = charStart + firstToken.getText().length();

		String data = "@(" + line + ":" + charStart + "," + endLine + ":" + endChar + ") with text: "
				+ firstToken.getText();
		final int tmp = level + 1;
		final StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.repeat("\t", level));
		sb.append(node.getClass().getSimpleName() + ": " + data + " :" + node.getText());
		System.out.println(sb.toString());
		final int n = node.getChildCount();
		for (int i = 0; i < n; i++) {

			final ParseTree c = node.getChild(i);
			print(c, tmp, stream);

		}
	}
	
	public static TsqlIssue[] verify(Rule rule, String text) {
		FillerRequest request = getRequest(text);
		CustomRulesVisitor visitor = new CustomRulesVisitor(new CandidateRule("test", rule));
		CustomTreeVisitor treeVisitor = new CustomTreeVisitor(visitor);
		treeVisitor.visit(request.getRoot());
		CandidateNode[] nodes = visitor.getNodes();
		final CustomIssuesProvider provider = new CustomIssuesProvider();
		TsqlIssue[] issues = provider.getIssues(request, nodes);
		return issues;
	}

	public static FillerRequest getRequest(String text) {
		final CharStream charStream = CharStreams.fromString(text.toUpperCase());
		FillerRequest request = PluginHelper.createRequestFromStream(null, Charset.defaultCharset(), charStream,
				IOUtils.toInputStream(text, Charset.defaultCharset()));
		return request;
	}

}
