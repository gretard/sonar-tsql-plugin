package org.sonar.plugins.tsql.rules.parsers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.rules.issues.CgIssues;
import org.sonar.plugins.tsql.rules.issues.CgIssues.File.Issue;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class SqlCodeGuardIssuesParser implements IIssuesParser<TsqlIssue> {

	private static final Logger LOGGER = Loggers.get(SqlCodeGuardIssuesParser.class);

	@Override
	public TsqlIssue[] parse(final File file) {
		final List<TsqlIssue> list = new ArrayList<TsqlIssue>();
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(CgIssues.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final CgIssues issues = (CgIssues) jaxbUnmarshaller.unmarshal(file);
			for (final org.sonar.plugins.tsql.rules.issues.CgIssues.File f : issues.getFile()) {
				for (final Issue is : f.getIssue()) {
					final TsqlIssue issue = new TsqlIssue();
					issue.setDescription(is.getText());
					issue.setFilePath(f.getFullname());
					issue.setLine(is.getLine());
					issue.setType(is.getCode());
					list.add(issue);
				}

			}
			return list.toArray(new TsqlIssue[0]);

		} catch (Throwable e) {
			LOGGER.warn("Unexpected error occured", e);
		}
		return new TsqlIssue[0];
	}

}
