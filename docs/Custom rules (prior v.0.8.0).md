# Wiki #
## Custom rules ##
Currently, plugin supports naive implementation of ability to match code against custom rules defined in xml files with specific format. Below are the details how to start using and creating custom rules.


### Usage ###
To use custom rules, you need:

 - Create an xml file with custom rules defined, there is an [example file](https://github.com/gretard/sonar-tsql-plugin/raw/master/Example/customRules/myExampleRepo.customRules) at **/Example/customRules/** folder. Each file defines a single repository.
 - Deploy custom rules file/files to SonarQube server's filesystem, i.e. copy file to **C:\sonar\** folder
 - Update **sonar.tsql.customrules.paths** settings in SonarQube server for TSQL plugin (please note, that on SonarQube server paths need to be absolute), i.e.: 

	 - sonar.tsql.customrules.paths=C:\sonar-rules\
	 - sonar.tsql.customrules.paths=C:\sonar-rules\test.customRules,C:\sonar-rules\test2.customRules
- Restart SonarQube server
- Copy same rule files to the machine from which you are running analysis. You could copy to same place as in settings on the SonarQube server or you can keep then together with code, then you can specify either relative or absolute path to the rules, i.e.:
    - sonar.tsql.customrules.paths=./customRules

- Run code analysis

### Creating custom rules ###
Custom rules are defined using xml format. Each file should be defining a single repository using format described below. Attributes repoName and repoKey defines repository's name and key used by SonarQube. All keys should be unique.

Mandatory fields for rule:

- key - used to report issues, should be unique among repositories
- name - rule name
- internalKey - used to report issues, should be unique among repositories
- description - rule description
- ruleImplementation - defines how to detect code against which rules will be applied. There are different options for this item:
	- names - defines a list of T-SQL parser classes on which rule will be applied. Full list is specified below.
	- textToFind - defines custom text which will be looked for in the applicable nodes
	- parentRules - defines array of rules of type ruleImplementation which will be applied for node's parents 
	- childrenRules - defines array of rules of type ruleImplementation which will be applied for node's children
	- siblingsRules- defines array of rules of type ruleImplementation which will be applied for node's siblings
	- usesRules - defines array of rules of type ruleImplementation which will be applied for node's similar to current node, i.e. contains same cursor name
	- ruleViolationMesssage - contains text shown by SonarQube server
	- times - if ruleResultType is set to FailIfLessFound or FailIfMoreFound then this value is compared against actual number of nodes violating that rule and failing accordingly
	- ruleMode - can be SINGLE or GROUP. If  SINGLE then all nodes matching rule will be treated differently, i.e. for each select statement rule will be applied. If mode is set to GROUP, then plugin will try to group matching nodes and apply rule once, i.e. when searching for cursors with same names.
	- ruleMatchType - defines how plugin will be applying rules, can be:
		- Full - will check if selected node's class matches, contains specified text if any and node contains text which was found for by other rules
		- TextOnly - checks only if node contains specified text in the textToFind section
		- TextAndClass - checks if node contains specified text in the textToFind section and class name matches
		- ClassOnly - checks if class name matches
		- Strict - this options does the Full check and in addition checks if node is in the same control flow as parent node
	- ruleResultType - defines what to do if a particular rule matched code. Available options:
		- Default - does nothing
		- FailIfFound - reports code issue if at least one node was found violating rule
		- FailIfNotFound - reports code issue if no node was found violating rule
		- FailIfLessFound/FailIfMoreFound - reports code issue if rule was not violated less or more times than value defined by times value
		- SkipIfFound/SkipIfNotFound - skips code issue reporting at all if rule was satisfied or not
	- textCheckType - defines how to search for particular text in nodes, can be:
		- Contains - will be checked if node contains some defined text
		- Regexp - will be checked if node's text matches regexp
		- Strict - equals  operator will be used
   - violatingRulesCodeExamples/compliantRulesCodeExamples - can contain examples of compliant and non-compliant code 
   - distance - specifies how far node node should be from parent so that it is selected
   - index - specifies which node to select, if negative - elements is selected in reverse order, for example -1 can be used to select last child element
   - indexCheckType - specifies how to compare node's index, can be:

		- Default - does not nothing
		- More - checks if node's index to parent node is more than expected value
		- Less - checks if node's index to parent node is less than expected value
		- Equals - checks if node's index to parent node is equal to the expected value

   - distanceCheckType - specifies how to compare node's distance, can be:

		- Default - does not nothing
		- More - checks if node's distance to parent node is more than expected value
		- Less - checks if node's distance to parent node is less than expected value
		- Equals - checks if node's distance to parent node is equal to the expected value

Schema can be found at [/sonar-tsql-plugin/src/main/resources/schemas/customRules.xsd](https://github.com/gretard/sonar-tsql-plugin/blob/master/sonar-tsql-plugin/src/main/resources/schemas/customRules.xsd).

Below is an example of an xml file contents for with rules:

```
<sql-rules repoName="Demo rules" repoKey="tsqlDemoRepo">
  <rule>
        <key>CC001</key>
        <name>Waitfor is used.</name>
        <internalKey>CC001</internalKey>
        <description>Waitfor is used</description>
        <ruleImplementation>
            <names>
                <textItem>Waitfor_statementContext</textItem>
            </names>
            <textToFind/>
            <parentRules/>
            <childrenRules/>
            <siblingsRules/>
            <usesRules/>
            <ruleViolationMessage>Waitfor is used.</ruleViolationMessage>
            <times>0</times>
            <ruleMode>Single</ruleMode>
            <ruleMatchType>ClassOnly</ruleMatchType>
            <ruleResultType>FailIfFound</ruleResultType>
            <textCheckType>Default</textCheckType>
            <violatingRulesCodeExamples/>
            <compliantRulesCodeExamples/>
        </ruleImplementation>
    </rule>
  <rule>
        <key>CC002</key>
        <name>SELECT * is used</name>
        <internalKey>CC002</internalKey>
        <description>SELECT * is used. Please list names.</description>
        <ruleImplementation>
            <names>
                <textItem>Select_listContext</textItem> 
            </names>
            <textToFind/>
            <parentRules/>
            <childrenRules>
                <ruleImplementation>
                    <names>
                        <textItem>Select_list_elemContext</textItem>
                    </names>
                    <textToFind>
                        <textItem>*</textItem>
                    </textToFind>
                    <parentRules/>
                    <childrenRules/>
                    <siblingsRules/>
                    <usesRules/>
                    <ruleViolationMessage>SELECT * is used</ruleViolationMessage>
                    <times>0</times>
                    <ruleMode>Single</ruleMode>
                    <ruleMatchType>TextAndClass</ruleMatchType>
                    <ruleResultType>FailIfFound</ruleResultType>
                    <textCheckType>Strict</textCheckType>
                    <violatingRulesCodeExamples/>
                    <compliantRulesCodeExamples/>
                </ruleImplementation>
            </childrenRules>
            <siblingsRules/>
            <usesRules/>
            <times>0</times>
            <ruleMode>Single</ruleMode>
            <ruleMatchType>Default</ruleMatchType>
            <ruleResultType>Default</ruleResultType>
            <textCheckType>Default</textCheckType>
            <violatingRulesCodeExamples>
                <ruleCodeExample>SELECT * from dbo.test;</ruleCodeExample>
            </violatingRulesCodeExamples>
            <compliantRulesCodeExamples>
                <ruleCodeExample>SELECT name, surname from dbo.test;</ruleCodeExample>
 <ruleCodeExample>SELECT name, surname, 1 * 3 from dbo.test;</ruleCodeExample>
            </compliantRulesCodeExamples>
        </ruleImplementation>
    </rule>
</sql-rules>
```

To start building custom rules, you can also look into example at  [example file](https://github.com/gretard/sonar-tsql-plugin/raw/master/Example/customRules/myExampleRepo.customRules).

In addition to this, there is a small command line tool allowing to verify rules [rulesHelper.jar](https://github.com/gretard/sonar-tsql-plugin/releases/download/0.6.1-alpha1/rulesHelper.jar "rulesHelper.jar") against code in the violatingRulesCodeExamples/compliantRulesCodeExamples sections. And also it allows to print tree of parsed T-SQL code.

Printing tree of *select 1* statement:

```
java -jar rulesHelper.jar print text "select 1" 
```

Checking if rules produces expected results as defined in the compliantRulesCodeExamples/violatingRulesCodeExamples sections:

```
java -jar rulesHelper.jar verify file "c:/sonar/myExampleRepo.customRules"
```

### Simple rule example ###

Below is an example of a custom rule reporting all WAITFOR statements found.

```
    <rule>
        <key>C001</key>
        <name>WAITFOR is used</name>
        <internalKey>C001</internalKey>
        <description>WAITFOR is used.&lt;h2&gt;Code examples&lt;/h2&gt;&lt;h3&gt;Non-compliant&lt;/h3&gt;&lt;pre&gt;&lt;code&gt;WAITFOR '10:00:00';&lt;/code&gt;&lt;/pre&gt;</description>
        <severity>MINOR</severity>
        <remediationFunction>LINEAR</remediationFunction>
        <debtRemediationFunctionCoefficient>2min</debtRemediationFunctionCoefficient>
        <tag>performance</tag>
        <ruleImplementation>
            <names>
                <textItem>Waitfor_statementContext</textItem>
            </names>
            <textToFind/>
            <parentRules/>
            <childrenRules/>
            <siblingsRules/>
            <usesRules/>
            <ruleViolationMessage>WAITFOR is used.</ruleViolationMessage>
            <times>1</times>
            <distance>0</distance>
            <index>0</index>
            <indexCheckType>Default</indexCheckType>
            <distanceCheckType>Default</distanceCheckType>
            <ruleMode>Default</ruleMode>
            <ruleMatchType>ClassOnly</ruleMatchType>
            <ruleResultType>FailIfFound</ruleResultType>
            <textCheckType>Default</textCheckType>
            <violatingRulesCodeExamples>
                <ruleCodeExample>WAITFOR '10:00:00';</ruleCodeExample>
            </violatingRulesCodeExamples>
            <compliantRulesCodeExamples/>
        </ruleImplementation>
    </rule>
```

For example, statement *WAITFOR '10:00:00';* will be parsed as:

```
Tsql_fileContext@0: WAITFOR'10:00:00';<EOF>
	BatchContext@1: WAITFOR'10:00:00';
		Sql_clausesContext@2: WAITFOR'10:00:00';
			Sql_clauseContext@3: WAITFOR'10:00:00';
				Cfl_statementContext@4: WAITFOR'10:00:00';
					Waitfor_statementContext@5: WAITFOR'10:00:00';
						TerminalNodeImpl@6: WAITFOR
						ExpressionContext@6: '10:00:00'
							Primitive_expressionContext@7: '10:00:00'
								ConstantContext@8: '10:00:00'
									TerminalNodeImpl@9: '10:00:00'
						TerminalNodeImpl@6: ;
	TerminalNodeImpl@1: <EOF>
```


How custom rules work:

1. Find all nodes of type *Waitfor_statementContext*
2. As rule mode is set to *SINGLE* and *FailIfFound* - all lines where  node types *Waitfor_statementContext* were found will be reported as violating this rule. 


### Creating more complex custom rules ###

Below is an example of a rule finding issues where select statements contain asterisks.

```
 <rule>
        <key>C002</key>
        <name>SELECT * is used</name>
        <internalKey>C002</internalKey>
        <description>&lt;h2&gt;Description&lt;/h2&gt;&lt;p&gt;SELECT * is used. Please list names.&lt;/p&gt;&lt;h2&gt;Code examples&lt;/h2&gt;&lt;h3&gt;Non-compliant&lt;/h3&gt;&lt;pre&gt;&lt;code&gt;SELECT * from dbo.test;&lt;/code&gt;&lt;/pre&gt;&lt;h3&gt;Compliant&lt;/h3&gt;&lt;pre&gt;&lt;code&gt;SELECT name, surname from dbo.test;&lt;/code&gt;&lt;/pre&gt;&lt;pre&gt;&lt;code&gt;SELECT name, surname, 1 * 3 from dbo.test;&lt;/code&gt;&lt;/pre&gt;</description>
        <severity>MINOR</severity>
        <remediationFunction>LINEAR</remediationFunction>
        <debtRemediationFunctionCoefficient>2min</debtRemediationFunctionCoefficient>
        <tag>design</tag>
        <ruleImplementation>
            <names>
                <textItem>Select_list_elemContext</textItem>
            </names>
            <textToFind>
                <textItem>*</textItem>
            </textToFind>
            <parentRules/>
            <childrenRules/>
            <siblingsRules/>
            <usesRules/>
            <ruleViolationMessage>SELECT * was used</ruleViolationMessage>
            <times>1</times>
            <distance>0</distance>
            <index>0</index>
            <indexCheckType>Default</indexCheckType>
            <distanceCheckType>Default</distanceCheckType>
            <ruleMode>Default</ruleMode>
            <ruleMatchType>TextAndClass</ruleMatchType>
            <ruleResultType>FailIfFound</ruleResultType>
            <textCheckType>Strict</textCheckType>
            <violatingRulesCodeExamples>
                <ruleCodeExample>SELECT * from dbo.test;</ruleCodeExample>
            </violatingRulesCodeExamples>
            <compliantRulesCodeExamples>
                <ruleCodeExample>SELECT name, surname from dbo.test;</ruleCodeExample>
                <ruleCodeExample>SELECT name, surname, 1 * 3 from dbo.test;</ruleCodeExample>
            </compliantRulesCodeExamples>
        </ruleImplementation>
    </rule>
```
For example, statement *SELECT * from dbo.test;* will be parsed as:

```
Tsql_fileContext@0: SELECT*FROMDBO.TEST;<EOF>
	BatchContext@1: SELECT*FROMDBO.TEST;
		Sql_clausesContext@2: SELECT*FROMDBO.TEST;
			Sql_clauseContext@3: SELECT*FROMDBO.TEST;
				Dml_clauseContext@4: SELECT*FROMDBO.TEST;
					Select_statementContext@5: SELECT*FROMDBO.TEST;
						Query_expressionContext@6: SELECT*FROMDBO.TEST
							Query_specificationContext@7: SELECT*FROMDBO.TEST
								TerminalNodeImpl@8: SELECT
								Select_listContext@8: *
									Select_list_elemContext@9: *
										AsteriskContext@10: *
											TerminalNodeImpl@11: *
								TerminalNodeImpl@8: FROM
								Table_sourcesContext@8: DBO.TEST
									Table_sourceContext@9: DBO.TEST
										Table_source_item_joinedContext@10: DBO.TEST
											Table_source_itemContext@11: DBO.TEST
												Table_name_with_hintContext@12: DBO.TEST
													Table_nameContext@13: DBO.TEST
														IdContext@14: DBO
															Simple_idContext@15: DBO
																TerminalNodeImpl@16: DBO
														TerminalNodeImpl@14: .
														IdContext@14: TEST
															Simple_idContext@15: TEST
																TerminalNodeImpl@16: TEST
						TerminalNodeImpl@6: ;
	TerminalNodeImpl@1: <EOF>
```

How custom rule works:

1. Finds all nodes of type *Select_listContext*
2. As rule mode is set to *SINGLE* and *FailIfFound* - all nodes are checked if any of the nodes contain child where its text value is equal to * as text check mode is set to *Strict*. Then such nodes are reported as having issues. If mode would be set to *Contains*, then statements such as *SELECT 1 * 3* would be reported as well. 

### Creating more complex custom rule with distance ###

For example, if you wanted to create a rule requiring that each SELECT statement ends with semicolon, you could use the following definition:

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<rule>
    <key>Example1</key>
    <name>Select statement should end with semicolon</name>
    <internalKey>Example1</internalKey>
    <description>Select statement should end with semicolon&lt;h2&gt;Code examples&lt;/h2&gt;&lt;h3&gt;Non-compliant&lt;/h3&gt;&lt;pre&gt;&lt;code&gt;SELECT * from dbo.test where name like '%test%'&lt;/code&gt;&lt;/pre&gt;&lt;h3&gt;Compliant&lt;/h3&gt;&lt;pre&gt;&lt;code&gt;SELECT * from dbo.test where name like '%test%';&lt;/code&gt;&lt;/pre&gt;</description>
    <ruleImplementation>
        <names>
            <textItem>Select_statementContext</textItem>
        </names>
        <textToFind/>
        <parentRules/>
        <childrenRules>
            <ruleImplementation>
                <names>
                    <textItem>TerminalNodeImpl</textItem>
                </names>
                <textToFind>
                    <textItem>;</textItem>
                </textToFind>
                <parentRules/>
                <childrenRules/>
                <siblingsRules/>
                <usesRules/>
                <times>1</times>
                <distance>1</distance>
                <index>-1</index>
                <indexCheckType>Equals</indexCheckType>
                <distanceCheckType>Equals</distanceCheckType>
                <ruleMode>Default</ruleMode>
                <ruleMatchType>TextAndClass</ruleMatchType>
                <ruleResultType>FailIfNotFound</ruleResultType>
                <textCheckType>Strict</textCheckType>
                <violatingRulesCodeExamples/>
                <compliantRulesCodeExamples/>
            </ruleImplementation>
        </childrenRules>
        <siblingsRules/>
        <usesRules/>
        <times>1</times>
        <distance>0</distance>
        <index>0</index>
        <indexCheckType>Default</indexCheckType>
        <distanceCheckType>Default</distanceCheckType>
        <ruleMode>Default</ruleMode>
        <ruleMatchType>ClassOnly</ruleMatchType>
        <ruleResultType>Default</ruleResultType>
        <textCheckType>Default</textCheckType>
        <violatingRulesCodeExamples>
            <ruleCodeExample>SELECT * from dbo.test where name like '%test%'</ruleCodeExample>
        </violatingRulesCodeExamples>
        <compliantRulesCodeExamples>
            <ruleCodeExample>SELECT * from dbo.test where name like '%test%';</ruleCodeExample>
        </compliantRulesCodeExamples>
    </ruleImplementation>
</rule>
```

Tree for sample code (select * from dbo.test;):

```
Tsql_fileContext@0: SELECT*FROMDBO.TEST;<EOF>
	BatchContext@1: SELECT*FROMDBO.TEST;
		Sql_clausesContext@2: SELECT*FROMDBO.TEST;
			Sql_clauseContext@3: SELECT*FROMDBO.TEST;
				Dml_clauseContext@4: SELECT*FROMDBO.TEST;
					Select_statementContext@5: SELECT*FROMDBO.TEST;
						Query_expressionContext@6: SELECT*FROMDBO.TEST
							Query_specificationContext@7: SELECT*FROMDBO.TEST
								TerminalNodeImpl@8: SELECT
								Select_listContext@8: *
									Select_list_elemContext@9: *
										AsteriskContext@10: *
											TerminalNodeImpl@11: *
								TerminalNodeImpl@8: FROM
								Table_sourcesContext@8: DBO.TEST
									Table_sourceContext@9: DBO.TEST
										Table_source_item_joinedContext@10: DBO.TEST
											Table_source_itemContext@11: DBO.TEST
												Table_name_with_hintContext@12: DBO.TEST
													Table_nameContext@13: DBO.TEST
														IdContext@14: DBO
															Simple_idContext@15: DBO
																TerminalNodeImpl@16: DBO
														TerminalNodeImpl@14: .
														IdContext@14: TEST
															Simple_idContext@15: TEST
																TerminalNodeImpl@16: TEST
						TerminalNodeImpl@6: ;
	TerminalNodeImpl@1: <EOF>
```

Rule application:

```
Found candidate with text SELECT*FROMDBO.TESTWHERENAMELIKE'%TEST%'; of class Select_statementContext against rule: [Select_statementContext] classes and [] text
Found 1 candidates against [match: CLASS_ONLY, result: DEFAULT, distance 0, index: 0, indexCheck DEFAULT, distanceCheck: DEFAULT] rule: [Select_statementContext] classes and [] text
	Node matching rule: SELECT*FROMDBO.TESTWHERENAMELIKE'%TEST%'; Select_statementContext Distance: 0 Index: 0 Index2: 0
Found 1 candidates against [match: TEXT_AND_CLASS, result: FAIL_IF_NOT_FOUND, distance 1, index: -1, indexCheck EQUALS, distanceCheck: EQUALS] rule: [TerminalNodeImpl] classes and [;] text
	Node matching rule: ; TerminalNodeImpl Distance: 1 Index: 2 Index2: -1
```

As rule was set to FAIL_IF_NOT_FOUND as rule was satisfied, then no issue is reported.


### Supported names values ###
These are the following values availble for names values:

- AGGREGATE_WINDOWED_FUNCContext
- ANALYTIC_WINDOWED_FUNCContext
- Aggregate_windowed_functionContext
- AlgorithmContext
- All_distinct_expressionContext
- Alter_application_roleContext
- Alter_assemblyContext
- Alter_assembly_add_clauseContext
- Alter_assembly_asContext
- Alter_assembly_clauseContext
- Alter_assembly_client_file_clauseContext
- Alter_assembly_dropContext
- Alter_assembly_drop_clauseContext
- Alter_assembly_drop_multiple_filesContext
- Alter_assembly_file_bitsContext
- Alter_assembly_file_nameContext
- Alter_assembly_from_clauseContext
- Alter_assembly_from_clause_startContext
- Alter_assembly_startContext
- Alter_assembly_withContext
- Alter_assembly_with_clauseContext
- Alter_asssembly_add_clause_startContext
- Alter_asymmetric_keyContext
- Alter_asymmetric_key_startContext
- Alter_authorizationContext
- Alter_authorization_for_azure_dwContext
- Alter_authorization_for_parallel_dwContext
- Alter_authorization_for_sql_databaseContext
- Alter_authorization_startContext
- Alter_availability_groupContext
- Alter_availability_group_optionsContext
- Alter_availability_group_startContext
- Alter_certificateContext
- Alter_column_encryption_keyContext
- Alter_credentialContext
- Alter_cryptographic_providerContext
- Alter_databaseContext
- Alter_db_roleContext
- Alter_endpointContext
- Alter_external_data_sourceContext
- Alter_external_libraryContext
- Alter_external_resource_poolContext
- Alter_fulltext_catalogContext
- Alter_fulltext_stoplistContext
- Alter_login_azure_sqlContext
- Alter_login_azure_sql_dw_and_pdwContext
- Alter_login_sql_serverContext
- Alter_master_key_azure_sqlContext
- Alter_master_key_sql_serverContext
- Alter_message_typeContext
- Alter_partition_functionContext
- Alter_partition_schemeContext
- Alter_queueContext
- Alter_remote_service_bindingContext
- Alter_resource_governorContext
- Alter_schema_azure_sql_dw_and_pdwContext
- Alter_schema_sqlContext
- Alter_sequenceContext
- Alter_server_auditContext
- Alter_server_audit_specificationContext
- Alter_server_configurationContext
- Alter_server_roleContext
- Alter_server_role_pdwContext
- Alter_serviceContext
- Alter_service_master_keyContext
- Alter_symmetric_keyContext
- Alter_tableContext
- Alter_userContext
- Alter_user_azure_sqlContext
- Alter_workload_groupContext
- Analytic_windowed_functionContext
- Another_statementContext
- As_column_aliasContext
- As_table_aliasContext
- Assembly_optionContext
- Assignment_operatorContext
- AsteriskContext
- Asymmetric_key_optionContext
- Asymmetric_key_option_startContext
- Asymmetric_key_password_change_optionContext
- Authorization_granteeContext
- Auto_optionContext
- BINARY_CHECKSUMContext
- Backup_certificateContext
- Backup_databaseContext
- Backup_logContext
- Backup_master_keyContext
- Backup_service_master_keyContext
- Backup_statementContext
- BatchContext
- Begin_conversation_dialogContext
- Begin_conversation_timerContext
- Block_statementContext
- Bracket_expressionContext
- Break_statementContext
- Bulk_optionContext
- CASTContext
- CHECKSUMContext
- COALESCEContext
- CONVERTContext
- CURRENT_TIMESTAMPContext
- CURRENT_USERContext
- Case_expressionContext
- Cfl_statementContext
- Change_tableContext
- Change_tracking_optionContext
- Change_tracking_option_listContext
- Class_typeContext
- Class_type_for_azure_dwContext
- Class_type_for_parallel_dwContext
- Class_type_for_sql_databaseContext
- Client_assembly_specifierContext
- Close_keyContext
- ClusteredContext
- Colon_colonContext
- Column_aliasContext
- Column_alias_listContext
- Column_constraintContext
- Column_declarationContext
- Column_def_table_constraintContext
- Column_def_table_constraintsContext
- Column_definitionContext
- Column_elemContext
- Column_name_listContext
- Column_name_list_with_orderContext
- Common_table_expressionContext
- Comparison_operatorContext
- ConstantContext
- Constant_LOCAL_IDContext
- Constant_expressionContext
- Containment_optionContext
- Continue_statementContext
- Contract_nameContext
- Conversation_statementContext
- Create_application_roleContext
- Create_assemblyContext
- Create_asymmetric_keyContext
- Create_certificateContext
- Create_column_encryption_keyContext
- Create_column_master_keyContext
- Create_contractContext
- Create_credentialContext
- Create_cryptographic_providerContext
- Create_databaseContext
- Create_database_optionContext
- Create_db_roleContext
- Create_event_notificationContext
- Create_external_libraryContext
- Create_external_resource_poolContext
- Create_fulltext_catalogContext
- Create_fulltext_stoplistContext
- Create_indexContext
- Create_keyContext
- Create_login_azure_sqlContext
- Create_login_pdwContext
- Create_login_sql_serverContext
- Create_master_key_azure_sqlContext
- Create_master_key_sql_serverContext
- Create_or_alter_broker_priorityContext
- Create_or_alter_ddl_triggerContext
- Create_or_alter_dml_triggerContext
- Create_or_alter_event_sessionContext
- Create_or_alter_functionContext
- Create_or_alter_procedureContext
- Create_or_alter_triggerContext
- Create_queueContext
- Create_remote_service_bindingContext
- Create_resource_poolContext
- Create_routeContext
- Create_ruleContext
- Create_schemaContext
- Create_schema_azure_sql_dw_and_pdwContext
- Create_search_property_listContext
- Create_security_policyContext
- Create_sequenceContext
- Create_server_auditContext
- Create_server_audit_specificationContext
- Create_server_roleContext
- Create_serviceContext
- Create_statisticsContext
- Create_symmetric_keyContext
- Create_synonymContext
- Create_tableContext
- Create_typeContext
- Create_userContext
- Create_user_azure_sql_dwContext
- Create_viewContext
- Create_workload_groupContext
- Create_xml_schema_collectionContext
- Cursor_nameContext
- Cursor_optionContext
- Cursor_statementContext
- DATEADDContext
- DATEDIFFContext
- DATENAMEContext
- DATEPARTContext
- Data_typeContext
- Database_file_specContext
- Database_filestream_optionContext
- Database_mirroring_optionContext
- Database_optionspecContext
- Date_correlation_optimization_optionContext
- Date_optionsContext
- Db_encryption_optionContext
- Db_state_optionContext
- Db_update_optionContext
- Db_user_access_optionContext
- Dbcc_clauseContext
- Dbcc_optionsContext
- Ddl_clauseContext
- Ddl_objectContext
- Ddl_trigger_operationContext
- Declare_cursorContext
- Declare_localContext
- Declare_set_cursor_commonContext
- Declare_set_cursor_common_partialContext
- Declare_statementContext
- Decryption_mechanismContext
- Default_valueContext
- Delayed_durability_optionContext
- Delete_statementContext
- Delete_statement_fromContext
- Derived_tableContext
- Disable_triggerContext
- Dml_clauseContext
- Dml_trigger_operationContext
- Dml_trigger_optionContext
- Drop_aggregateContext
- Drop_application_roleContext
- Drop_assemblyContext
- Drop_asymmetric_keyContext
- Drop_availability_groupContext
- Drop_backward_compatible_indexContext
- Drop_broker_priorityContext
- Drop_certificateContext
- Drop_column_encryption_keyContext
- Drop_column_master_keyContext
- Drop_contractContext
- Drop_credentialContext
- Drop_cryptograhic_providerContext
- Drop_databaseContext
- Drop_database_audit_specificationContext
- Drop_database_scoped_credentialContext
- Drop_db_roleContext
- Drop_ddl_triggerContext
- Drop_defaultContext
- Drop_dml_triggerContext
- Drop_endpointContext
- Drop_event_notificationsContext
- Drop_event_sessionContext
- Drop_external_data_sourceContext
- Drop_external_file_formatContext
- Drop_external_libraryContext
- Drop_external_resource_poolContext
- Drop_external_tableContext
- Drop_fulltext_catalogContext
- Drop_fulltext_indexContext
- Drop_fulltext_stoplistContext
- Drop_functionContext
- Drop_indexContext
- Drop_loginContext
- Drop_master_keyContext
- Drop_message_typeContext
- Drop_partition_functionContext
- Drop_partition_schemeContext
- Drop_procedureContext
- Drop_queueContext
- Drop_relational_or_xml_or_spatial_indexContext
- Drop_remote_service_bindingContext
- Drop_resource_poolContext
- Drop_routeContext
- Drop_ruleContext
- Drop_schemaContext
- Drop_search_property_listContext
- Drop_security_policyContext
- Drop_sequenceContext
- Drop_server_auditContext
- Drop_server_audit_specificationContext
- Drop_server_roleContext
- Drop_serviceContext
- Drop_signatureContext
- Drop_statisticsContext
- Drop_statistics_name_azure_dw_and_pdwContext
- Drop_symmetric_keyContext
- Drop_synonymContext
- Drop_tableContext
- Drop_triggerContext
- Drop_typeContext
- Drop_userContext
- Drop_viewContext
- Drop_workload_groupContext
- Drop_xml_schema_collectionContext
- Empty_statementContext
- Enable_triggerContext
- Encryption_mechanismContext
- End_conversationContext
- Entity_nameContext
- Entity_name_for_azure_dwContext
- Entity_name_for_parallel_dwContext
- Entity_toContext
- Event_session_predicate_expressionContext
- Event_session_predicate_factorContext
- Event_session_predicate_leafContext
- Execute_clauseContext
- Execute_statementContext
- Execute_statement_argContext
- Execute_var_stringContext
- Exist_methodContext
- Existing_keysContext
- ExpressionContext
- Expression_elemContext
- Expression_listContext
- External_access_optionContext
- Fetch_cursorContext
- File_directory_path_separatorContext
- File_groupContext
- File_pathContext
- File_sizeContext
- File_specContext
- For_clauseContext
- Full_column_nameContext
- Full_column_name_listContext
- Full_table_nameContext
- Func_body_returns_scalarContext
- Func_body_returns_selectContext
- Func_body_returns_tableContext
- Func_proc_nameContext
- Function_callContext
- Function_optionContext
- GETDATEContext
- GETUTCDATEContext
- Generate_new_keysContext
- Get_conversationContext
- Go_statementContext
- Goto_statementContext
- Grant_permissionContext
- Group_by_itemContext
- Hadr_optionsContext
- HostContext
- IDENTITYContext
- ISNULLContext
- IdContext
- If_statementContext
- Index_optionContext
- Index_optionsContext
- Index_valueContext
- Insert_statementContext
- Insert_statement_valueContext
- Insert_with_table_hintsContext
- Join_partContext
- Key_optionsContext
- Local_driveContext
- Local_fileContext
- MIN_ACTIVE_ROWVERSIONContext
- Merge_matchedContext
- Merge_not_matchedContext
- Merge_statementContext
- Message_statementContext
- Mirroring_host_port_seperatorContext
- Mirroring_partnerContext
- Mirroring_set_optionContext
- Mirroring_witnessContext
- Mixed_page_allocation_optionContext
- Modify_methodContext
- Multiple_local_file_startContext
- Multiple_local_filesContext
- NULLIFContext
- Network_computerContext
- Network_file_shareContext
- Network_file_startContext
- Nodes_methodContext
- Null_notnullContext
- Null_or_defaultContext
- On_deleteContext
- On_offContext
- On_updateContext
- Open_keyContext
- Open_xmlContext
- OpendatasourceContext
- OpenqueryContext
- Optimize_for_argContext
- OptionContext
- Option_clauseContext
- Order_by_clauseContext
- Order_by_expressionContext
- Output_clauseContext
- Output_column_nameContext
- Output_dml_list_elemContext
- Over_clauseContext
- Parameterization_optionContext
- Partner_optionContext
- Partner_serverContext
- Partner_server_tcp_prefixContext
- Pivot_clauseContext
- Port_numberContext
- PredicateContext
- Primitive_expressionContext
- Print_statementContext
- Private_key_optionsContext
- Procedure_optionContext
- Procedure_paramContext
- Query_expressionContext
- Query_methodContext
- Query_specificationContext
- Queue_actionContext
- Queue_idContext
- Queue_rebuild_optionsContext
- Queue_settingsContext
- RANKING_WINDOWED_FUNCContext
- Raiseerror_statementContext
- Ranking_windowed_functionContext
- Receive_statementContext
- Recovery_optionContext
- Return_statementContext
- Row_or_range_clauseContext
- Rowset_functionContext
- Rowset_function_limitedContext
- SCALAR_FUNCTIONContext
- SESSION_USERContext
- STUFFContext
- SYSTEM_USERContext
- Scalar_function_nameContext
- Schema_declarationContext
- Search_conditionContext
- Search_condition_andContext
- Search_condition_listContext
- Search_condition_notContext
- Security_statementContext
- Select_listContext
- Select_list_elemContext
- Select_statementContext
- Send_conversationContext
- Service_broker_optionContext
- Service_nameContext
- Set_specialContext
- Set_statementContext
- SignContext
- Simple_idContext
- Simple_nameContext
- Snapshot_optionContext
- Sql_clauseContext
- Sql_clausesContext
- Sql_optionContext
- Sql_unionContext
- SubqueryContext
- Switch_search_condition_sectionContext
- Switch_sectionContext
- Table_aliasContext
- Table_constraintContext
- Table_hintContext
- Table_nameContext
- Table_name_with_hintContext
- Table_optionsContext
- Table_sourceContext
- Table_source_itemContext
- Table_source_item_joinedContext
- Table_sourcesContext
- Table_type_definitionContext
- Table_value_constructorContext
- Target_recovery_time_optionContext
- TerminationContext
- Throw_error_numberContext
- Throw_messageContext
- Throw_stateContext
- Throw_statementContext
- TimeContext
- Top_clauseContext
- Top_countContext
- Top_percentContext
- Transaction_statementContext
- Truncate_tableContext
- Try_catch_statementContext
- Tsql_fileContext
- Udt_elemContext
- Udt_method_argumentsContext
- Unary_operator_expressionContext
- Unpivot_clauseContext
- Update_elemContext
- Update_statementContext
- Use_statementContext
- Value_methodContext
- View_attributeContext
- Waitfor_conversationContext
- Waitfor_statementContext
- While_statementContext
- Window_frame_boundContext
- Window_frame_extentContext
- Window_frame_followingContext
- Window_frame_precedingContext
- With_expressionContext
- With_table_hintsContext
- Witness_optionContext
- Witness_partner_equalContext
- Witness_serverContext
- XML_DATA_TYPE_FUNCContext
- Xml_common_directivesContext
- Xml_data_type_methodsContext
- Xml_schema_collectionContext
- Xml_type_definitionContext