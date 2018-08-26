package org.sonar.plugins.tsql.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.sonar.plugins.tsql.antlr.FillerRequest;
import org.sonar.plugins.tsql.checks.custom.Rule;
import org.sonar.plugins.tsql.checks.custom.SqlRules;

public class RulesHelperTool {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		if (args.length != 3) {
			System.out.println("Please pass the following: ");
			System.out.println("\taction (print or  verify)");
			System.out.println("\ttype (text or  file)");
			System.out.println("\tvalue (sql string or path to folder) ");
			System.out.println("Example:\r\nprint text \"SELECT * FROM dbo.test;\"");
			System.out.println("Example:\r\nverify file \"c:/tests/customRules.rules;\"");

			return;
		}

		String action = args[0];
		String type = args[1];
		String value = args[2];
		String text = value;
		if (!"text".equals(type)) {
			text = IOUtils.toString(new FileInputStream(value), "UTF-8");
		}
		if ("print".equalsIgnoreCase(action)) {
			System.out.println("Printing tree:\r\n");
			FillerRequest result = AntlrUtils.getRequest(text);

			AntlrUtils.print(result.getRoot(), 0, result.getStream());
			return;

		}
		System.out.println("text");
		SqlRules[] rules = AntlrUtils.read(value);

		for (SqlRules rule : rules) {
			System.out.println("Checking repository: " + rule.getRepoName());
			for (Rule r : rule.getRule()) {
				System.out.println("Checking rule: " + r.getKey());
				for (String s : r.getRuleImplementation().getCompliantRulesCodeExamples().getRuleCodeExample()) {
					boolean res = AntlrUtils.verify(r, s).length == 0;
					System.out.println("\tc passed: " + res + " for " + s);
				}
				for (String s : r.getRuleImplementation().getViolatingRulesCodeExamples().getRuleCodeExample()) {
					boolean res = AntlrUtils.verify(r, s).length > 0;
					System.out.println("\tv passed: " + !res + " for " + s);
				}

			}
		}

	}

}
