## Plugin parameters and configuration ##
It is possible to control plugin behaviour by specifying the following parameters:

- **sonar.tsql.cg.path** - path where SQL code guard tool is, defaults to *C:\\Program Files (x86)\\SqlCodeGuard\\SqlCodeGuard.Cmd.exe*
- **sonar.tsql.cg.report** - SQL Code Guard results file, defaults to *cgresults.xml*, required only if you run analysis manually, before your build
- **sonar.tsql.ms.report** - ms static code analysis report file, defaults to *staticcodeanalysis.results.xml*
- **sonar.tsql.skip** - flag to skip plugin at all
- **sonar.tsql.skip.cpd** - flag to skip cpd token addition (this might be time consuming process)
- **sonar.tsql.customrules.paths** - a comma separated list of paths to the custom rules, defaults to empty value. If you are specifying this value, then on SonarQube server this needs to be absolute paths to files or directories, for example "c:\sonar-rules,c:\sonar\company.customRules,c:\sonar\othercompany.customRules"
- **sonar.tsql.customrules.prefix** - defines file extension which will be looked for in the custom paths so that custom rules can be loaded, defaults to *.customRules*
- **sonar.tsql.file.suffixes** - defines file extensions which will be scanned and treated as T-SQL language files. Defaults to *.sql*. For example, to scan .prc and .sql files, you can override it on the server side globally in the Administration section or in the project configuration by adding line
`sonar.tsql.file.suffixes=.sql,.prc`
- **sonar.tsql.sqlcover.report** - absolute or relative path to SQLCover report. If by default results are saved to the base dir of project with name *Coverage.opencoverxml* then nothing can be changed
- **sonar.tsql.sqlcover.skip** - flag whether to skip SQLCover coverage reporting
  
### Using default configuration ###
In this example, the following settings will force plugin:
-  to run *C:\\Program Files (x86)\\SqlCodeGuard\\SqlCodeGuard.Cmd.exe* and report found issues 
-  pick up any *staticcodeanalysis.results.xml* produced by msbuild which are found in the base directory and report found issues 
-  pick up and *Coverage.opencoverxml* SQLCover results found in the base directory 
  
<pre>
sonar.projectKey=test:sql:database1
sonar.projectName=sql database
sonar.projectVersion=1.0
sonar.sources=src
sonar.sourceEncoding=UTF-8
</pre>

### Using custom configuration to run SQL Code Guard automatically  ###
In example, the following settings will force plugin to run *C:\\Program Files\\SqlCodeGuard\\SqlCodeGuard.Cmd.exe* and pick up any *msstaticcodeanalysis.results.xml* produced by msbuild which are found in the sources directory
<pre>
sonar.projectKey=test:sql:database1
sonar.projectName=sql database
sonar.projectVersion=1.0
sonar.sources=.
sonar.sourceEncoding=UTF-8
sonar.tsql.cg.path=C:\\Program Files\\SqlCodeGuard\\SqlCodeGuard.Cmd.exe
sonar.tsql.ms.report=msstaticcodeanalysis.results.xml
</pre>

### Using custom configuration to use reports produced by SQL Code Guard ###
In example, the following settings will force plugin to pick up *cgreport.xml*  and *msstaticcodeanalysis.results.xml* files  found in the sources directory
<pre>
sonar.projectKey=test:sql:database1
sonar.projectName=sql database
sonar.projectVersion=1.0
sonar.sources=.
sonar.sourceEncoding=UTF-8
sonar.tsql.cg.path=-
sonar.tsql.cg.report=cgreport.xml
sonar.tsql.ms.report=msstaticcodeanalysis.results.xml
</pre>

Please see *Example* directory where sample T-SQL project is stored for manual configuration of running SQL Code Guard.

### Using your custom rules ###
In this example, supposing on SonarQube server custom rules were configured and loaded, the following settings will force plugin to use custom rules from project's customRules directory and c:/rules/ directory.
<pre>
sonar.projectKey=example.sql2
sonar.projectName=Example sql project2
sonar.projectVersion=1.0
sonar.sources=Example
sonar.sourceEncoding=UTF-8
sonar.language=tsql
## not needed to specify if rules are placed in the same place as on sonar server. Can be relative or absolute paths.
sonar.tsql.customrules.paths=./customRules,c:/rules/

</pre>

