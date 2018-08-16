# sonar-tsql-plugin
This repository contains T-SQL language plug-in for Sonar.

## Description ##
Currently plug-in supports:

- 14 code analysis rules by Microsoft. More details can be found at [https://msdn.microsoft.com/en-us/library/dd172133(v=vs.100).aspx](https://msdn.microsoft.com/en-us/library/dd172133(v=vs.100).aspx "https://msdn.microsoft.com/en-us/library/dd172133(v=vs.100).aspx").
- Code analysis rules by SQL Code Guard tool. Supported rules details can be found at [https://documentation.red-gate.com/scg/sql-code-guard-3-documentation/sql-static-code-analysis-rules](https://documentation.red-gate.com/scg/sql-code-guard-3-documentation/sql-static-code-analysis-rules)
- Keyword highlighting and copy/paste detectiong using T-SQL grammar from (https://github.com/antlr/grammars-v4/tree/master/tsql)
- Custom plug-in rules to detect various issues. You can find more details at [Plugin rules](https://github.com/gretard/sonar-tsql-plugin/wiki/Plugin-rules)
- Support for adding your custom rules defined in xml format. See [Custom rules](https://github.com/gretard/sonar-tsql-plugin/wiki/Custom-rules) section for more details.
- Complexity metrics: cyclomatic and cognitive.
- SQLCover reports (since version 0.8.0). SQLCover reporting details can be found at [https://github.com/GoEddie/SQLCover](https://github.com/GoEddie/SQLCover). You can take a look at examples folder at **/examples/example2-coverage**. 


## Usage ##

1. Download and install SonarQube
2. Download plug-in from the [releases](https://github.com/gretard/sonar-tsql-plugin/releases) and copy it to sonarqube's extensions\downloads directory
3. Start SonarQube and enable rules
4. If you want plug-in to automatically run SQL Code guard analysis part, please install [SQL Code Guard](http://sqlcodeguard.com/ "SQL Code Guard") onto your build machines and make sure that property **sonar.tsql.cg.path** is set to an existing installation (default is **C:\\Program Files (x86)\\SqlCodeGuard\\SqlCodeGuard.Cmd.exe**)
5. If you want plug-in to import issues found by Microsoft, please before running a sonar analysis build solution with setting */p:RunSqlCodeAnalysis=true*. 
6. If you want coverage to be reported - please run SQLCover before running sonar analysis

## Configuration ##
Options which are available for overriding can be found at [Configuration](https://github.com/gretard/sonar-tsql-plugin/wiki/Configuration).

## CI integration ##
### TFS integration ###

For T-SQL code to be scanned from TFS Build, the easiest way to start is to install:

 - SonarQube extension from marketplace https://marketplace.visualstudio.com/items?itemName=SonarSource.sonarqube into your TFS server
 - [SQL Code Guard](http://sqlcodeguard.com/ "SQL Code Guard") into your build agents if you want to use SQL Code Guard tool

After having done this, please use and configure **SonarQube Scanner CLI** step in your build definition. 




