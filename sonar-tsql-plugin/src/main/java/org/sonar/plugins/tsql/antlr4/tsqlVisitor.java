// Generated from org\sonar\plugins\tsql\antlr4\tsql.g4 by ANTLR 4.7
package org.sonar.plugins.tsql.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link tsqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface tsqlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link tsqlParser#tsql_file}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTsql_file(tsqlParser.Tsql_fileContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#batch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBatch(tsqlParser.BatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#sql_clauses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_clauses(tsqlParser.Sql_clausesContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#sql_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_clause(tsqlParser.Sql_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#dml_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDml_clause(tsqlParser.Dml_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#ddl_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDdl_clause(tsqlParser.Ddl_clauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code block_statement}
	 * labeled alternative in {@link tsqlParser#cfl_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock_statement(tsqlParser.Block_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code break_statement}
	 * labeled alternative in {@link tsqlParser#cfl_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak_statement(tsqlParser.Break_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code continue_statement}
	 * labeled alternative in {@link tsqlParser#cfl_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue_statement(tsqlParser.Continue_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code goto_statement}
	 * labeled alternative in {@link tsqlParser#cfl_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGoto_statement(tsqlParser.Goto_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code if_statement}
	 * labeled alternative in {@link tsqlParser#cfl_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_statement(tsqlParser.If_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code return_statement}
	 * labeled alternative in {@link tsqlParser#cfl_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_statement(tsqlParser.Return_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code throw_statement}
	 * labeled alternative in {@link tsqlParser#cfl_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrow_statement(tsqlParser.Throw_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code try_catch_statement}
	 * labeled alternative in {@link tsqlParser#cfl_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTry_catch_statement(tsqlParser.Try_catch_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code waitfor_statement}
	 * labeled alternative in {@link tsqlParser#cfl_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWaitfor_statement(tsqlParser.Waitfor_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code while_statement}
	 * labeled alternative in {@link tsqlParser#cfl_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_statement(tsqlParser.While_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code print_statement}
	 * labeled alternative in {@link tsqlParser#cfl_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrint_statement(tsqlParser.Print_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code raiseerror_statementb}
	 * labeled alternative in {@link tsqlParser#cfl_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRaiseerror_statementb(tsqlParser.Raiseerror_statementbContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#empty_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmpty_statement(tsqlParser.Empty_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#another_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnother_statement(tsqlParser.Another_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#create_queue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_queue(tsqlParser.Create_queueContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#queue_settings}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueue_settings(tsqlParser.Queue_settingsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#alter_queue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_queue(tsqlParser.Alter_queueContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#queue_action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueue_action(tsqlParser.Queue_actionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#queue_rebuild_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueue_rebuild_options(tsqlParser.Queue_rebuild_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#create_contract}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_contract(tsqlParser.Create_contractContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#conversation_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConversation_statement(tsqlParser.Conversation_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#message_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessage_statement(tsqlParser.Message_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#merge_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_statement(tsqlParser.Merge_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#merge_matched}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_matched(tsqlParser.Merge_matchedContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#merge_not_matched}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMerge_not_matched(tsqlParser.Merge_not_matchedContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#delete_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_statement(tsqlParser.Delete_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#delete_statement_from}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_statement_from(tsqlParser.Delete_statement_fromContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#insert_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_statement(tsqlParser.Insert_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#insert_statement_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_statement_value(tsqlParser.Insert_statement_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#receive_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReceive_statement(tsqlParser.Receive_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#select_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_statement(tsqlParser.Select_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#time}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTime(tsqlParser.TimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#update_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_statement(tsqlParser.Update_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#output_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutput_clause(tsqlParser.Output_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#output_dml_list_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutput_dml_list_elem(tsqlParser.Output_dml_list_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#output_column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutput_column_name(tsqlParser.Output_column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#create_database}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_database(tsqlParser.Create_databaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#create_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_index(tsqlParser.Create_indexContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#create_or_alter_procedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_or_alter_procedure(tsqlParser.Create_or_alter_procedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#create_or_alter_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_or_alter_trigger(tsqlParser.Create_or_alter_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#dml_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDml_trigger(tsqlParser.Dml_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#dml_trigger_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDml_trigger_option(tsqlParser.Dml_trigger_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#dml_trigger_operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDml_trigger_operation(tsqlParser.Dml_trigger_operationContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#ddl_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDdl_trigger(tsqlParser.Ddl_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#ddl_trigger_operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDdl_trigger_operation(tsqlParser.Ddl_trigger_operationContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#create_or_alter_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_or_alter_function(tsqlParser.Create_or_alter_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#func_body_returns_select}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_body_returns_select(tsqlParser.Func_body_returns_selectContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#func_body_returns_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_body_returns_table(tsqlParser.Func_body_returns_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#func_body_returns_scalar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_body_returns_scalar(tsqlParser.Func_body_returns_scalarContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#procedure_param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure_param(tsqlParser.Procedure_paramContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#procedure_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure_option(tsqlParser.Procedure_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#function_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_option(tsqlParser.Function_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#create_statistics}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_statistics(tsqlParser.Create_statisticsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#create_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_table(tsqlParser.Create_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#table_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_options(tsqlParser.Table_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#table_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_option(tsqlParser.Table_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#create_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_view(tsqlParser.Create_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#view_attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitView_attribute(tsqlParser.View_attributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#alter_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_table(tsqlParser.Alter_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#alter_database}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_database(tsqlParser.Alter_databaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#database_optionspec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_optionspec(tsqlParser.Database_optionspecContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#auto_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAuto_option(tsqlParser.Auto_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#change_tracking_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChange_tracking_option(tsqlParser.Change_tracking_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#change_tracking_option_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChange_tracking_option_list(tsqlParser.Change_tracking_option_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#containment_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainment_option(tsqlParser.Containment_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#cursor_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_option(tsqlParser.Cursor_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#date_correlation_optimization_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDate_correlation_optimization_option(tsqlParser.Date_correlation_optimization_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#db_encryption_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDb_encryption_option(tsqlParser.Db_encryption_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#db_state_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDb_state_option(tsqlParser.Db_state_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#db_update_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDb_update_option(tsqlParser.Db_update_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#db_user_access_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDb_user_access_option(tsqlParser.Db_user_access_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#delayed_durability_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelayed_durability_option(tsqlParser.Delayed_durability_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#external_access_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternal_access_option(tsqlParser.External_access_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#hadr_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHadr_options(tsqlParser.Hadr_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#mixed_page_allocation_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMixed_page_allocation_option(tsqlParser.Mixed_page_allocation_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#parameterization_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterization_option(tsqlParser.Parameterization_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#recovery_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecovery_option(tsqlParser.Recovery_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#service_broker_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitService_broker_option(tsqlParser.Service_broker_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#snapshot_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSnapshot_option(tsqlParser.Snapshot_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#sql_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_option(tsqlParser.Sql_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#target_recovery_time_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTarget_recovery_time_option(tsqlParser.Target_recovery_time_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#termination}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermination(tsqlParser.TerminationContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#drop_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_index(tsqlParser.Drop_indexContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#drop_procedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_procedure(tsqlParser.Drop_procedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#drop_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_trigger(tsqlParser.Drop_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#drop_dml_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_dml_trigger(tsqlParser.Drop_dml_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#drop_ddl_trigger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_ddl_trigger(tsqlParser.Drop_ddl_triggerContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#drop_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_function(tsqlParser.Drop_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#drop_statistics}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_statistics(tsqlParser.Drop_statisticsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#drop_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_table(tsqlParser.Drop_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#drop_view}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_view(tsqlParser.Drop_viewContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#create_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_type(tsqlParser.Create_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#drop_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_type(tsqlParser.Drop_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#rowset_function_limited}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowset_function_limited(tsqlParser.Rowset_function_limitedContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#openquery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpenquery(tsqlParser.OpenqueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#opendatasource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpendatasource(tsqlParser.OpendatasourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#declare_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_statement(tsqlParser.Declare_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#cursor_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_statement(tsqlParser.Cursor_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#execute_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecute_statement(tsqlParser.Execute_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#execute_statement_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecute_statement_arg(tsqlParser.Execute_statement_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#execute_var_string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecute_var_string(tsqlParser.Execute_var_stringContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#security_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSecurity_statement(tsqlParser.Security_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#create_certificate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_certificate(tsqlParser.Create_certificateContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#existing_keys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExisting_keys(tsqlParser.Existing_keysContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#private_key_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrivate_key_options(tsqlParser.Private_key_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#generate_new_keys}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenerate_new_keys(tsqlParser.Generate_new_keysContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#date_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDate_options(tsqlParser.Date_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#open_key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpen_key(tsqlParser.Open_keyContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#close_key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClose_key(tsqlParser.Close_keyContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#create_key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_key(tsqlParser.Create_keyContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#key_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_options(tsqlParser.Key_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#algorithm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlgorithm(tsqlParser.AlgorithmContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#encryption_mechanism}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEncryption_mechanism(tsqlParser.Encryption_mechanismContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#decryption_mechanism}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecryption_mechanism(tsqlParser.Decryption_mechanismContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#grant_permission}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrant_permission(tsqlParser.Grant_permissionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#set_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_statement(tsqlParser.Set_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#transaction_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction_statement(tsqlParser.Transaction_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#go_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGo_statement(tsqlParser.Go_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#use_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUse_statement(tsqlParser.Use_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#dbcc_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbcc_clause(tsqlParser.Dbcc_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#dbcc_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDbcc_options(tsqlParser.Dbcc_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#execute_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecute_clause(tsqlParser.Execute_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#declare_local}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_local(tsqlParser.Declare_localContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#table_type_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_type_definition(tsqlParser.Table_type_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#xml_type_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_type_definition(tsqlParser.Xml_type_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#xml_schema_collection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_schema_collection(tsqlParser.Xml_schema_collectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#column_def_table_constraints}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_def_table_constraints(tsqlParser.Column_def_table_constraintsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#column_def_table_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_def_table_constraint(tsqlParser.Column_def_table_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#column_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_definition(tsqlParser.Column_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#column_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_constraint(tsqlParser.Column_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#table_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_constraint(tsqlParser.Table_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#on_delete}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_delete(tsqlParser.On_deleteContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#on_update}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_update(tsqlParser.On_updateContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#index_options}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_options(tsqlParser.Index_optionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#index_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_option(tsqlParser.Index_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#declare_cursor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_cursor(tsqlParser.Declare_cursorContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#declare_set_cursor_common}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_set_cursor_common(tsqlParser.Declare_set_cursor_commonContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#declare_set_cursor_common_partial}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare_set_cursor_common_partial(tsqlParser.Declare_set_cursor_common_partialContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#fetch_cursor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFetch_cursor(tsqlParser.Fetch_cursorContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#set_special}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_special(tsqlParser.Set_specialContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#constant_LOCAL_ID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant_LOCAL_ID(tsqlParser.Constant_LOCAL_IDContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binary_operator_expression}
	 * labeled alternative in {@link tsqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinary_operator_expression(tsqlParser.Binary_operator_expressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primitive_expression}
	 * labeled alternative in {@link tsqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitive_expression(tsqlParser.Primitive_expressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code asssignment_operator_expression}
	 * labeled alternative in {@link tsqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsssignment_operator_expression(tsqlParser.Asssignment_operator_expressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bracket_expression}
	 * labeled alternative in {@link tsqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracket_expression(tsqlParser.Bracket_expressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unary_operator_expression}
	 * labeled alternative in {@link tsqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_operator_expression(tsqlParser.Unary_operator_expressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code function_call_expression}
	 * labeled alternative in {@link tsqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_call_expression(tsqlParser.Function_call_expressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code case_expression}
	 * labeled alternative in {@link tsqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_expression(tsqlParser.Case_expressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code column_ref_expression}
	 * labeled alternative in {@link tsqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_ref_expression(tsqlParser.Column_ref_expressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code subquery_expression}
	 * labeled alternative in {@link tsqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery_expression(tsqlParser.Subquery_expressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code over_clause_expression}
	 * labeled alternative in {@link tsqlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOver_clause_expression(tsqlParser.Over_clause_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#constant_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant_expression(tsqlParser.Constant_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#subquery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery(tsqlParser.SubqueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#with_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_expression(tsqlParser.With_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#common_table_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommon_table_expression(tsqlParser.Common_table_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#update_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_elem(tsqlParser.Update_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#search_condition_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearch_condition_list(tsqlParser.Search_condition_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#search_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearch_condition(tsqlParser.Search_conditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#search_condition_and}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearch_condition_and(tsqlParser.Search_condition_andContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#search_condition_not}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearch_condition_not(tsqlParser.Search_condition_notContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicate(tsqlParser.PredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#query_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery_expression(tsqlParser.Query_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#union}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnion(tsqlParser.UnionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#query_specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery_specification(tsqlParser.Query_specificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#top_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTop_clause(tsqlParser.Top_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#top_percent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTop_percent(tsqlParser.Top_percentContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#top_count}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTop_count(tsqlParser.Top_countContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#order_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrder_by_clause(tsqlParser.Order_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#for_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_clause(tsqlParser.For_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#xml_common_directives}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_common_directives(tsqlParser.Xml_common_directivesContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#order_by_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrder_by_expression(tsqlParser.Order_by_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#group_by_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_by_item(tsqlParser.Group_by_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#option_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOption_clause(tsqlParser.Option_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOption(tsqlParser.OptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#optimize_for_arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptimize_for_arg(tsqlParser.Optimize_for_argContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#select_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_list(tsqlParser.Select_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#select_list_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_list_elem(tsqlParser.Select_list_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#table_sources}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_sources(tsqlParser.Table_sourcesContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#table_source}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_source(tsqlParser.Table_sourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#table_source_item_joined}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_source_item_joined(tsqlParser.Table_source_item_joinedContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#table_source_item}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_source_item(tsqlParser.Table_source_itemContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#open_xml}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpen_xml(tsqlParser.Open_xmlContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#schema_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchema_declaration(tsqlParser.Schema_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#column_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_declaration(tsqlParser.Column_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#change_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChange_table(tsqlParser.Change_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#join_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_part(tsqlParser.Join_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#pivot_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPivot_clause(tsqlParser.Pivot_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#unpivot_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnpivot_clause(tsqlParser.Unpivot_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#full_column_name_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFull_column_name_list(tsqlParser.Full_column_name_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#table_name_with_hint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_name_with_hint(tsqlParser.Table_name_with_hintContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#rowset_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRowset_function(tsqlParser.Rowset_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#bulk_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBulk_option(tsqlParser.Bulk_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#derived_table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDerived_table(tsqlParser.Derived_tableContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#function_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_call(tsqlParser.Function_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#xml_data_type_methods}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXml_data_type_methods(tsqlParser.Xml_data_type_methodsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#value_method}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_method(tsqlParser.Value_methodContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#query_method}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery_method(tsqlParser.Query_methodContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#exist_method}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExist_method(tsqlParser.Exist_methodContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#modify_method}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModify_method(tsqlParser.Modify_methodContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#nodes_method}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodes_method(tsqlParser.Nodes_methodContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#switch_section}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitch_section(tsqlParser.Switch_sectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#switch_search_condition_section}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitch_search_condition_section(tsqlParser.Switch_search_condition_sectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#as_table_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAs_table_alias(tsqlParser.As_table_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#table_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_alias(tsqlParser.Table_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#with_table_hints}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWith_table_hints(tsqlParser.With_table_hintsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#insert_with_table_hints}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_with_table_hints(tsqlParser.Insert_with_table_hintsContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#table_hint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_hint(tsqlParser.Table_hintContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#index_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex_value(tsqlParser.Index_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#column_alias_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_alias_list(tsqlParser.Column_alias_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#column_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_alias(tsqlParser.Column_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#table_value_constructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_value_constructor(tsqlParser.Table_value_constructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#expression_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_list(tsqlParser.Expression_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#ranking_windowed_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRanking_windowed_function(tsqlParser.Ranking_windowed_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregate_windowed_function(tsqlParser.Aggregate_windowed_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#all_distinct_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAll_distinct_expression(tsqlParser.All_distinct_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#over_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOver_clause(tsqlParser.Over_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#row_or_range_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRow_or_range_clause(tsqlParser.Row_or_range_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#window_frame_extent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_frame_extent(tsqlParser.Window_frame_extentContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#window_frame_bound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_frame_bound(tsqlParser.Window_frame_boundContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#window_frame_preceding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_frame_preceding(tsqlParser.Window_frame_precedingContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#window_frame_following}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindow_frame_following(tsqlParser.Window_frame_followingContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#create_database_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_database_option(tsqlParser.Create_database_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#database_filestream_option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_filestream_option(tsqlParser.Database_filestream_optionContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#database_file_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase_file_spec(tsqlParser.Database_file_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#file_group}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_group(tsqlParser.File_groupContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#file_spec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_spec(tsqlParser.File_specContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#full_table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFull_table_name(tsqlParser.Full_table_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_name(tsqlParser.Table_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#simple_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_name(tsqlParser.Simple_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#func_proc_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_proc_name(tsqlParser.Func_proc_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#ddl_object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDdl_object(tsqlParser.Ddl_objectContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#full_column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFull_column_name(tsqlParser.Full_column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#column_name_list_with_order}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_name_list_with_order(tsqlParser.Column_name_list_with_orderContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#column_name_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_name_list(tsqlParser.Column_name_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#cursor_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCursor_name(tsqlParser.Cursor_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#on_off}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOn_off(tsqlParser.On_offContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#clustered}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClustered(tsqlParser.ClusteredContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#null_notnull}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull_notnull(tsqlParser.Null_notnullContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#null_or_default}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull_or_default(tsqlParser.Null_or_defaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#scalar_function_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScalar_function_name(tsqlParser.Scalar_function_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#begin_conversation_timer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBegin_conversation_timer(tsqlParser.Begin_conversation_timerContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#begin_conversation_dialog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBegin_conversation_dialog(tsqlParser.Begin_conversation_dialogContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#contract_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContract_name(tsqlParser.Contract_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#service_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitService_name(tsqlParser.Service_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#end_conversation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnd_conversation(tsqlParser.End_conversationContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#waitfor_conversation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWaitfor_conversation(tsqlParser.Waitfor_conversationContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#get_conversation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGet_conversation(tsqlParser.Get_conversationContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#queue_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueue_id(tsqlParser.Queue_idContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#send_conversation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSend_conversation(tsqlParser.Send_conversationContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#data_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitData_type(tsqlParser.Data_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#default_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefault_value(tsqlParser.Default_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(tsqlParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#sign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSign(tsqlParser.SignContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(tsqlParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#simple_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_id(tsqlParser.Simple_idContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#comparison_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparison_operator(tsqlParser.Comparison_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#assignment_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_operator(tsqlParser.Assignment_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link tsqlParser#file_size}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile_size(tsqlParser.File_sizeContext ctx);
}