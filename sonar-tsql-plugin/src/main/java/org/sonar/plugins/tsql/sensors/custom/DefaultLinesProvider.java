package org.sonar.plugins.tsql.sensors.custom;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;

public class DefaultLinesProvider implements ILinesProvider {

	private final CommonTokenStream stream;

	public DefaultLinesProvider(final CommonTokenStream stream) {
		this.stream = stream;
	}

	@Override
	public int getLine(ParsedNode node) {
		final Interval sourceInterval = node.getItem().getSourceInterval();
		final Token firstToken = stream.get(sourceInterval.a);
		int line = firstToken.getLine();
		return line;
	}

}
