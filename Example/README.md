# Example T-SQL project
In order to get results to sonar server please:

 - (Optional) If needed update path to SQL code guard on sonar server or add **sonar.tsql.cg.path** setting with path in sonar.properties file
 - (Optional) Build sample project with setting /p:RunSqlCodeAnalysis=true if not using other means for building:
 	- msbuild /p:RunSqlCodeAnalysis=true
 - (Optional) Update sonar-project.properties file as needed
 - Run sonar scanner

## Using custom rules ##
- (Optional) Please update file **myExampleRepo.customRules** in *customRules* directory with your custom rules
- Copy **myExampleRepo.customRules** file to SonarQube's server known place, i.e. **C:\\sonar\\rules** folder
- Set on server sonar **sonar.tsql.customrules.paths** setting to **C:\\sonar\\rules\\** or **C:\\sonar\\rules\\myExampleRepo.customRules**
- Restart SonarQube server
- Run sonar scanner