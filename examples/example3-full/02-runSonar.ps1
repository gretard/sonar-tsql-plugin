## Run tests with SQLCover
$database = "ExampleDatabase"
$server = ".\SQLEXPRESS01"
$cloverDir = "$PSScriptRoot\tools\SQLCover"
$coverageOutputDir = "$PSScriptRoot\build\sqlcoverresults"

. "$cloverDir\SQLCover.ps1" 
$results = Get-CoverTSql  "$cloverDir\SQLCover.dll"  "server=$server;initial catalog=$database;integrated security=sspi;" "$database " "exec tSQLt.RunAll" 
New-Item -ItemType Directory -Force -Path $coverageOutputDir
Export-OpenXml $results "$coverageOutputDir"  


## Build and run code analysis
$dbProject = "$PSScriptRoot\src\ExampleDatabase\ExampleDatabase.sln"
$msbuildpath = Resolve-Path "C:\Program Files*\MSBuild\*\Bin\*\MSBuild.exe" | select -ExpandProperty Path -First 1


&$msbuildpath "$dbProject" /t:build /p:RunSqlCodeAnalysis=True

## Run SQLCodeGuard if needed manually
$sqlCodeGuard = "$PSScriptRoot\tools\SQLCodeGuardCmdLine\SqlCodeGuard30.Cmd.exe"
$sqlCodeGuardResults = "$PSScriptRoot\build\cgtestresults.xml"
$sqlCodeGuardArgs = @(
	"-source",
	"$PSScriptRoot\src",
	"-out",
	"$sqlCodeGuardResults",
	"/include:all"
)
&$sqlCodeGuard  $sqlCodeGuardArgs



## Run sonar-scanner


$sonarScanner = "$PSScriptRoot\tools\sonar-scanner\sonar-scanner-3.2.0.1227-windows\bin\sonar-scanner.bat"

$sonarArgs = @(
"-Dsonar.projectKey=tsql.sample.project",
"-Dsonar.projectName=TSQL sample project",
"-Dsonar.projectVersion=1.0",
"-Dsonar.sources=src",
"-Dsonar.host.url=http://localhost:9000"
"-Dsonar.exclusions=**/bin/**/*.*,**/obj/**/*.*,**/*.sqlproj", # skip build files from analysis

# it is possible to specify absolute path to the SQLCover report or directory where file matching *Coverage.opencoverxml resides, by default plugin will try to find it in the base directory's subdirectories
"-Dsonar.tsql.sqlcover.report=$coverageOutputDir\Coverage.opencoverxml",

# it is possible to either specify path to sqlcodeguard executable and plugin will try to run it or you can specify actual path to report's xml
# or directory where file matching  *cgresults.xml resides by setting sonar.tsql.cg.report property
"-Dsonar.tsql.cg.path=$sqlCodeGuard"
#"-Dsonar.tsql.cg.report=$sqlCodeGuardResults",

# it is possible to specify absolute path to the MSBuild code analysis report or directory where file matching *StaticCodeAnalysis.Results.xml resides, by default plugin will try to find it in the base directory's subdirectories
#"-Dsonar.tsql.ms.report=$PSScriptRoot\src\ExampleDatabase\ExampleDatabase\bin\Debug"

);
&$sonarScanner $sonarArgs