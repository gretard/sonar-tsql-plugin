GO
--test
SELECT FirstName, MiddleName
FROM Person.Person WHERE LastName = 
'Adams';
BEGIN TRANSACTION;

GO

IF @@TRANCOUNT = 0

BEGIN

    SELECT FirstName, MiddleName 

    FROM Person.Person WHERE LastName = 'Adams';

    ROLLBACK TRANSACTION;

    PRINT N'Rolling back the transaction two times would cause an error.';

END;

ROLLBACK TRANSACTION;

PRINT N'Rolled back the transaction.';

GO

