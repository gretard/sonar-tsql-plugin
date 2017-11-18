package org.sonar.plugins.tsql.sensors.custom.matchers;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sonar.plugins.tsql.checks.custom.RuleDistanceIndexMatchType;
import org.sonar.plugins.tsql.checks.custom.RuleImplementation;
import org.sonar.plugins.tsql.helpers.TestNode;
import org.sonar.plugins.tsql.sensors.custom.nodes.IParsedNode;

@RunWith(Parameterized.class)
public class DistanceMatcherTest {
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(
				new Object[][] { { RuleDistanceIndexMatchType.DEFAULT, 0, new TestNode("test", "test", 1, 1, 1), true },
						{ RuleDistanceIndexMatchType.MORE, 1, new TestNode("test", "test", 0, 2, 1), false },
						{ RuleDistanceIndexMatchType.MORE, 1, new TestNode("test", "test", 1, 0, 1), true },
						{ RuleDistanceIndexMatchType.LESS, 1, new TestNode("test", "test", 0, 1, 2), true },
						{ RuleDistanceIndexMatchType.LESS, 2, new TestNode("test", "test", 1, 1, 2), true },
						{ RuleDistanceIndexMatchType.LESS, 2, new TestNode("test", "test", 10, 5, 2), false },
						{ RuleDistanceIndexMatchType.EQUALS, 2, new TestNode("test", "test", 2, 2, 2), true },
						{ RuleDistanceIndexMatchType.EQUALS, 2, new TestNode("test", "test", 1, 5, 2), false },

				});
	}

	private boolean result;
	private IParsedNode node;
	private int distance;
	private RuleDistanceIndexMatchType type;
	private final DistanceMatcher matcher = new DistanceMatcher();

	public DistanceMatcherTest(RuleDistanceIndexMatchType type, int distance, IParsedNode node, boolean result) {
		this.type = type;
		this.distance = distance;
		this.node = node;
		this.result = result;

	}

	@Test
	public void test() {
		RuleImplementation rule = new RuleImplementation();
		rule.setDistance(this.distance);
		rule.setDistanceCheckType(this.type);
		Assert.assertEquals(String.format("Expected %s, %s with type %s index1: %s index2 %s", result, type,
				this.distance, node.getIndex(), node.getIndex2()),

				this.result, matcher.match(rule, this.node));
	}

}
