package org.sonar.plugins.tsql.rules.parsers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.tsql.rules.issues.CodeGuardIssues;
import org.sonar.plugins.tsql.rules.issues.CodeGuardIssues.File.Issue;
import org.sonar.plugins.tsql.rules.issues.TsqlIssue;

public class CodeGuardIssuesParser implements IIssuesParser<TsqlIssue> {

	private static final Logger LOGGER = Loggers.get(CodeGuardIssuesParser.class);

	@Override
	public TsqlIssue[] parse(final File file) {
		final List<TsqlIssue> list = new ArrayList<TsqlIssue>();
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(CodeGuardIssues.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final CodeGuardIssues issues = (CodeGuardIssues) jaxbUnmarshaller.unmarshal(file);
			for (final org.sonar.plugins.tsql.rules.issues.CodeGuardIssues.File f : issues.getFile()) {
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
