package org.sonar.plugins.tsql.rules.definitions;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.utils.internal.JUnitTempFolder;
import org.sonar.plugins.tsql.checks.custom.SqlRules;

public class CustomUserChecksProviderTest {
	@org.junit.Rule
	public JUnitTempFolder temp = new JUnitTempFolder();

	@Test
	public void testGetRules() throws IOException {
		File baseFile = temp.newFile("rulesTest", "xml");
		FileUtils.copyURLToFile(getClass().getResource("/customrulesSample.xml"), baseFile);

		CustomUserChecksProvider provider = new CustomUserChecksProvider();
		Map<String, SqlRules> rules = provider.getRules(null, "rules", baseFile.getParentFile().getAbsolutePath());
		Assert.assertEquals(1, rules.size());
		Assert.assertEquals(8, rules.values().toArray(new SqlRules[0])[0].getRule().size());
	}

	@Test
	public void testNoRules() throws IOException {

		File baseFile = temp.newFile("ruledsTest", "xml");
		FileUtils.copyURLToFile(getClass().getResource("/customrulesSample.xml"), baseFile);

		CustomUserChecksProvider provider = new CustomUserChecksProvider();
		Map<String, SqlRules> rules = provider.getRules(null, "rules", baseFile.getParentFile().getAbsolutePath());
		Assert.assertEquals(0, rules.size());
	}
}
