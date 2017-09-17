package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;

public class DefaultLinesProvider implements ILinesProvider {

	private CommonTokenStream stream;

	public DefaultLinesProvider(CommonTokenStream stream) {
		this.stream = stream;
	}

	@Override
	public int getLine(ParsedNode node) {
		Interval sourceInterval = node.getItem().getSourceInterval();
		Token firstToken = stream.get(sourceInterval.a);
		int line = firstToken.getLine();
		return line;
	}

}
