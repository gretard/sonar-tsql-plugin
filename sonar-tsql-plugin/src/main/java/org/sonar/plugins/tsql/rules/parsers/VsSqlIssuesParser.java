package org.sonar.plugins.tsql.rules.parsers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;
import org.sonar.plugins.tsql.rules.issues.VsIssues;
import org.sonar.plugins.tsql.rules.issues.VsIssues.Problem;

public class VsSqlIssuesParser implements IIssuesParser<TsqlIssue> {

	private static final Logger LOGGER = Loggers.get(VsSqlIssuesParser.class);

	@Override
	public TsqlIssue[] parse(File file) {
		final List<TsqlIssue> list = new ArrayList<TsqlIssue>();
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(VsIssues.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final VsIssues issues = (VsIssues) jaxbUnmarshaller.unmarshal(file);
			for (final Problem p : issues.getProblem()) {
				final TsqlIssue issue = new TsqlIssue();
				issue.setDescription(p.getProblemDescription());
				issue.setFilePath(p.getSourceFile());
				issue.setLine(p.getLine());
				issue.setType(p.getRule());
				list.add(issue);
			}
			return list.toArray(new TsqlIssue[0]);
		} catch (Throwable e) {
			LOGGER.error("Unexpected error occured", e);
		}
		return new TsqlIssue[0];
	}

}
