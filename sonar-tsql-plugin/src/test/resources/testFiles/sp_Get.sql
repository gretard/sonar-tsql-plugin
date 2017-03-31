CREATE PROCEDURE [dbo].[sp_Get]
	@param1 int = 0,
	@param2 int
AS
	SELECT * FROM TestTable
RETURN 0
