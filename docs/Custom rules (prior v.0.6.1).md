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
	- times - if ruleResultType is set to FailIfLessFound or FailIfMoreFound then this value is compared against actual nodes violating that rule and failed accordingly
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
   
Schema can be found at **/sonar-tsql-plugin/src/main/resources/schemas/customRules.xsd**.

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

To start building custom rules, you can also look into example at ** sonar-tsql-plugin/Example/customRules/myExampleRepo.customRules**.

In addition to this, there is a small command line tool allowing to verify rules [rulesHelper.jar](https://github.com/gretard/sonar-tsql-plugin/releases/download/0.6.0-alpha1/rulesHelper.jar "rulesHelper.jar") against code in the violatingRulesCodeExamples/compliantRulesCodeExamples sections. And also it allwos to print tree of parsed T-SQL code.

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
<sql-rules repoName="Demo rules" repoKey="tsqlDemoRepo">
  <rule>
        <key>CC001</key>
        <name>Waitfor is used.</name>
        <internalKey>CC001</internalKey>
        <description>Waitfor is used</description>
        <ruleImplementation>
            <names>
                <textItem>Waitfor_statementContext</textItem> <!--(1) -->
            </names>
            <textToFind/>
            <parentRules/>
            <childrenRules/>
            <siblingsRules/>
            <usesRules/>
            <ruleViolationMessage>Waitfor is used.</ruleViolationMessage>
            <times>0</times>
            <ruleMode>Single</ruleMode> <!--(2) -->
            <ruleMatchType>ClassOnly</ruleMatchType>
            <ruleResultType>FailIfFound</ruleResultType> <!--(2) -->
            <textCheckType>Default</textCheckType>
            <violatingRulesCodeExamples/>
            <compliantRulesCodeExamples/>
        </ruleImplementation>
    </rule>
</sql-rules>
```

For example, statement *WAITFOR '10:00:00';* will be parsed as:

```
Tsql_fileContext: WAITFOR'10:00:00';<EOF>
	BatchContext: WAITFOR'10:00:00';
		Sql_clausesContext: WAITFOR'10:00:00';
			Sql_clauseContext: WAITFOR'10:00:00';
				Waitfor_statementContext: WAITFOR'10:00:00';
					TerminalNodeImpl: WAITFOR
					Primitive_expressionContext: '10:00:00'
						ConstantContext: '10:00:00'
							TerminalNodeImpl: '10:00:00'
					TerminalNodeImpl: ;
	TerminalNodeImpl: <EOF>
```


How custom rules work:

1. Find all nodes of type *Waitfor_statementContext*
2. As rule mode is set to *SINGLE* and *FailIfFound* - all lines where  node types *Waitfor_statementContext* were found will be reported as violating this rule. 




### Creating more complex custom rules ###

Below is an example of a rule finding issues where select statements contain asterisks.

```
 <rule>
        <key>CC002</key>
        <name>SELECT * is used</name>
        <internalKey>CC002</internalKey>
        <description>SELECT * is used. Please list names.</description>
        <ruleImplementation>
            <names>
                <textItem>Select_listContext</textItem> <!--(1) -->
            </names>
            <textToFind/>
            <parentRules/>
            <childrenRules> <!--(2) -->
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
```
For example, statement *SELECT * FROM dbo.test;* will be parsed as:

```
Tsql_fileContext: SELECT*FROMdbo.test;<EOF>
	BatchContext: SELECT*FROMdbo.test;
		Sql_clausesContext: SELECT*FROMdbo.test;
			Sql_clauseContext: SELECT*FROMdbo.test;
				Dml_clauseContext: SELECT*FROMdbo.test;
					Select_statementContext: SELECT*FROMdbo.test;
						Query_expressionContext: SELECT*FROMdbo.test
							Query_specificationContext: SELECT*FROMdbo.test
								TerminalNodeImpl: SELECT
								Select_listContext: *
									Select_list_elemContext: *
										TerminalNodeImpl: *
								TerminalNodeImpl: FROM
								Table_sourcesContext: dbo.test
									Table_sourceContext: dbo.test
										Table_source_item_joinedContext: dbo.test
											Table_source_itemContext: dbo.test
												Table_name_with_hintContext: dbo.test
													Table_nameContext: dbo.test
														IdContext: dbo
															Simple_idContext: dbo
																TerminalNodeImpl: dbo
														TerminalNodeImpl: .
														IdContext: test
															Simple_idContext: test
																TerminalNodeImpl: test
						TerminalNodeImpl: ;
	TerminalNodeImpl: <EOF>s
```

How custom rule works:

1. Finds all nodes of type *Select_listContext*
2. As rule mode is set to *SINGLE* and *FailIfFound* - all nodes are checked if any of the nodes contain child where its text value is equal to * as text check mode is set to *Strict*. Then such nodes are reported as having issues. If mode would be set to *Contains*, then statements such as *SELECT 1 * 3* would be reported as well. 

### Supported names values ###
These are the following values availble for names values:

- Aggregate_windowed_functionContext
- AlgorithmContext
- All_distinct_expressionContext
- Alter_databaseContext
- Alter_queueContext
- Alter_tableContext
- Another_statementContext
- As_table_aliasContext
- Assignment_operatorContext
- Asssignment_operator_expressionContext
- Auto_optionContext
- BatchContext
- Begin_conversation_dialogContext
- Begin_conversation_timerContext
- Binary_operator_expressionContext
- Block_statementContext
- Bracket_expressionContext
- Break_statementContext
- Bulk_optionContext
- Case_expressionContext
- Cfl_statementContext
- Change_tableContext
- Change_tracking_optionContext
- Change_tracking_option_listContext
- Close_keyContext
- ClusteredContext
- Column_aliasContext
- Column_alias_listContext
- Column_constraintContext
- Column_declarationContext
- Column_def_table_constraintContext
- Column_def_table_constraintsContext
- Column_definitionContext
- Column_name_listContext
- Column_name_list_with_orderContext
- Column_ref_expressionContext
- Common_table_expressionContext
- Comparison_operatorContext
- ConstantContext
- Constant_LOCAL_IDContext
- Constant_expressionContext
- Containment_optionContext
- Continue_statementContext
- Contract_nameContext
- Conversation_statementContext
- Create_certificateContext
- Create_contractContext
- Create_databaseContext
- Create_database_optionContext
- Create_indexContext
- Create_keyContext
- Create_or_alter_functionContext
- Create_or_alter_procedureContext
- Create_or_alter_triggerContext
- Create_queueContext
- Create_statisticsContext
- Create_tableContext
- Create_typeContext
- Create_viewContext
- Cursor_nameContext
- Cursor_optionContext
- Cursor_statementContext
- Data_typeContext
- Database_file_specContext
- Database_filestream_optionContext
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
- Ddl_triggerContext
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
- Dml_clauseContext
- Dml_triggerContext
- Dml_trigger_operationContext
- Dml_trigger_optionContext
- Drop_ddl_triggerContext
- Drop_dml_triggerContext
- Drop_functionContext
- Drop_indexContext
- Drop_procedureContext
- Drop_statisticsContext
- Drop_tableContext
- Drop_triggerContext
- Drop_typeContext
- Drop_viewContext
- Empty_statementContext
- Encryption_mechanismContext
- End_conversationContext
- Execute_clauseContext
- Execute_statementContext
- Execute_statement_argContext
- Execute_var_stringContext
- Exist_methodContext
- Existing_keysContext
- ExpressionContext
- Expression_listContext
- External_access_optionContext
- Fetch_cursorContext
- File_groupContext
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
- Function_call_expressionContext
- Function_optionContext
- Generate_new_keysContext
- Get_conversationContext
- Go_statementContext
- Goto_statementContext
- Grant_permissionContext
- Group_by_itemContext
- Hadr_optionsContext
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
- Merge_matchedContext
- Merge_not_matchedContext
- Merge_statementContext
- Message_statementContext
- Mixed_page_allocation_optionContext
- Modify_methodContext
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
- Over_clause_expressionContext
- Parameterization_optionContext
- ParserRuleContext
- Pivot_clauseContext
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
- Raiseerror_statementbContext
- Ranking_windowed_functionContext
- Receive_statementContext
- Recovery_optionContext
- Return_statementContext
- Row_or_range_clauseContext
- Rowset_functionContext
- Rowset_function_limitedContext
- RuleContext
- RuleNode
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
- SubqueryContext
- Subquery_expressionContext
- Switch_search_condition_sectionContext
- Switch_sectionContext
- Table_aliasContext
- Table_constraintContext
- Table_hintContext
- Table_nameContext
- Table_name_with_hintContext
- Table_optionContext
- Table_optionsContext
- Table_sourceContext
- Table_source_itemContext
- Table_source_item_joinedContext
- Table_sourcesContext
- Table_type_definitionContext
- Table_value_constructorContext
- Target_recovery_time_optionContext
- TerminationContext
- Throw_statementContext
- TimeContext
- Top_clauseContext
- Top_countContext
- Top_percentContext
- Transaction_statementContext
- Try_catch_statementContext
- Tsql_fileContext
- Unary_operator_expressionContext
- UnionContext
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
- Xml_common_directivesContext
- Xml_data_type_methodsContext
- Xml_schema_collectionContext
- Xml_type_definitionContext
