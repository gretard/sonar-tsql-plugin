package org.sonar.plugins.tsql.helpers;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;

public class CodePrinter {
	public static void main(String args[]) {
		AntrlResult result = Antlr4Utils.getFull("SELECT * from test; SELECT * from test; ");
		ParseTree root = result.getTree();
		print(root, 0, result.getStream());
	}

	static void print(ParseTree node, int level, CommonTokenStream stream) {
		int tmp = level + 1;
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.repeat("\t", level));
		sb.append(node.getClass().getSimpleName() + ": " + node.getText());
		System.out.println(sb.toString());
		final int n = node.getChildCount();

		for (int i = 0; i < n; i++) {

			final ParseTree c = node.getChild(i);
			print(c, tmp, stream);

		}
	}
}
