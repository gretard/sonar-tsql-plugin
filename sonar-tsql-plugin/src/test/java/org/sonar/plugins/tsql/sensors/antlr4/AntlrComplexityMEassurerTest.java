package org.sonar.plugins.tsql.sensors.antlr4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metrics;
import org.sonar.plugins.tsql.helpers.Antlr4Utils;
import org.sonar.plugins.tsql.helpers.AntrlResult;
import org.sonar.plugins.tsql.languages.TSQLLanguage;
import org.sonar.plugins.tsql.sensors.antlr4.LinesProvider.SourceLine;


public class AntlrComplexityMEassurerTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void test() throws IOException {
		String s = 
				"SELECT SalesOrderID, SUM(LineTotal) AS SubTotal "
				+ "FROM Sales.SalesOrderDetail  left join dbo.x on t1.id = t2.id where a > 5"+
"GROUP BY SalesOrderID  "+
"HAVING SUM(LineTotal) > 100000.00  "+
"ORDER BY SalesOrderID, test  ";
				//"select *,1,2 from dbo.test where a > 0 and b > 2; ";
			/*	+ "insert into dbo.test(id, id2) values (1,2);"
				+ "update dbo.test set id = 1 where id > 0;"
	    +"MERGE Production.UnitMeasure AS target     USING (SELECT @UnitMeasureCode, @Name) AS source (UnitMeasureCode, Name)      ON (target.UnitMeasureCode = source.UnitMeasureCode)"  
	    +"WHEN MATCHED THEN           UPDATE SET Name = source.Name  WHEN NOT MATCHED THEN      INSERT (UnitMeasureCode, Name)      VALUES (source.UnitMeasureCode, source.Name)  "
	    +"OUTPUT deleted.*, $action, inserted.* INTO #MyTempTable; ";
	*/

		
		AntrlResult result = Antlr4Utils.getFull(s);
		
	int r = 	new XXX().visit(result.getTree()); //.getTree().
		Antlr4Utils.print(result.getTree(), 0);
		System.out.println(r);
	}

}
