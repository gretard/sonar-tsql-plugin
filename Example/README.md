# Example T-SQL project
In order to get results to sonar server please:

 - If needed update and run *run.bat*, which will run [SQL Code Guard tool](http://sqlcodeguard.com "http://sqlcodeguard.com")
 - Build sample project
 - Update sonar-project.properties file if needed
 - Run sonar scanner

## Using custom rules ##
- Please update file **myExampleRepo.customRules** in *customRules* directory
- Copy **myExampleRepo.customRules** file to SonarQube's server known place, i.e. **C:\\sonar\\rules**
- Set sonar **sonar.tsql.customrules.paths** setting to **C:\\sonar\\** or **C:\\sonar\\myExampleRepo.customRules**
- Restart SonarQube server
- Start analysis