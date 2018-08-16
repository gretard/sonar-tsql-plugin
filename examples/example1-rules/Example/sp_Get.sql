CREATE PROCEDURE [dbo].[sp_Get]
	@param1 int = 0,
	@param2 int
AS
BEGIN
SELECT * FROM TestTable;
EXEC ('SELECT 1');
insert into dbo.test values (1,2);

EXECUTE sp_executesql N'select 1';

RETURN 0
END
-- kasldjaskjd