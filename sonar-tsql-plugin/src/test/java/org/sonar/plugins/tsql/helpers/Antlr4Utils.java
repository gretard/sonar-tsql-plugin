package org.sonar.plugins.tsql.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.apache.commons.lang3.StringUtils;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.antlr4.tsqlLexer;
import org.sonar.plugins.tsql.antlr4.tsqlParser;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Column_name_listContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Column_ref_expressionContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Common_table_expressionContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.ConstantContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Cursor_nameContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Cursor_statementContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Declare_cursorContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Execute_statementContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Func_proc_nameContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Function_callContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.IdContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Insert_statementContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Order_by_clauseContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.PredicateContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Primitive_expressionContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Query_expressionContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Search_conditionContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Select_listContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Select_list_elemContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Select_statementContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Simple_idContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Table_hintContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Table_nameContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Table_name_with_hintContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Table_sourceContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Tsql_fileContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Waitfor_statementContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.With_table_hintsContext;
import org.sonar.plugins.tsql.rules.custom.SqlRules;
import org.sonar.plugins.tsql.rules.custom.Rule;
import org.sonar.plugins.tsql.rules.custom.RuleImplementation;
import org.sonar.plugins.tsql.rules.custom.RuleMatchType;
import org.sonar.plugins.tsql.rules.custom.RuleMode;
import org.sonar.plugins.tsql.rules.custom.RuleResultType;
import org.sonar.plugins.tsql.rules.custom.TextCheckType;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.sensors.custom.CustomRulesProvider;
import org.sonar.plugins.tsql.sensors.custom.CustomRulesViolationsProvider;

public class Antlr4Utils {
	public static ParseTree get(String text) {
		final CharStream charStream = CharStreams.fromString(text);
		final tsqlLexer lexer = new tsqlLexer(charStream);
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		final tsqlParser parser = new tsqlParser(tokens);
		final Tsql_fileContext tree = parser.tsql_file();
		return tree;
	}

	public static void print(final ParseTree node, final int level) {
		final int tmp = level + 1;
		final StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.repeat("\t", level));
		sb.append(node.getClass().getSimpleName() + ": " + node.getText());
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

	public static boolean verify(Rule rule, String text) {
		AntrlResult result = Antlr4Utils.getFull(text);
		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("test");
		customRules.setRepoName("test");
		customRules.getRule().add(rule);
		CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream(), customRules);
		ParseTree root = result.getTree();

		TsqlIssue[] issues = provider.getIssues(root);
		return issues.length == 0;
	}

	public static TsqlIssue[] verify2(Rule rule, String text) {
		AntrlResult result = Antlr4Utils.getFull(text);
		SqlRules customRules = new SqlRules();
		customRules.setRepoKey("test");
		customRules.setRepoName("test");
		customRules.getRule().add(rule);
		CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream(), customRules);
		ParseTree root = result.getTree();

		TsqlIssue[] issues = provider.getIssues(root);
		return issues;
	}

	public static AntrlResult getFull(String text) {
		final CharStream charStream = CharStreams.fromString(text);
		final tsqlLexer lexer = new tsqlLexer(charStream);
		lexer.removeErrorListeners();
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		final tsqlParser parser = new tsqlParser(tokens);
		parser.removeErrorListeners();
		final Tsql_fileContext tree = parser.tsql_file();
		AntrlResult result = new AntrlResult();
		result.setStream(tokens);
		result.setTree(tree);
		return result;
	}

	public static AntrlResult getFull(InputStream stream) throws IOException {
		final CharStream charStream = CharStreams.fromStream(stream);
		final tsqlLexer lexer = new tsqlLexer(charStream);
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		final tsqlParser parser = new tsqlParser(tokens);
		final Tsql_fileContext tree = parser.tsql_file();
		AntrlResult result = new AntrlResult();
		result.setStream(tokens);
		result.setTree(tree);
		return result;
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
				getOrderByRule(), getExecRule(), getNoLockRule(), getSchemaRule(), getSargRule()));
		return customRules;
	}

	public static Rule getWaitForRule() {
		Rule rule = new Rule();
		rule.setKey("C001");
		rule.setInternalKey("C001");
		rule.setDescription("WAITFOR is used.");
		rule.setName("WAITFOR is used");
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

		RuleImplementation child2 = new RuleImplementation();
		child2.getNames().getTextItem().add(ConstantContext.class.getSimpleName());
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

	public static Rule getSargRule() {
		Rule rule = new Rule();
		rule.setKey("C009");
		rule.setInternalKey("C009");
		rule.setStatus("BETA");
		rule.setName("Non-sargable statement used");
		rule.setDescription(
				"<h2>Description</h2><p>Use of non-sargeable arguments might cause performance problems.</p>");

		RuleImplementation functionCallContainsColRef = new RuleImplementation();
		functionCallContainsColRef.getNames().getTextItem().add(Column_ref_expressionContext.class.getSimpleName());
		functionCallContainsColRef.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		functionCallContainsColRef.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		functionCallContainsColRef
				.setRuleViolationMessage("Non-sargeable argument found - column referenced in a function");

		RuleImplementation ruleFunctionCall = new RuleImplementation();
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

	public static void main(String[] args) {

		System.out.println(ruleToString(getCustomMainRules()));

		SqlRules rules = getCustomMainRules();
		for (Rule r : rules.getRule()) {
			System.out.println(r.getKey() + " - " + r.getName());
		}

	}
}
