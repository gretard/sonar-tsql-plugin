package org.sonar.plugins.tsql.helpers;

import org.antlr.tsql.TSqlParser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class AntrlResult {
	private ParseTree tree;
	private CommonTokenStream stream;
	private TSqlParser parser;
	public ParseTree getTree() {
		return tree;
	}
	public void setTree(ParseTree tree) {
		this.tree = tree;
	}
	public CommonTokenStream getStream() {
		return stream;
	}
	public void setStream(CommonTokenStream stream) {
		this.stream = stream;
	}
	public void setParser(TSqlParser parser) {
		this.parser = parser;
		// TODO Auto-generated method stub
		
	}
	public TSqlParser getParser() {
		return parser;
	}
}
