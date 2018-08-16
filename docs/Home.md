# Sonar TSQL plugin #
## Description ##
Currently plug-in supports:

- 14 code analysis rules by Microsoft. Details can be found at [https://msdn.microsoft.com/en-us/library/dd172133(v=vs.100).aspx](https://msdn.microsoft.com/en-us/library/dd172133(v=vs.100).aspx "https://msdn.microsoft.com/en-us/library/dd172133(v=vs.100).aspx").
- Code analysis rules by SQL Code Guard tool. Details can be found at [https://documentation.red-gate.com/scg/sql-code-guard-3-documentation/sql-static-code-analysis-rules](https://documentation.red-gate.com/scg/sql-code-guard-3-documentation/sql-static-code-analysis-rules)
- Keyword highlighting and copy/paste detectiong using T-SQL grammar from (https://github.com/antlr/grammars-v4/tree/master/tsql)
- Custom plugin rules to detect various issues. Their detection can be disabled by sonar.tsql.skip.custom.rules setting. You can find more details at [[Plugin rules]]
- Support for adding your custom rules defined in xml format. See [[Custom rules]] section for more details.
- Complexity metrics: cyclomatic and cognitive.
- SQLCover reports  (since version 0.8.0). SQLCover reporting details can be found at [https://github.com/GoEddie/SQLCover](https://github.com/GoEddie/SQLCover). You can take a look at examples folder at **/examples/example2-coverage**. 

