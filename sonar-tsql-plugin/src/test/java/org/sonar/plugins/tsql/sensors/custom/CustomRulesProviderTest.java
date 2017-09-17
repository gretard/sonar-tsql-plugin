package org.sonar.plugins.tsql.sensors.custom;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.internal.JUnitTempFolder;
import org.sonar.plugins.tsql.Constants;
import org.sonar.plugins.tsql.rules.custom.CustomRules;
import org.sonar.plugins.tsql.sensors.custom.CustomRulesProvider;

public class CustomRulesProviderTest {
	@org.junit.Rule
	public JUnitTempFolder temp = new JUnitTempFolder();

	@Test
	public void testGetRules() throws IOException {
		File baseFile = temp.newFile("rulesTest", "xml");
		FileUtils.copyURLToFile(getClass().getResource("/customrulesSample.xml"), baseFile);
		
		Settings settings = new Settings();
		settings.setProperty(Constants.PLUGIN_CUSTOM_RULES_PATH, baseFile.getParentFile().getAbsolutePath());
		settings.setProperty(Constants.PLUGIN_CUSTOM_RULES_PREFIX, "rules");
			
		CustomRulesProvider provider = new CustomRulesProvider();
		Map<String, CustomRules> rules = provider.getRules(settings);
		Assert.assertEquals(1, rules.size());
		Assert.assertEquals(4, rules.values().toArray(new CustomRules[0])[0].getRule().size());
	}
	@Test
	public void testNoRules() throws IOException {
		File baseFile = temp.newFile("ruledsTest", "xml");
		FileUtils.copyURLToFile(getClass().getResource("/customrulesSample.xml"), baseFile);
		
		Settings settings = new Settings();
		settings.setProperty(Constants.PLUGIN_CUSTOM_RULES_PATH, baseFile.getParentFile().getAbsolutePath());
		settings.setProperty(Constants.PLUGIN_CUSTOM_RULES_PREFIX, "rules");
			
		CustomRulesProvider provider = new CustomRulesProvider();
		Map<String, CustomRules> rules = provider.getRules(settings);
		Assert.assertEquals(0, rules.size());
	}
}
