package org.sonar.plugins.tsql.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sonar.plugins.tsql.antlr4.tsqlLexer;
import org.sonar.plugins.tsql.antlr4.tsqlParser;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Column_name_listContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.ConstantContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Cursor_nameContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Cursor_statementContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Declare_cursorContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Execute_statementContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Insert_statementContext;
import org.sonar.plugins.tsql.antlr4.tsqlParser.Order_by_clauseContext;
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

	public static AntrlResult getFull(String text) {
		final CharStream charStream = CharStreams.fromString(text);
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
	public static String ruleToString(Rule... rules) {

		String xmlString = "";
		try {
			CustomRules customRules = new CustomRules();
			customRules.setRepoKey("tsqlDemoRepo");
			customRules.setRepoName("Demo rules");
			
			customRules.getRule().addAll(Arrays.asList(rules));
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

	public static Rule getWaitForRule() {
		Rule rule = new Rule();
		rule.setKey("C001");
		rule.setInternalKey("C001");
		rule.setDescription("Waitfor is used");
		rule.setName("Waitfor is used.");
		RuleImplementation impl = new RuleImplementation();
		impl.getNames().getTextItem().add(Waitfor_statementContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		impl.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		impl.setRuleViolationMessage("Waitfor is used.");
		rule.setRuleImplementation(impl);
		return rule;
	}
	
	public static Rule getSelectAllRule() {
		Rule rule = new Rule();
		rule.setKey("C002");
		rule.setInternalKey("C002");
		rule.setName("SELECT * is used");
		rule.setDescription("<h2>Description</h2><p>SELECT * is used. Please list names.</p><h2>Examples</h2><h3>Non-compliant</h3><code>SELECT * from dbo.test;</code><h3>Compliant</h3><code>SELECT name from dbo.test;</code>");

		RuleImplementation child2 = new RuleImplementation();
		child2.getNames().getTextItem().add(Select_list_elemContext.class.getSimpleName());
		child2.getTextToFind().getTextItem().add("*");
		child2.setTextCheckType(TextCheckType.STRICT);
		child2.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		child2.setRuleMatchType(RuleMatchType.TEXT_AND_CLASS);
		child2.setRuleViolationMessage("SELECT * is used");

		RuleImplementation impl = new RuleImplementation();

		impl.getChildrenRules().getRuleImplementation().add(child2);
		impl.getNames().getTextItem().add(Select_listContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.DEFAULT);
		impl.setRuleResultType(RuleResultType.DEFAULT);
		rule.setRuleImplementation(impl);

		return rule;
	}

	public static Rule getCursorRule() {
		Rule rule = new Rule();
		rule.setKey("C003");
		rule.setInternalKey("C003");
		rule.setName("Cursor lifecycle is violated");
		rule.setDescription("Cursor lifecycle is violated. Cursor either is not opened, deallocated or closed.");

		RuleImplementation impl = new RuleImplementation();

		impl.getNames().getTextItem().add(Cursor_nameContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.DEFAULT);
		impl.setRuleResultType(RuleResultType.DEFAULT);
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
		impl.setRuleMode(RuleMode.GROUP);

		return rule;
	}

	public static Rule getInsertRule() {
		Rule rule = new Rule();
		rule.setKey("C004");
		rule.setInternalKey("C004");
		rule.setName("INSERT statement does not have columns listed");
		rule.setDescription("<h2>Description</h2><p>Always use a column list in your INSERT statements.</p><h2>Examples</h2><h3>Non-compliant</h3><code>INSERT INTO dbo.test VALUES (1,2)</code><h3>Compliant</h3><code>INSERT INTO dbo.test (a,b) VALUES (1,2)</code>");
		RuleImplementation child2 = new RuleImplementation();
		child2.getNames().getTextItem().add(Column_name_listContext.class.getSimpleName());
		child2.setTextCheckType(TextCheckType.DEFAULT);
		child2.setRuleResultType(RuleResultType.FAIL_IF_NOT_FOUND);
		child2.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		child2.setRuleViolationMessage("Column list is not specified in an insert statement");

		RuleImplementation impl = new RuleImplementation();

		impl.getChildrenRules().getRuleImplementation().add(child2);
		impl.getNames().getTextItem().add(Insert_statementContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.DEFAULT);
		impl.setRuleResultType(RuleResultType.DEFAULT);
		rule.setRuleImplementation(impl);

		return rule;
	}

	public static Rule getOrderByRule() {
		Rule rule = new Rule();
		rule.setKey("C005");
		rule.setInternalKey("C005");
		rule.setName("Do not use column numbers in the ORDER BY clause");
		rule.setDescription("<h2>Description</h2><p>Always use column names in an order by clause. Avoid positional references.</p><h2>Examples</h2><h3>Non-compliant</h3><code>SELECT * from dbo.test order by 1;</code><h3>Compliant</h3><code>SELECT name from dbo.test order by name;</code>");

		RuleImplementation child2 = new RuleImplementation();
		child2.getNames().getTextItem().add(ConstantContext.class.getSimpleName());
		child2.setTextCheckType(TextCheckType.DEFAULT);
		child2.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		child2.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		child2.setRuleViolationMessage("Column number is used instead of name in order by clause");

		RuleImplementation impl = new RuleImplementation();

		impl.getChildrenRules().getRuleImplementation().add(child2);
		impl.getNames().getTextItem().add(Order_by_clauseContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.DEFAULT);
		impl.setRuleResultType(RuleResultType.DEFAULT);
		impl.setRuleViolationMessage("");
		rule.setRuleImplementation(impl);
		return rule;
	}

	public static Rule getExecRule() {
		Rule rule = new Rule();
		rule.setKey("C006");
		rule.setInternalKey("C006");
		rule.setName("Execute/exec for dynamic query was used");
		rule.setDescription(".");
		rule.setDescription("<h2>Description</h2><p>Execute/exec for dynamic query was used. It is better to use sp_executesql for dynamic queries.</p><h2>Examples</h2><h3>Non-compliant</h3><code>EXEC ('SELECT 1');</code><h3>Compliant</h3><code>EXECUTE sp_executesql N'select 1';</code>");

		RuleImplementation child2 = new RuleImplementation();
		child2.getNames().getTextItem().add(ConstantContext.class.getSimpleName());
		child2.setTextCheckType(TextCheckType.DEFAULT);
		child2.setRuleResultType(RuleResultType.FAIL_IF_FOUND);
		child2.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		child2.setRuleViolationMessage("Execute/exec for dynamic query was used. It is better to use sp_executesql for dynamic queries.");
			
		
		
		RuleImplementation impl = new RuleImplementation();
		impl.getChildrenRules().getRuleImplementation().add(child2);
		impl.getNames().getTextItem().add(Execute_statementContext.class.getSimpleName());
		impl.setRuleMatchType(RuleMatchType.CLASS_ONLY);
		impl.setRuleResultType(RuleResultType.DEFAULT);
		rule.setRuleImplementation(impl);
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

		parent0.getUsesRules().getRuleImplementation().add(child2);
		parent0.getUsesRules().getRuleImplementation().add(child3);

		RuleImplementation parent = new RuleImplementation();
		parent.getNames().getTextItem().add(Cursor_nameContext.class.getSimpleName());
		parent.setTextCheckType(TextCheckType.DEFAULT);
		parent.setRuleResultType(RuleResultType.DEFAULT);
		parent.setRuleMatchType(RuleMatchType.DEFAULT);
		parent.setRuleMode(RuleMode.GROUP);

		parent.getUsesRules().getRuleImplementation().add(parent0);
		rule.setRuleImplementation(parent);
		return rule;
	}

	public static void main(String[] args) {

		System.out.println(ruleToString(getWaitForRule(), getSelectAllRule(), getCursorRule(), getInsertRule(),
				getOrderByRule(), getExecRule(), getMultipleDeclarations(), getSameFlow()));

	}
}
