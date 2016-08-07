import java.io.File;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.plugins.tsql.rules.TsqlIssues;
import org.sonar.plugins.tsql.rules.TsqlIssue;
import org.sonar.plugins.tsql.rules.Tsqlssue;

public class SerializationTests {

	@Test
	public void test() throws JAXBException {
		String file = "sample.xml";

		InputStream st = this.getClass().getResourceAsStream(file);
		Assert.assertNotNull(st);
		JAXBContext jaxbContext = JAXBContext.newInstance(TsqlIssues.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		TsqlIssues issues = (TsqlIssues) jaxbUnmarshaller.unmarshal(st);
		Assert.assertEquals(1, issues.getIssues().length);
		TsqlIssue issue = issues.getIssues()[0];
		Assert.assertEquals("Microsoft.Rules.Data.SR0001", issue.getType());

	}

}
