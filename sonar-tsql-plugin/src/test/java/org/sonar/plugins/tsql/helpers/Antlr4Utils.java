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
import org.apache.commons.lang3.StringUtils;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.antlr4.tsqlLexer;
import org.sonar.plugins.tsql.antlr4.tsqlParser;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Column_name_listContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.ConstantContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Cursor_nameContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Cursor_statementContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Declare_cursorContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Execute_statementContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Func_proc_nameContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Insert_statementContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Order_by_clauseContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Primitive_expressionContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Select_listContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Select_list_elemContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Tsql_fileContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Waitfor_statementContext;
import org.sonar.plugins.tsql.rules.custom.CustomRules;
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
		int endChar = charStart+firstToken.getText().length();
		
	
		String data = "@("+line+":"+charStart+","+endLine+":"+endChar+") with text: "+firstToken.getText();
		final int tmp = level + 1;
		final StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.repeat("\t", level));
		sb.append(node.getClass().getSimpleName() + ": "+data+" :" + node.getText());
		System.out.println(sb.toString());
		final int n = node.getChildCount();
		for (int i = 0; i < n; i++) {

			final ParseTree c = node.getChild(i);
			print(c, tmp, stream);

		}
	}
	
	public static boolean verify(Rule rule, String text) {
		AntrlResult result = Antlr4Utils.getFull(text);
		CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream());
		ParseTree root = result.getTree();
		CustomRules customRules = new CustomRules();
		customRules.setRepoKey("test");
		customRules.setRepoName("test");
		customRules.getRule().add(rule);
		TsqlIssue[] issues = provider.getIssues(root, customRules);
		return issues.length == 0;
	}
	public static TsqlIssue[] verify2(Rule rule, String text) {
		AntrlResult result = Antlr4Utils.getFull(text);
		CustomRulesViolationsProvider provider = new CustomRulesViolationsProvider(result.getStream());
		ParseTree root = result.getTree();
		CustomRules customRules = new CustomRules();
		customRules.setRepoKey("test");
		customRules.setRepoName("test");
		customRules.getRule().add(rule);
		TsqlIssue[] issues = provider.getIssues(root, customRules);
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

	public static String ruleToString(CustomRules customRules) {

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
					sb.append("<code>" + x + "</code>");
				}
			}

			if (!compliant.isEmpty()) {
				sb.append("<h3>Compliant</h3>");
				for (String x : compliant) {
					sb.append("<code>" + x + "</code>");
				}
			}
			r.setDescription(sb.toString());

		}
		String xmlString = "";
		try {
			JAXBContext context = JAXBContext.newInstance(CustomRules.class);
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

	public static CustomRules[] read(String path) {
		CustomRulesProvider provider = new CustomRulesProvider();
		return provider.getRules(null, "", path).values().toArray(new CustomRules[0]);
	}

	public static CustomRules getCustomRules() {
		CustomRules customRules = new CustomRules();
		customRules.setRepoKey("tsqlDemoRepo");
		customRules.setRepoName("Demo rules");

		customRules.getRule().addAll(Arrays.asList(getWaitForRule(), getSelectAllRule(), getCursorRule(),
				getInsertRule(), getOrderByRule(), getExecRule(), getMultipleDeclarations(), getSameFlow()));
		return customRules;
	}
	public static CustomRules getCustomMainRules() {
		CustomRules customRules = new CustomRules();
		customRules.setRepoKey(Constants.PLUGIN_REPO_KEY);
		customRules.setRepoName(Constants.PLUGIN_REPO_NAME);

		customRules.getRule().addAll(Arrays.asList(getWaitForRule(), getSelectAllRule(), 
				getInsertRule(), getOrderByRule(), getExecRule()));
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

		RuleImplementation child2 = new RuleImplementation();
		child2.getNames().getTextItem().add(Select_list_elemContext.class.getSimpleName());
		child2.getTextToFind().getTextItem().add("*");
		child2.setTextCheckType(TextCheckType.STRICT);
		child2.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		child2.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		child2.setRuleViolationMessage("SELECT * is used.");

		RuleImplementation impl = new RuleImplementation();

		impl.getChildrenRules().getRuleImplementation().add(child2);
		impl.getNames().getTextItem().add(Select_listContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.DEFAULT);
		impl.setRuleResultType(RuleResultType.DEFAULT);
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
		rule.setDescription("<h2>Description</h2><p>INSERT statement does not have columns listed. Always use a column list in your INSERT statements.</p>");
		RuleImplementation child2 = new RuleImplementation();
		child2.getNames().getTextItem().add(Column_name_listContext.class.getSimpleName());
		child2.setTextCheckType(TextCheckType.DEFAULT);
		child2.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		child2.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		child2.setRuleViolationMessage("Column list is not specified in an insert statement.");

		RuleImplementation impl = new RuleImplementation();

		impl.getChildrenRules().getRuleImplementation().add(child2);
		impl.getNames().getTextItem().add(Insert_statementContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.DEFAULT);
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
		impl.setRuleMatchType(RuleMatchType.DEFAULT);
		impl.setRuleResultType(RuleResultType.DEFAULT);
		impl.setRuleViolationMessage("");
		impl.getViolatingRulesCodeExamples().getRuleCodeExample().add("SELECT * from dbo.test order by 1;");
		impl.getCompliantRulesCodeExamples().getRuleCodeExample().add("SELECT * from dbo.test order by name;");

		rule.setRuleImplementation(impl);
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
		skipSubRule.setRuleViolationMessage("EXECUTE/EXEC for dynamic query is used. It is better to use sp_executesql for dynamic queries.");
	

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
	
		impl.getViolatingRulesCodeExamples().getRuleCodeExample().add("DECLARE vend_cursor CURSOR      FOR SELECT * FROM Purchasing.Vendor; OPEN vend_cursor; FETCH NEXT FROM vend_cursor; SELECT * FROM Purchasing.Vendor; DEALLOCATE vendor_cursor; ");
		impl.getCompliantRulesCodeExamples().getRuleCodeExample().add("DECLARE vend_cursor CURSOR      FOR SELECT * FROM Purchasing.Vendor; OPEN vend_cursor; FETCH NEXT FROM vend_cursor; SELECT * FROM Purchasing.Vendor; CLOSE vend_cursor; DEALLOCATE vend_cursor; ");
		
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
		parent.setRuleMatchType(RuleMatchType.DEFAULT);
		parent.setRuleMode(RuleMode.GROUP);

		parent.getUsesRules().getRuleImplementation().add(child2);
		parent.getViolatingRulesCodeExamples().getRuleCodeExample().add("DECLARE vend_cursor CURSOR      FOR SELECT * FROM Purchasing.Vendor;DECLARE vend_cursor CURSOR      FOR SELECT * FROM Purchasing.Vendor; OPEN vend_cursor; BEGIN FETCH NEXT FROM vend_cursor; END SELECT * FROM Purchasing.Vendor; BEGIN CLOSE vend_cursor; END DEALLOCATE vend_cursor; ");
		parent.getCompliantRulesCodeExamples().getRuleCodeExample().add("DECLARE vend_cursor CURSOR      FOR SELECT * FROM Purchasing.Vendor; OPEN vend_cursor; FETCH NEXT FROM vend_cursor; SELECT * FROM Purchasing.Vendor; CLOSE vend_cursor; DEALLOCATE vend_cursor; ");

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
		parent.setRuleMatchType(RuleMatchType.DEFAULT);
		parent.setRuleMode(RuleMode.GROUP);

		parent.getUsesRules().getRuleImplementation().add(child2);
		parent.getUsesRules().getRuleImplementation().add(child3);
		
		parent.getUsesRules().getRuleImplementation().add(parent0);
		parent.getViolatingRulesCodeExamples().getRuleCodeExample().add("DECLARE vend_cursor CURSOR      FOR SELECT * FROM Purchasing.Vendor; OPEN vend_cursor; BEGIN FETCH NEXT FROM vend_cursor; END SELECT * FROM Purchasing.Vendor; BEGIN CLOSE vend_cursor; END DEALLOCATE vend_cursor; ");
			parent.getCompliantRulesCodeExamples().getRuleCodeExample().add("DECLARE vend_cursor CURSOR      FOR SELECT * FROM Purchasing.Vendor; OPEN vend_cursor; FETCH NEXT FROM vend_cursor; SELECT * FROM Purchasing.Vendor; CLOSE vend_cursor; DEALLOCATE vend_cursor; ");
	
		rule.setRuleImplementation(parent);
		
		return rule;
	}

	public static void main(String[] args) {

		System.out.println(ruleToString(getCustomMainRules()));

	}
}
