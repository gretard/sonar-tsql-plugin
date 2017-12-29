package org.sonar.plugins.tsql.metrics;

import java.util.List;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;
import static java.util.Arrays.asList;
public class TSQLMetrics implements Metrics {

	  public static final Metric<Integer> CODE_COMPLEXITY = new Metric.Builder("complexity_hc", "Halstead complexity", Metric.ValueType.INT)
	    .setDescription("Code complexity. Defines how hard is to comprehend code. Based on https://stackoverflow.com/a/3353953.")
	    .setDirection(Metric.DIRECTION_WORST)
	    .setQualitative(true)
	    .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
	    .create();

	@Override
	public List<Metric> getMetrics() {
		 return asList(CODE_COMPLEXITY);
	}

}