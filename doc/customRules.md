# Wiki #
## Custom rules ##
Currently, plugin support naive implementation of ability to match code against custom rules defined in xml files with specific format. Below are the details to start using and creating custom rules.

Currently, plugin has examples of 8 sample rules defined:
 - SELECT * is used
 - WAITFOR is used

### Usage ###
To use custom rules, you need:

 - Create an xml file with custom rules defined, there is an example in the Example project folder. Each file defines a single repository.
 - Deploy custom rules file/files to SonarQube server's filesystem, i.e. copy file to **C:\sonar\** folder
 - Update **sonar.tsql.customrules.paths** settings in SonarQube server for TSQL plugin (please note, that on SonarQube server paths need to be absolute), i.e.: 

	 - sonar.tsql.customrules.paths=C:\sonar-rules\
	 - sonar.tsql.customrules.paths=C:\sonar-rules\test.customRules,C:\sonar-rules\test2.customRules
- Restart SonarQube server
- Copy same rules files to the machine from which you are running analysis. You could copy to same place as in settings on the SonarQube server or you can keep then together with code, then you can specify either relative or absolute path to the rules, i.e.:
    - sonar.tsql.customrules.paths=./customRules

- Run code analysis

### Creating custom rules ###
Custom rules are defined using xml format. Each file should be defining a single repository using the following format. Attributs repoName and repoKey defines repository's name and key used by SonarQube. Key should be unique.

Below is an example of a custom rule reporting all WAITFOR statements found.

```
<sql-rules repoName="Demo rules" repoKey="tsqlDemoRepo">
  <rule>
        <key>C001</key>
        <name>Waitfor is used.</name>
        <internalKey>C001</internalKey>
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
</sql-rules>
```

Mandatory fields for rule:

- key - used to report issues, should be unique among repositories
- name - rule name
- internalKey - used to report issues, should be unique among repositories
- description - rule description
- ruleImplementation - defines how to detect code against which rules will be applied. There are different options for this item:
	- names - defines a list of TSQL parser classes on which rule will be applied. Full list is specified below.
	- textToFind - defines custom text wich will be looked for in the applicable nodes
	- parentRules - defines rules which will be applied for node's parents 
	- childrenRules - defines rules which will be applied for node's children
	- siblingsRules- defines rules which will be applied for node's siblings
	- usesRules - defines rules which will be applied for node's similar to current node, i.e. contains same cursor name
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
   
### Creating more complex custom rules ###

Finding issues with select statements containing asterisks:

```
 <rule>
        <key>C002</key>
        <name>SELECT * is used</name>
        <internalKey>C002</internalKey>
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
            </compliantRulesCodeExamples>
        </ruleImplementation>
    </rule>
```

Rules application:

1. Rule (1) instructs for plugin to find all select statements. If it  had 0 children rules defined, then no code issues would be reported as rule mode,match, check and result types are set to Default (skip) mode.
2. Rule (2) intructs to check each select statement's child and check if there exists any child whose text is equals to *, as textCheckType is set to Strict mode then values such as (1 * 3) will not be reported, if mode would be set to Contains - then there could be false positives reported.

### Supported names values ###

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
