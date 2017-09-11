package org.sonar.plugins.tsql.rules.parsers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.rules.issues.MsIssues;
import org.sonar.plugins.tsql.rules.issues.MsIssues.Problem;

public class MsIssuesParser implements IIssuesParser<TsqlIssue> {

	private static final Logger LOGGER = Loggers.get(MsIssuesParser.class);

	@Override
	public TsqlIssue[] parse(final File file) {
		final List<TsqlIssue> list = new ArrayList<TsqlIssue>();
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(MsIssues.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final MsIssues issues = (MsIssues) jaxbUnmarshaller.unmarshal(file);
			for (final Problem p : issues.getProblem()) {
				final TsqlIssue issue = new TsqlIssue();
				issue.setDescription(p.getProblemDescription());
				issue.setFilePath(p.getSourceFile());
				issue.setLine(p.getLine());
				issue.setType(p.getRule());
				list.add(issue);
			}
			return list.toArray(new TsqlIssue[0]);
		} catch (final Throwable e) {
			LOGGER.warn("Unexpected error occured", e);
		}
		return new TsqlIssue[0];
	}

}
