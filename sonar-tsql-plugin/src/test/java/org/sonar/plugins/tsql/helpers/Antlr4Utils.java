package org.sonar.plugins.tsql.helpers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.antlr.tsql.TSqlLexer;
import org.antlr.tsql.TSqlParser;
import org.antlr.tsql.TSqlParser.Column_name_listContext;
import org.antlr.tsql.TSqlParser.ConstantContext;
import org.antlr.tsql.TSqlParser.Cursor_nameContext;
import org.antlr.tsql.TSqlParser.Cursor_statementContext;
import org.antlr.tsql.TSqlParser.Declare_cursorContext;
import org.antlr.tsql.TSqlParser.Declare_statementContext;
import org.antlr.tsql.TSqlParser.Execute_statementContext;
import org.antlr.tsql.TSqlParser.Full_column_nameContext;
import org.antlr.tsql.TSqlParser.Func_proc_nameContext;
import org.antlr.tsql.TSqlParser.Function_callContext;
import org.antlr.tsql.TSqlParser.IdContext;
import org.antlr.tsql.TSqlParser.Insert_statementContext;
import org.antlr.tsql.TSqlParser.Order_by_clauseContext;
import org.antlr.tsql.TSqlParser.PredicateContext;
import org.antlr.tsql.TSqlParser.Primitive_expressionContext;
import org.antlr.tsql.TSqlParser.Query_expressionContext;
import org.antlr.tsql.TSqlParser.SCALAR_FUNCTIONContext;
import org.antlr.tsql.TSqlParser.Search_conditionContext;
import org.antlr.tsql.TSqlParser.Select_list_elemContext;
import org.antlr.tsql.TSqlParser.Set_statementContext;
import org.antlr.tsql.TSqlParser.Simple_idContext;
import org.antlr.tsql.TSqlParser.Table_constraintContext;
import org.antlr.tsql.TSqlParser.Table_hintContext;
import org.antlr.tsql.TSqlParser.Table_nameContext;
import org.antlr.tsql.TSqlParser.Waitfor_statementContext;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.RuleDistanceIndexMatchType;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.checks.custom.RuleMatchType;
import org.sonar.plugins.tsql.checks.custom.RuleMode;
import org.sonar.plugins.tsql.checks.custom.RuleResultType;
import org.sonar.plugins.tsql.checks.custom.SqlRules;
import org.sonar.plugins.tsql.checks.custom.TextCheckType;
import org.sonar.plugins.tsql.rules.definitions.CustomRulesProvider;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.sensors.custom.CustomIssuesProvider;
import org.sonar.plugins.tsql.sensors.custom.FoundViolationsAnalyzer;
import org.sonar.plugins.tsql.sensors.custom.NodesMatchingRulesProvider;
import org.sonar.plugins.tsql.sensors.custom.lines.DefaultLinesProvider;
import org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNode;
import org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;
import org.sonar.plugins.tsql.sensors.custom.nodes.NodeUsesProvider;

public class Antlr4Utils {
	public static ParseTree get(String text) {
		return getFull(text).getTree();
	}

	public static void print(final ParseTree node, final int level) {
		final int tmp = level + 1;
		final StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.repeat("\t", level));
		sb.append(node.getClass().getSimpleName() + "@" + level + ": " + node.getText());
		System.out.println(sb.toString());
		final int n = node.getChildCount();
		for (int i = 0; i < n; i++) {

			final ParseTree c = node.getChild(i);
			print(c, tmp);

		}
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

	public static TsqlIssue[] verify2(Rule rule, String text) {
		final CharStream charStream = CharStreams.fromString(text.toUpperCase());
		final TSqlLexer lexer = new TSqlLexer(charStream);
		final CommonTokenStream stream = new CommonTokenStream(lexer);
		stream.fill();
		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("test");
		customRules.setRepoName("test");
		customRules.getRule().add(rule);
		final CustomIssuesProvider provider = new CustomIssuesProvider();
		TsqlIssue[] issues = provider.getIssues(stream, customRules);
		return issues;
	}
	public static TsqlIssue[] verifyWithPrinting(Rule rule, String text) {
		final CharStream charStream = CharStreams.fromString(text.toUpperCase());
		final TSqlLexer lexer = new TSqlLexer(charStream);
		final CommonTokenStream stream = new CommonTokenStream(lexer);
		stream.fill();
		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("test");
		customRules.setRepoName("test");
		customRules.getRule().add(rule);
		
		final TSqlParser parser = new TSqlParser(stream);

		final ParseTree root = parser.tsql_file();
		final CandidateNodesProvider candidatesProvider = new org.sonar.plugins.tsql.sensors.custom.nodes.CandidateNodesProvider(
				customRules);
		final FoundViolationsAnalyzer an = new FoundViolationsAnalyzer(new DefaultLinesProvider(stream));
		final CandidateNode[] candidates = candidatesProvider.getNodes(root);
		
		final NodesMatchingRulesProvider m = new NodesMatchingRulesProvider(new NodeUsesProvider(root));
		final List<TsqlIssue> issues = new ArrayList<TsqlIssue>();
		for (CandidateNode candidate : candidates) {
			System.out.println(String.format(
					"Found candidate with text %s of class %s against rule: %s classes and %s text",
					candidate.getNode().getText(),
					candidate.getNode().getClassName(),
					Arrays.toString(candidate.getRule().getRuleImplementation().getNames().getTextItem().toArray(new String[0])),
					Arrays.toString(candidate.getRule().getRuleImplementation().getTextToFind().getTextItem().toArray(new String[0]))
					
					));
			final Map<RuleImplementation, List<IParsedNode>> results = m.check(candidate);
			
			for (Entry<RuleImplementation, List<IParsedNode>> entry : results.entrySet()) {
				RuleImplementation mRule = entry.getKey();
				List<IParsedNode> items = entry.getValue();
				System.out.println(String.format(
						"Found %s candidates against [match: %s, result: %s, distance %s, index: %s, indexCheck %s, distanceCheck: %s] rule: %s classes and %s text",
						items.size(),
						mRule.getRuleMatchType(),
						mRule.getRuleResultType(),
						mRule.getDistance(),
						mRule.getIndex(),
						mRule.getIndexCheckType(),
						mRule.getDistanceCheckType(),
						Arrays.toString(mRule.getNames().getTextItem().toArray(new String[0])),
						Arrays.toString(mRule.getTextToFind().getTextItem().toArray(new String[0]))
						
						));
				for (IParsedNode node : items) {
					System.out.println("\tNode matching rule: "+node.getText()+" "+node.getClassName()+" Distance: "+node.getDistance()+" Index: "+node.getIndex()+" Index2: "+node.getIndex2());
				}
			}
			
			final List<TsqlIssue> foundIssues = an.create(candidate, results);
			issues.addAll(foundIssues);
		}
		final TsqlIssue[] finalIssues = issues.toArray(new TsqlIssue[0]);
		return finalIssues;
		
	}
	public static AntrlResult getFull(String text) {
		final CharStream charStream = CharStreams.fromString(text.toUpperCase());
		return getFromStream(charStream);
	}

	private static AntrlResult getFromStream(final CharStream charStream) {

		final TSqlLexer lexer = new TSqlLexer(charStream);
		final CommonTokenStream stream = new CommonTokenStream(lexer);
		stream.fill();
		final TSqlParser parser = new TSqlParser(stream);

		AntrlResult result = new AntrlResult();
		result.setTree(parser.tsql_file());
		result.setStream(stream);
		return result;
	}

	public static AntrlResult getFull(InputStream stream) throws IOException {
		final CharStream charStream = CharStreams.fromStream(stream);
		return getFromStream(charStream);
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

	public static String ruleImplToString(Rule r) {
		System.out.println(r.getKey());
		String xmlString = "";
		List<String> compliant = r.getRuleImplementation().getCompliantRulesCodeExamples().getRuleCodeExample();
		List<String> violating = r.getRuleImplementation().getViolatingRulesCodeExamples().getRuleCodeExample();

		StringBuilder sb = new StringBuilder();
		sb.append(r.getDescription());
		sb.append("<h2>Code examples</h2>");
		if (!violating.isEmpty()) {
			sb.append("<h3>Non-compliant</h3>");
			for (String x : violating) {
				sb.append("<pre><code>" + x + "</code></pre>");
				System.out.println(x);
				print(Antlr4Utils.get(x), 0);
			}
		}

		if (!compliant.isEmpty()) {
			sb.append("<h3>Compliant</h3>");
			for (String x : compliant) {
				sb.append("<pre><code>" + x + "</code></pre>");
				System.out.println(x);
				print(Antlr4Utils.get(x), 0);
			}
		}
		if (r.getSource() != null && !r.getSource().isEmpty()) {
			sb.append("<h4>Source</h4>");
			sb.append(String.format("<a href='%s'>%s</a>", r.getSource(), r.getSource()));
		}
		r.setDescription(sb.toString());

		try {
			JAXBContext context = JAXBContext.newInstance(Rule.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To
			StringWriter sw = new StringWriter();
			m.marshal(r, sw);
			xmlString = sw.toString();

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return xmlString;
	}

	public static SqlRules[] read(String path) {
		CustomRulesProvider provider = new CustomRulesProvider();
		return provider.getRules(null, "", path).values().toArray(new SqlRules[0]);
	}

	public static SqlRules getCustomRules() {
		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("tsqlDemoRepo");
		customRules.setRepoName("Demo rules");

		customRules.getRule()
				.addAll(Arrays.asList(getSargRule(), getWaitForRule(), getSelectAllRule(), // getCursorRule(),
						getInsertRule(), getOrderByRule(), getNoLockRule(), getExecRule(), // getMultipleDeclarations(),
						// getSameFlow(),

						getSargRule()
		// getSchemaRule()
		));
		return customRules;
	}

	public static SqlRules getCustomMainRules() {
		SqlRules customRules = new SqlRules();
		customRules.setRepoKey(Constants.PLUGIN_REPO_KEY);
		customRules.setRepoName(Constants.PLUGIN_REPO_NAME);

		customRules.getRule().addAll(Arrays.asList(getWaitForRule(), getSelectAllRule(), getInsertRule(),
				getOrderByRule(), getExecRule(), getNoLockRule(), getSargRule(), getPKRule(), getFKRule()));
		return customRules;
	}

	public static Rule getWaitForRule() {
		Rule rule = new Rule();
		rule.setKey("C001");
		rule.setInternalKey("C001");
		rule.setDescription("WAITFOR is used.");
		rule.setName("WAITFOR is used");
		rule.setTag("performance");
		rule.setSeverity("MINOR");
		rule.setRemediationFunction("LINEAR");
		rule.setDebtRemediationFunctionCoefficient("2min");
		RuleImplementation impl = new RuleImplementation();
		impl.getNames().getTextItem().add(Waitfor_statementContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		impl.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		impl.setRuleViolationMessage("WAITFOR is used.");
		impl.getViolatingRulesCodeExamples().getRuleCodeExample().add("WAITFOR '10:00:00';");

		rule.setRuleImplementation(impl);
		return rule;
	}

	public static Rule getSelectAllRule() {
		Rule rule = new Rule();
		rule.setKey("C002");
		rule.setInternalKey("C002");
		rule.setName("SELECT * is used");
		rule.setDescription("<h2>Description</h2><p>SELECT * is used. Please list names.</p>");
		rule.setTag("design");
		rule.setSeverity("MINOR");
		rule.setRemediationFunction("LINEAR");
		rule.setDebtRemediationFunctionCoefficient("2min");
		RuleImplementation impl = new RuleImplementation();

		// impl.getChildrenRules().getRuleImplementation().add(child2);
		impl.getNames().getTextItem().add(Select_list_elemContext.class.getSimpleName());
		impl.getTextToFind().getTextItem().add("*");
		impl.setRuleViolationMessage("SELECT * was used");
		impl.setTextCheckType(TextCheckType.STRICT);
		impl.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		impl.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		impl.getViolatingRulesCodeExamples().getRuleCodeExample().add("SELECT * from dbo.test;");
		impl.getCompliantRulesCodeExamples().getRuleCodeExample().add("SELECT name, surname from dbo.test;");
		impl.getCompliantRulesCodeExamples().getRuleCodeExample().add("SELECT name, surname, 1 * 3 from dbo.test;");
		rule.setRuleImplementation(impl);

		return rule;
	}

	public static Rule getInsertRule() {
		Rule rule = new Rule();
		rule.setKey("C003");
		rule.setInternalKey("C003");
		rule.setName("INSERT statement without columns listed");
		rule.setDescription(
				"<h2>Description</h2><p>INSERT statement does not have columns listed. Always use a column list in your INSERT statements.</p>");
		rule.setTag("design");
		rule.setSeverity("MINOR");
		rule.setRemediationFunction("LINEAR");
		rule.setDebtRemediationFunctionCoefficient("2min");
		RuleImplementation child2 = new RuleImplementation();
		child2.getNames().getTextItem().add(Column_name_listContext.class.getSimpleName());
		child2.setTextCheckType(TextCheckType.DEFAULT);
		child2.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		child2.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		child2.setRuleViolationMessage("Column list is not specified in an insert statement.");

		RuleImplementation impl = new RuleImplementation();

		impl.getChildrenRules().getRuleImplementation().add(child2);
		impl.getNames().getTextItem().add(Insert_statementContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		impl.setRuleResultType(RuleResultType.DEFAULT);
		impl.setRuleViolationMessage("TESTT");
		impl.getViolatingRulesCodeExamples().getRuleCodeExample().add("INSERT INTO dbo.test VALUES (1,2);");
		impl.getCompliantRulesCodeExamples().getRuleCodeExample().add("INSERT INTO dbo.test (a,b) VALUES (1,2);");

		rule.setRuleImplementation(impl);

		return rule;
	}

	public static Rule getOrderByRule() {
		Rule rule = new Rule();
		rule.setKey("C004");
		rule.setInternalKey("C004");
		rule.setName("ORDER BY clause contains positional references");
		rule.setDescription(
				"<h2>Description</h2><p>Do not use column numbers in the ORDER BY clause. Always use column names in an order by clause. Avoid positional references.</p>");
		rule.setTag("design");
		rule.setSeverity("MINOR");
		rule.setRemediationFunction("LINEAR");
		rule.setDebtRemediationFunctionCoefficient("2min");
		RuleImplementation child2 = new RuleImplementation();
		child2.getNames().getTextItem().add(ConstantContext.class.getSimpleName());
		child2.setTextCheckType(TextCheckType.DEFAULT);
		child2.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		child2.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		child2.setRuleViolationMessage("Positional reference is used instead of column name in order by clause.");

		RuleImplementation impl = new RuleImplementation();

		impl.getChildrenRules().getRuleImplementation().add(child2);
		impl.getNames().getTextItem().add(Order_by_clauseContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		impl.setRuleResultType(RuleResultType.DEFAULT);
		impl.setRuleViolationMessage("");
		impl.getViolatingRulesCodeExamples().getRuleCodeExample().add("SELECT * from dbo.test order by 1;");
		impl.getCompliantRulesCodeExamples().getRuleCodeExample().add("SELECT * from dbo.test order by name;");

		rule.setRuleImplementation(impl);
		return rule;
	}

	public static Rule getSchemaRule() {
		Rule rule = new Rule();
		rule.setKey("C006");
		rule.setInternalKey("C006");
		rule.setName("Non schema qualified object name");
		rule.setDescription(
				"<h2>Description</h2><p>Always use schema-qualified object names to speed up resolution and improve query plan reuse.</p>");

		RuleImplementation parentQuery = new RuleImplementation();
		parentQuery.getNames().getTextItem().add(Query_expressionContext.class.getSimpleName());
		parentQuery.setRuleResultType(RuleResultType.DEFAULT);
		parentQuery.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		parentQuery.setRuleViolationMessage("TEST");

		RuleImplementation parentTableQuery = new RuleImplementation();
		parentTableQuery.getNames().getTextItem().add(Table_nameContext.class.getSimpleName());
		parentTableQuery.setRuleResultType(RuleResultType.DEFAULT);
		parentTableQuery.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		parentTableQuery.setRuleViolationMessage("parentTableQuery");

		RuleImplementation child2 = new RuleImplementation();
		child2.getNames().getTextItem().add(Simple_idContext.class.getSimpleName());
		child2.setTextCheckType(TextCheckType.DEFAULT);
		child2.setRuleResultType(RuleResultType.FAIL_IF_LESS_FOUND);
		child2.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		child2.setTimes(2);
		child2.setRuleViolationMessage("Always use schema-qualified object names");

		parentTableQuery.getChildrenRules().getRuleImplementation().add(child2);
		parentQuery.getChildrenRules().getRuleImplementation().add(parentTableQuery);
		RuleImplementation impl = new RuleImplementation();

		impl.getParentRules().getRuleImplementation().add(parentQuery);
		impl.getNames().getTextItem().add(Table_nameContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		impl.setRuleResultType(RuleResultType.DEFAULT);
		impl.setRuleViolationMessage("Always use schema-qualified object names");
		impl.getViolatingRulesCodeExamples().getRuleCodeExample().add("SELECT * from test order by 1;");
		impl.getViolatingRulesCodeExamples().getRuleCodeExample().add("SELECT OrderID, OrderDate FROM Orders");
		impl.getCompliantRulesCodeExamples().getRuleCodeExample().add("SELECT * from dbo.test order by name;");
		impl.getCompliantRulesCodeExamples().getRuleCodeExample().add("SELECT * from main.dbo.test order by name;");
		// impl.getCompliantRulesCodeExamples().getRuleCodeExample()
		// .add("with cte as(select a, b,c from dbo.test)\r\nSELECT a from cte;
		// select * from test.dbo");

		rule.setRuleImplementation(impl);
		rule.setSource("http://sqlmag.com/t-sql/t-sql-best-practices-part-1");
		return rule;
	}

	public static Rule getExecRule() {
		Rule rule = new Rule();
		rule.setKey("C005");
		rule.setInternalKey("C005");
		rule.setName("EXECUTE/EXEC for dynamic query is used");
		rule.setDescription(".");
		rule.setDescription(
				"<h2>Description</h2><p>EXECUTE/EXEC for dynamic query was used. It is better to use sp_executesql for dynamic queries.</p>");
		rule.setTag("best-practise");
		rule.setSeverity("MINOR");
		rule.setRemediationFunction("LINEAR");
		rule.setDebtRemediationFunctionCoefficient("2min");
		RuleImplementation child2 = new RuleImplementation();
		child2.getNames().getTextItem().add(Primitive_expressionContext.class.getSimpleName());
		child2.setTextCheckType(TextCheckType.DEFAULT);
		child2.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		child2.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		child2.setRuleViolationMessage(
				"EXECUTE/EXEC for dynamic query is used. It is better to use sp_executesql for dynamic queries.");

		RuleImplementation skipSubRule = new RuleImplementation();
		skipSubRule.getNames().getTextItem().add(Func_proc_nameContext.class.getSimpleName());
		skipSubRule.setTextCheckType(TextCheckType.DEFAULT);
		skipSubRule.setRuleResultType(RuleResultType.SKIP_IF_FOUND);
		skipSubRule.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		skipSubRule.setRuleViolationMessage(
				"EXECUTE/EXEC for dynamic query is used. It is better to use sp_executesql for dynamic queries.");

		RuleImplementation impl = new RuleImplementation();
		impl.getChildrenRules().getRuleImplementation().add(child2);
		impl.getChildrenRules().getRuleImplementation().add(skipSubRule);
		impl.getNames().getTextItem().add(Execute_statementContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		impl.setRuleResultType(RuleResultType.DEFAULT);
		impl.getViolatingRulesCodeExamples().getRuleCodeExample().add("EXEC ('SELECT 1');");
		impl.getViolatingRulesCodeExamples().getRuleCodeExample().add("EXEC (@sQueryText);");

		impl.getCompliantRulesCodeExamples().getRuleCodeExample().add("EXECUTE sp_executesql N'select 1';");
		impl.getCompliantRulesCodeExamples().getRuleCodeExample().add("exec sys.sp_test  @test = 'Publisher';");

		rule.setRuleImplementation(impl);

		return rule;
	}

	public static Rule getNoLockRule() {
		Rule rule = new Rule();
		rule.setKey("C007");
		rule.setInternalKey("C007");
		rule.setName("NOLOCK hint used");
		rule.setDescription("<h2>Description</h2><p>Use of NOLOCK might cause data inconsistency problems.</p>");
		rule.setTag("reliability");
		rule.setSeverity("MINOR");
		rule.setRemediationFunction("LINEAR");
		rule.setDebtRemediationFunctionCoefficient("2min");
		RuleImplementation impl = new RuleImplementation();

		impl.getTextToFind().getTextItem().add("NOLOCK");
		impl.setTextCheckType(TextCheckType.CONTAINS);
		impl.getNames().getTextItem().add(Table_hintContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		impl.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		impl.setRuleViolationMessage("NOLOCK hint was used");
		impl.getViolatingRulesCodeExamples().getRuleCodeExample()
				.add("SELECT name, surnam from dbo.test WITH (NOLOCK);");
		impl.getCompliantRulesCodeExamples().getRuleCodeExample().add("SELECT name, surnam from dbo.test;");
		rule.setSource("http://sqlmag.com/t-sql/t-sql-best-practices-part-1");
		rule.setRuleImplementation(impl);

		return rule;
	}

	public static Rule getCursorRule() {
		Rule rule = new Rule();
		rule.setKey("C006");
		rule.setInternalKey("C006");
		rule.setName("Cursor lifecycle is violated");
		rule.setDescription("Cursor lifecycle is violated. Cursor either is not opened, deallocated or closed.");

		RuleImplementation impl = new RuleImplementation();

		impl.getNames().getTextItem().add(Cursor_nameContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.DEFAULT);
		impl.setRuleResultType(RuleResultType.DEFAULT);
		impl.setRuleMode(RuleMode.GROUP);
		rule.setRuleImplementation(impl);

		RuleImplementation child = new RuleImplementation();
		child.getNames().getTextItem().add(Cursor_statementContext.class.getSimpleName());
		child.getTextToFind().getTextItem().add("OPEN");
		child.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		child.setRuleMatchType(RuleMatchType.FULL);
		child.setRuleViolationMessage("Cursor was not opened.");

		RuleImplementation childClose = new RuleImplementation();
		childClose.getNames().getTextItem().add(Cursor_statementContext.class.getSimpleName());
		childClose.getTextToFind().getTextItem().add("CLOSE");
		childClose.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		childClose.setRuleMatchType(RuleMatchType.FULL);
		childClose.setRuleViolationMessage("Cursor was not closed.");

		RuleImplementation childDeallocate = new RuleImplementation();
		childDeallocate.getNames().getTextItem().add(Cursor_statementContext.class.getSimpleName());
		childDeallocate.getTextToFind().getTextItem().add("DEALLOCATE");
		childDeallocate.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		childDeallocate.setRuleMatchType(RuleMatchType.FULL);
		childDeallocate.setRuleViolationMessage("Cursor was not deallocated.");

		RuleImplementation child2 = new RuleImplementation();
		child2.getNames().getTextItem().add(Cursor_statementContext.class.getSimpleName());
		child2.getTextToFind().getTextItem().add("DECLARE");
		child2.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		child2.setRuleMatchType(RuleMatchType.FULL);
		child2.setRuleViolationMessage("Cursor was not declared.");

		impl.getUsesRules().getRuleImplementation().add(child);
		impl.getUsesRules().getRuleImplementation().add(child2);
		impl.getUsesRules().getRuleImplementation().add(childClose);
		impl.getUsesRules().getRuleImplementation().add(childDeallocate);

		impl.getViolatingRulesCodeExamples().getRuleCodeExample().add(
				"DECLARE vend_cursor CURSOR      FOR SELECT * FROM Purchasing.Vendor; OPEN vend_cursor; FETCH NEXT FROM vend_cursor; SELECT * FROM Purchasing.Vendor; DEALLOCATE vendor_cursor; ");
		impl.getCompliantRulesCodeExamples().getRuleCodeExample().add(
				"DECLARE vend_cursor CURSOR      FOR SELECT * FROM Purchasing.Vendor; OPEN vend_cursor; FETCH NEXT FROM vend_cursor; SELECT * FROM Purchasing.Vendor; CLOSE vend_cursor; DEALLOCATE vend_cursor; ");

		return rule;
	}

	public static Rule getMultipleDeclarations() {
		Rule rule = new Rule();
		rule.setKey("C007");
		rule.setInternalKey("C007");
		rule.setName("Multiple cursor declarations found");
		rule.setDescription("Multiple cursor declarations found");

		RuleImplementation child2 = new RuleImplementation();
		child2.getNames().getTextItem().add(Cursor_statementContext.class.getSimpleName());
		child2.getTextToFind().getTextItem().add("DECLARE");
		child2.setRuleResultType(RuleResultType.FAIL_IF_MORE_FOUND);
		child2.setTimes(1);
		child2.setRuleMatchType(RuleMatchType.FULL);
		child2.setRuleViolationMessage("Cursor was declared multiple times.");

		RuleImplementation parent = new RuleImplementation();
		parent.getNames().getTextItem().add(Cursor_nameContext.class.getSimpleName());
		parent.setTextCheckType(TextCheckType.DEFAULT);
		parent.setRuleResultType(RuleResultType.DEFAULT);
		parent.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		parent.setRuleMode(RuleMode.GROUP);

		parent.getUsesRules().getRuleImplementation().add(child2);
		parent.getViolatingRulesCodeExamples().getRuleCodeExample().add(
				"DECLARE vend_cursor CURSOR      FOR SELECT * FROM Purchasing.Vendor;DECLARE vend_cursor CURSOR      FOR SELECT * FROM Purchasing.Vendor; OPEN vend_cursor; BEGIN FETCH NEXT FROM vend_cursor; END SELECT * FROM Purchasing.Vendor; BEGIN CLOSE vend_cursor; END DEALLOCATE vend_cursor; ");
		parent.getCompliantRulesCodeExamples().getRuleCodeExample().add(
				"DECLARE vend_cursor CURSOR      FOR SELECT * FROM Purchasing.Vendor; OPEN vend_cursor; FETCH NEXT FROM vend_cursor; SELECT * FROM Purchasing.Vendor; CLOSE vend_cursor; DEALLOCATE vend_cursor; ");

		rule.setRuleImplementation(parent);

		return rule;
	}

	public static Rule getSameFlow() {
		Rule rule = new Rule();
		rule.setKey("C008");
		rule.setInternalKey("C008");
		rule.setName("Cursor was closed in a different control statement");
		rule.setDescription("Cursor was closed or deallocated in a different control statement than declared.");

		RuleImplementation child2 = new RuleImplementation();
		child2.getNames().getTextItem().add(Cursor_statementContext.class.getSimpleName());
		child2.getTextToFind().getTextItem().add("CLOSE");
		child2.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		child2.setRuleMatchType(RuleMatchType.STRICT);
		child2.setRuleViolationMessage("Cursor was closed in a different control statement.");

		RuleImplementation child3 = new RuleImplementation();
		child3.getNames().getTextItem().add(Cursor_statementContext.class.getSimpleName());
		child3.getTextToFind().getTextItem().add("DEALLOCATE");
		child3.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		child3.setRuleMatchType(RuleMatchType.STRICT);
		child3.setRuleViolationMessage("Cursor was deallocated in a different control statement.");

		RuleImplementation parent0 = new RuleImplementation();
		parent0.getNames().getTextItem().add(Declare_cursorContext.class.getSimpleName());
		parent0.setRuleResultType(RuleResultType.DEFAULT);
		parent0.setRuleMatchType(RuleMatchType.FULL);

		RuleImplementation parent = new RuleImplementation();
		parent.getNames().getTextItem().add(Cursor_nameContext.class.getSimpleName());
		parent.setTextCheckType(TextCheckType.DEFAULT);
		parent.setRuleResultType(RuleResultType.DEFAULT);
		parent.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		parent.setRuleMode(RuleMode.GROUP);

		parent.getUsesRules().getRuleImplementation().add(child2);
		parent.getUsesRules().getRuleImplementation().add(child3);

		parent.getUsesRules().getRuleImplementation().add(parent0);
		parent.getViolatingRulesCodeExamples().getRuleCodeExample().add(
				"DECLARE vend_cursor CURSOR      FOR SELECT * FROM Purchasing.Vendor; OPEN vend_cursor; BEGIN FETCH NEXT FROM vend_cursor; END SELECT * FROM Purchasing.Vendor; BEGIN CLOSE vend_cursor; END DEALLOCATE vend_cursor; ");
		parent.getCompliantRulesCodeExamples().getRuleCodeExample().add(
				"DECLARE vend_cursor CURSOR      FOR SELECT * FROM Purchasing.Vendor; OPEN vend_cursor; FETCH NEXT FROM vend_cursor; SELECT * FROM Purchasing.Vendor; CLOSE vend_cursor; DEALLOCATE vend_cursor; ");

		rule.setRuleImplementation(parent);

		return rule;
	}

	public static Rule getPKRule() {
		RuleImplementation ruleImpl = new RuleImplementation();
		ruleImpl.getNames().getTextItem().add(Table_constraintContext.class.getSimpleName());
		ruleImpl.setRuleMatchType(RuleMatchType.CLASS_ONLY);

		RuleImplementation child1 = new RuleImplementation();
		child1.getTextToFind().getTextItem().add("PRIMARY");
		child1.getNames().getTextItem().add(TerminalNodeImpl.class.getSimpleName());
		child1.setRuleResultType(RuleResultType.SKIP_IF_NOT_FOUND);
		child1.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);

		RuleImplementation child2 = new RuleImplementation();
		child2.getTextToFind().getTextItem().add("KEY");
		child2.getNames().getTextItem().add(TerminalNodeImpl.class.getSimpleName());
		child2.setRuleResultType(RuleResultType.SKIP_IF_NOT_FOUND);
		child2.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		ruleImpl.getChildrenRules().getRuleImplementation().add(child1);
		ruleImpl.getChildrenRules().getRuleImplementation().add(child2);
		ruleImpl.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);

		RuleImplementation main = new RuleImplementation();
		main.getNames().getTextItem().add(IdContext.class.getSimpleName());
		main.setTextCheckType(TextCheckType.CONTAINS);
		main.getTextToFind().getTextItem().add("PK_");
		main.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		main.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		main.setRuleViolationMessage("Primary key is not defined using suggested naming convention to start with PK_");
		main.setDistanceCheckType(RuleDistanceIndexMatchType.EQUALS);

		ruleImpl.getChildrenRules().getRuleImplementation().add(main);
		ruleImpl.getViolatingRulesCodeExamples().getRuleCodeExample().add(
				"CREATE TABLE dbo.Orders\r\n(\r\nId int NOT NULL,\r\nCONSTRAINT OrderID PRIMARY KEY CLUSTERED (Id) \r\n);  ");
		ruleImpl.getCompliantRulesCodeExamples().getRuleCodeExample()
				.add("ALTER TABLE dbo.Orders ADD CONSTRAINT PK_OrderId PRIMARY KEY CLUSTERED (Id);");
		ruleImpl.getViolatingRulesCodeExamples().getRuleCodeExample()
				.add("CREATE TABLE dbo.Orders\r\n(\r\nId int NOT NULL,  \r\nPRIMARY KEY (Id)\r\n);  ");

		Rule rule = new Rule();
		rule.setKey("C010");
		rule.setInternalKey("C010");
		rule.setStatus("BETA");
		rule.setName("Defined primary key is not using recommended naming convention");
		rule.setDescription(
				"<h2>Description</h2><p>Defined primary key is not using recommended naming convention to start with PK_.</p>");
		rule.setTag("naming");
		rule.setSeverity("MINOR");
		rule.setRemediationFunction("LINEAR");
		rule.setDebtRemediationFunctionCoefficient("3min");
		rule.setRuleImplementation(ruleImpl);

		return rule;
	}

	public static Rule getFKRule() {
		RuleImplementation ruleImpl = new RuleImplementation();
		ruleImpl.getNames().getTextItem().add(Table_constraintContext.class.getSimpleName());
		ruleImpl.setRuleMatchType(RuleMatchType.CLASS_ONLY);

		RuleImplementation child1 = new RuleImplementation();
		child1.getTextToFind().getTextItem().add("FOREIGN");
		child1.getNames().getTextItem().add(TerminalNodeImpl.class.getSimpleName());
		child1.setRuleResultType(RuleResultType.SKIP_IF_NOT_FOUND);
		child1.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);

		RuleImplementation child2 = new RuleImplementation();
		child2.getTextToFind().getTextItem().add("KEY");
		child2.getNames().getTextItem().add(TerminalNodeImpl.class.getSimpleName());
		child2.setRuleResultType(RuleResultType.SKIP_IF_NOT_FOUND);
		child2.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		ruleImpl.getChildrenRules().getRuleImplementation().add(child1);
		ruleImpl.getChildrenRules().getRuleImplementation().add(child2);
		ruleImpl.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);

		RuleImplementation main = new RuleImplementation();
		main.getNames().getTextItem().add(IdContext.class.getSimpleName());
		main.setTextCheckType(TextCheckType.CONTAINS);
		main.getTextToFind().getTextItem().add("FK_");
		main.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		main.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		main.setRuleViolationMessage("Foreign key is not defined using suggested naming convention to start with FK_");
		main.setDistanceCheckType(RuleDistanceIndexMatchType.EQUALS);

		ruleImpl.getChildrenRules().getRuleImplementation().add(main);
		ruleImpl.getViolatingRulesCodeExamples().getRuleCodeExample().add(
				"ALTER TABLE dbo.Orders ADD CONSTRAINT ClientId FOREIGN KEY (ClientId) REFERENCES dbo.Clients(Id);  ");
		ruleImpl.getCompliantRulesCodeExamples().getRuleCodeExample().add(
				"ALTER TABLE dbo.Orders ADD CONSTRAINT FK_ClientId FOREIGN KEY (ClientId) REFERENCES dbo.Clients(Id); ");

		Rule rule = new Rule();
		rule.setKey("C011");
		rule.setInternalKey("C011");
		rule.setStatus("BETA");
		rule.setName("Defined foreign key is not using recommended naming convention");
		rule.setDescription(
				"<h2>Description</h2><p>Defined foreign key is not using recommended naming convention to start with FK_.</p>");
		rule.setTag("naming");
		rule.setSeverity("MINOR");
		rule.setRemediationFunction("LINEAR");
		rule.setDebtRemediationFunctionCoefficient("3min");
		rule.setRuleImplementation(ruleImpl);

		return rule;
	}

	public static Rule getSargRule() {
		Rule rule = new Rule();
		rule.setKey("C009");
		rule.setInternalKey("C009");
		rule.setStatus("BETA");
		rule.setName("Non-sargable statement used");
		rule.setDescription(
				"<h2>Description</h2><p>Use of non-sargeable arguments might cause performance problems.</p>");
		rule.setTag("performance");
		rule.setSeverity("MAJOR");
		rule.setRemediationFunction("LINEAR");
		rule.setDebtRemediationFunctionCoefficient("5min");
		RuleImplementation functionCallContainsColRef = new RuleImplementation();
		functionCallContainsColRef.getNames().getTextItem().add(Full_column_nameContext.class.getSimpleName());
		functionCallContainsColRef.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		functionCallContainsColRef.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		functionCallContainsColRef
				.setRuleViolationMessage("Non-sargeable argument found - column referenced in a function");

		RuleImplementation ruleFunctionCall = new RuleImplementation();
		ruleFunctionCall.getNames().getTextItem().add(SCALAR_FUNCTIONContext.class.getSimpleName());
		ruleFunctionCall.getNames().getTextItem().add(Function_callContext.class.getSimpleName());
		ruleFunctionCall.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		ruleFunctionCall.setRuleResultType(RuleResultType.DEFAULT);
		ruleFunctionCall.getChildrenRules().getRuleImplementation().add(functionCallContainsColRef);

		RuleImplementation predicateContextContainsLike = new RuleImplementation();
		predicateContextContainsLike.getTextToFind().getTextItem().add("LIKE");
		predicateContextContainsLike.setTextCheckType(TextCheckType.CONTAINS);
		predicateContextContainsLike.getNames().getTextItem().add(PredicateContext.class.getSimpleName());
		predicateContextContainsLike.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		predicateContextContainsLike.setRuleResultType(RuleResultType.DEFAULT);

		RuleImplementation functionCallContainsLikeWildcard = new RuleImplementation();
		functionCallContainsLikeWildcard.getTextToFind().getTextItem().add("N?[',‘]%(.*?)'");
		functionCallContainsLikeWildcard.setTextCheckType(TextCheckType.REGEXP);
		functionCallContainsLikeWildcard.getNames().getTextItem().add(TerminalNodeImpl.class.getSimpleName());
		functionCallContainsLikeWildcard.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		functionCallContainsLikeWildcard.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		functionCallContainsLikeWildcard
				.setRuleViolationMessage("Non-sargeable argument found - predicate starts with wildcard");

		predicateContextContainsLike.getChildrenRules().getRuleImplementation().add(functionCallContainsLikeWildcard);

		RuleImplementation impl = new RuleImplementation();
		impl.getChildrenRules().getRuleImplementation().add(ruleFunctionCall);
		impl.getChildrenRules().getRuleImplementation().add(predicateContextContainsLike);

		impl.getNames().getTextItem().add(Search_conditionContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		impl.setRuleResultType(RuleResultType.DEFAULT);
		impl.getCompliantRulesCodeExamples().getRuleCodeExample()
				.add("SELECT MAX(RateChangeDate)  FROM HumanResources.EmployeePayHistory WHERE BusinessEntityID = 1");
		impl.getViolatingRulesCodeExamples().getRuleCodeExample()
				.add("SELECT name, surname from dbo.test where year(date) > 2008");
		impl.getViolatingRulesCodeExamples().getRuleCodeExample()
				.add("SELECT name, surname from dbo.test where name like '%red' ");
		impl.getCompliantRulesCodeExamples().getRuleCodeExample()
				.add("SELECT name, surname from dbo.test where date between 2008-10-10 and 2010-10-10;");
		// impl.getCompliantRulesCodeExamples().getRuleCodeExample()
		// .add("IF @LanguageName LIKE N'%Chinese%' SET @LanguageName =
		// N'Chinese';");

		//
		rule.setSource("http://sqlmag.com/t-sql/t-sql-best-practices-part-1");
		rule.setRuleImplementation(impl);

		return rule;
	}

	public static Rule getDeclareRule() {
		Rule r = new Rule();
		r.setInternalKey("C011");
		r.setKey("C011");
		r.setStatus("BETA");
		r.setName("Variable was declared, but not set");
		r.setDescription("<h2>Description</h2><p>Variable was declared, but not set.</p>");

		RuleImplementation useRule = new RuleImplementation();
		useRule.getNames().getTextItem().add(Set_statementContext.class.getSimpleName());
		useRule.setRuleMatchType(RuleMatchType.FULL);
		useRule.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		useRule.setRuleViolationMessage("Item was not set");

		RuleImplementation parentRule = new RuleImplementation();
		parentRule.getNames().getTextItem().add(Declare_statementContext.class.getSimpleName());
		parentRule.setRuleMatchType(RuleMatchType.FULL);
		parentRule.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		parentRule.setRuleViolationMessage("Item was not declared");

		RuleImplementation childRule = new RuleImplementation();
		childRule.getNames().getTextItem().add("Data_typeContext");
		childRule.setRuleMatchType(RuleMatchType.CLASS_ONLY);

		RuleImplementation sibRule = new RuleImplementation();
		sibRule.getNames().getTextItem().add(Primitive_expressionContext.class.getSimpleName());

		sibRule.setRuleResultType(RuleResultType.SKIP_IF_FOUND);
		childRule.getSiblingsRules().getRuleImplementation().add(sibRule);

		parentRule.getChildrenRules().getRuleImplementation().add(childRule);
		RuleImplementation impl = new RuleImplementation();
		impl.getUsesRules().getRuleImplementation().add(useRule);
		impl.getUsesRules().getRuleImplementation().add(parentRule);
		impl.setTextCheckType(TextCheckType.REGEXP);
		impl.getTextToFind().getTextItem().add("@(.*?)");
		impl.getNames().getTextItem().add("TerminalNodeImpl");
		impl.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		impl.setRuleMode(RuleMode.GROUP);
		impl.getCompliantRulesCodeExamples().getRuleCodeExample()
				.add("DECLARE @Group nvarchar(50); Set @Group = 'test';");
		impl.getViolatingRulesCodeExamples().getRuleCodeExample().add("DECLARE @Group nvarchar(50);");
		impl.setRuleViolationMessage("Variable was declared, but not set");
		r.setRuleImplementation(impl);
		return r;
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {

		//FileUtils.write(new File("src/main/resources/rules/plugin-rules.xml"), ruleToString(getCustomMainRules()));
		SqlRules rules = getCustomMainRules();
		for (Rule r : rules.getRule()) {
			System.out.println(r.getKey() + " - " + r.getName());
		}
		System.out.println();
		for (Rule r : rules.getRule()) {
			System.out.println(r.getKey() + " - " + r.getName());
			ruleImplToString(r);
		}

	}
}
