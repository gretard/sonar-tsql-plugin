. .\SQLCover.ps1  
$result = Get-CoverTSql ".\SQLCover.dll" "server=.;initial catalog=tSQLt_Example;integrated security=sspi;" "tSQLt_Example" "exec tSQLt.RunAll"  
$result
Export-OpenXml $result "$PSScriptRoot\build" 