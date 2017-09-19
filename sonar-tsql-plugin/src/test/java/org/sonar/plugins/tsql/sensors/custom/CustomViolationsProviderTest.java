package org.sonar.plugins.tsql.sensors.custom;

import org.junit.Assert;
import org.junit.Test;

public class CustomViolationsProviderTest {

	@Test
	public void test() {
		CustomViolationsProvider provider = new CustomViolationsProvider(new ILinesProvider() {

			@Override
			public int getLine(ParsedNode node) {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		Assert.assertEquals(0, provider.getIssues().length);
	}

}
