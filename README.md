# sonar-tsql-plugin
This repository contains T-SQL language plugin for Sonar.

## Description ##
Currently plugin supports:

- 14 code analysis rules by Microsoft. Details can be found at [https://msdn.microsoft.com/en-us/library/dd172133(v=vs.100).aspx](https://msdn.microsoft.com/en-us/library/dd172133(v=vs.100).aspx "https://msdn.microsoft.com/en-us/library/dd172133(v=vs.100).aspx").

In the future it is planned to:

- Add support for SQL Code guard rules [http://sqlcodeguard.com/](http://sqlcodeguard.com/)
- Implement syntax highlighting of the reserved words
 

## Usage ##

1. Download and install SonarQube
2. Download plugin from the [releases](https://github.com/gretard/sonar-tsql-plugin/releases) and copy it to sonarqube's extensions\plugins directory
3. Start SonarQube and enable rules

## Plugin parameters ##
By default plugin tries to find all xml files in base directory ending in *staticcodeanalysis.results.xml*. It is possible to override these by specyfing the following parameters:

- *sonar.tsql.ms.reportPath* - directory where analysis results are stored
- *sonar.tsql.ms.report* - report file

In example, the following settings will force plugin to search files ending in test.xml in c:\test directory:
<pre>
sonar.projectKey=test:sql:database1
sonar.projectName=sql database
sonar.projectVersion=1.0
sonar.sources=.
sonar.sourceEncoding=UTF-8
sonar.tsql.ms.reportPath=c:\\test
sonar.tsql.ms.report=test.xml
</pre>
