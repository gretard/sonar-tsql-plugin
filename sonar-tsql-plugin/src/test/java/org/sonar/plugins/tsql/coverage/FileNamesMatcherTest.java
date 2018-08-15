package org.sonar.plugins.tsql.coverage;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class FileNamesMatcherTest {
	FileNamesMatcher sut = new FileNamesMatcher();
	@Test
	public void testMatchWithNoSchema() {
		CoveredLinesReport[] lines = sut.match("[tes t].sql","src",
				ImmutableMap.of("dbo.test", new CoveredLinesReport("[dbo].[test]"), "other.test", new CoveredLinesReport("[other].[test")));
		Assert.assertEquals(2, lines.length);
	}

	@Test
	public void testMatchWithNoSchemaAndParent() {
		CoveredLinesReport[] lines = sut.match("[tes t].sql","dbo",
				ImmutableMap.of("dbo.test", new CoveredLinesReport("[dbo].[test]"), "other.test", new CoveredLinesReport("[other].[test")));
		Assert.assertEquals(1, lines.length);
	}
	@Test
	public void testMatchWithSchemaAndParent() {
		CoveredLinesReport[] lines = sut.match("dbo.[tes t].sql","dbo",
				ImmutableMap.of("dbo.test", new CoveredLinesReport("[dbo].[test]"), "other.test", new CoveredLinesReport("[other].[test")));
		Assert.assertEquals(1, lines.length);
	}
	@Test
	public void testNoMatch() {
		
		CoveredLinesReport[] lines = sut.match("[test2].sql", "src",
				ImmutableMap.of("dbo.test", new CoveredLinesReport("[dbo].[test]"), "other.test", new CoveredLinesReport("[other].[test")));
		Assert.assertEquals(0, lines.length);
	}

	@Test
	public void testMatchWithSchema() {
		CoveredLinesReport[] lines = sut.match("dbo.t est.sql","src",
				ImmutableMap.of("dbo.test", new CoveredLinesReport("[dbo].[test]"), "other.test", new CoveredLinesReport("[other].[test")));
		Assert.assertEquals(1, lines.length);
	}

	@Test
	public void testMatchWithSchema2() {
		CoveredLinesReport[] lines = sut.match("dbo.test.sql", "src",ImmutableMap.of("dbo.test", new CoveredLinesReport("[dbo].[test]"), "other.test", new CoveredLinesReport("[other].[test")));
		Assert.assertEquals(1, lines.length);
	}
	
	@Test
	public void testMatchWithSchema3() {
		CoveredLinesReport[] lines = sut.match("db.dbo.test.sql", "src",ImmutableMap.of("dbo.test", new CoveredLinesReport("[dbo].[test]"), "other.test", new CoveredLinesReport("[other].[test")));
		Assert.assertEquals(1, lines.length);
	}

}
