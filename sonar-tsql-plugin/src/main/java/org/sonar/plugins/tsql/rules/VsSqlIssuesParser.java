package org.sonar.plugins.tsql.rules;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class VsSqlIssuesParser implements IIssuesParser<TsqlIssue> {

	@Override
	public TsqlIssue[] parse(File file) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(TsqlIssues.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			TsqlIssues customer = (TsqlIssues) jaxbUnmarshaller.unmarshal(file);

			return customer.getIssues();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new TsqlIssue[0];
	}

}
