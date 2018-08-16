# Rules #

Plugin supports the following 15 rules:

- C001 - WAITFOR is used
- C002 - SELECT * is used
- C003 - INSERT statement without columns listed
- C004 - ORDER BY clause contains positional references
- C005 - EXECUTE/EXEC for dynamic query is used
- C007 - NOLOCK hint used
- C009 - Non-sargable statement used
- C010 - Defined primary key is not using recommended naming convention
- C011 - Defined foreign key is not using recommended naming convention
- C012 - Comparison operator (=, <>, !=) to check if value is null used
- C013 - Defined index name is not using recommended naming convention
- C014 - OR verb is used in a WHERE clause
- C015 - UNION operator is used
- C016 - IN/NOT IN is used for a subquery
- C017 - ORDER BY clause does not contain order (ASC/DESC)

## C001 - WAITFOR is used ##
WAITFOR is used.

### Non-compliant examples ###

`WAITFOR '10:00:00';`

## C002 - SELECT * is used ##
<p>SELECT * is used. Please list names.</p>

### Compliant examples ###

`SELECT name, surname from dbo.test;`

`SELECT name, surname, 1 * 3 from dbo.test;`

### Non-compliant examples ###

`SELECT * from dbo.test;`

## C003 - INSERT statement without columns listed ##
<p>INSERT statement does not have columns listed. Always use a column list in your INSERT statements.</p>

### Compliant examples ###

`INSERT INTO dbo.test (a,b) VALUES (1,2);`

### Non-compliant examples ###

`INSERT INTO dbo.test VALUES (1,2);`

## C004 - ORDER BY clause contains positional references ##
<p>Do not use column numbers in the ORDER BY clause. Always use column names in an order by clause. Avoid positional references.</p>

### Compliant examples ###

`SELECT * from dbo.test order by name;`

### Non-compliant examples ###

`SELECT * from dbo.test order by 1;`

## C005 - EXECUTE/EXEC for dynamic query is used ##
<p>EXECUTE/EXEC for dynamic query was used. It is better to use sp_executesql for dynamic queries.</p>

### Compliant examples ###

`EXECUTE sp_executesql N'select 1';`

`exec sys.sp_test  @test = 'Publisher';`

### Non-compliant examples ###

`EXEC ('SELECT 1');`

`EXEC (@sQueryText);`

## C007 - NOLOCK hint used ##
<p>Use of NOLOCK might cause data inconsistency problems.</p>

### Compliant examples ###

`SELECT name, surname from dbo.test;`

### Non-compliant examples ###

`SELECT name, surname from dbo.test WITH (NOLOCK);`

## C009 - Non-sargable statement used ##
<p>Use of non-sargeable arguments might cause performance problems.</p>

### Compliant examples ###

`SELECT MAX(RateChangeDate)  FROM HumanResources.EmployeePayHistory WHERE BusinessEntityID = 1`

`SELECT name, surname from dbo.test where date between 2008-10-10 and 2010-10-10;`

`SELECT max(price) from dbo.items;`

### Non-compliant examples ###

`SELECT name, surname from dbo.test where year(date) > 2008`

`SELECT name, surname from dbo.test where name like '%red' `

## C010 - Defined primary key is not using recommended naming convention ##
<p>Defined primary key is not using recommended naming convention to start with PK_.</p>

### Compliant examples ###

`ALTER TABLE dbo.Orders ADD CONSTRAINT PK_OrderId PRIMARY KEY CLUSTERED (Id);`

### Non-compliant examples ###

`CREATE TABLE dbo.Orders
(
Id int NOT NULL,
CONSTRAINT OrderID PRIMARY KEY CLUSTERED (Id) 
);  `

`CREATE TABLE dbo.Orders
(
Id int NOT NULL,  
PRIMARY KEY (Id)
);  `

## C011 - Defined foreign key is not using recommended naming convention ##
<p>Defined foreign key is not using recommended naming convention to start with FK_.</p>

### Compliant examples ###

`ALTER TABLE dbo.Orders ADD CONSTRAINT FK_ClientId FOREIGN KEY (ClientId) REFERENCES dbo.Clients(Id); `

### Non-compliant examples ###

`ALTER TABLE dbo.Orders ADD CONSTRAINT ClientId FOREIGN KEY (ClientId) REFERENCES dbo.Clients(Id);  `

## C012 - Comparison operator (=, <>, !=) to check if value is null used ##
<p>It is not advisable to use comparison operator to check if value is null as comparison operators return UNKNOWN when either or both arguments are NULL. Please use IS NULL or IS NOT NULL instead.</p>

### Compliant examples ###

`SELECT * from dbo.test where name IS NULL;`

`SELECT * from dbo.test where name IS NOT NULL;`

`SELECT * from dbo.test where name = 'test';`

### Non-compliant examples ###

`SELECT * from dbo.test where name = null;`

`SELECT * from dbo.test where name != null;`

`SELECT * from dbo.test where name <> null;`

## C013 - Defined index name is not using recommended naming convention ##
<p>Defined index name is not using recommended naming convention to start with IX_.</p>

### Compliant examples ###

`CREATE UNIQUE INDEX IX_Test_Name on dbo.test (Name);`

### Non-compliant examples ###

`CREATE UNIQUE INDEX Test_Name on dbo.test (Name);`

## C014 - OR verb is used in a WHERE clause ##
<p>It is  advisable to consider using UNION/UNION ALL operator instead of OR verb in the WHERE clause.</p>

### Compliant examples ###

`SELECT name, surname, count from dbo.test where name = 'or' and surname = 'TestOR';`

### Non-compliant examples ###

`SELECT name, surname, count from dbo.test where name = 'Test' OR surname = 'Testor';`

## C015 - UNION operator is used ##
<p>It is  advisable to consider using UNION ALL operator instead of UNION.</p>

### Compliant examples ###

`SELECT name, surname, count from dbo.test union all SELECT name, surname, count from dbo.test2;`

### Non-compliant examples ###

`SELECT name, surname, count from dbo.test union SELECT name, surname, count from dbo.test2;`

## C016 - IN/NOT IN is used for a subquery ##
<p>Consider using EXISTS/NOT EXISTS operator instead of IN for a subquery.</p>

### Compliant examples ###

`SELECT name, surname, count from dbo.test where locationID in (1,2,3);`

`SELECT name, surname, count from dbo.test where locationID not in (1,2,3);`

`SELECT name, surname, count from dbo.test where exists (select 1 from dbo.locations where id = locationID);`

### Non-compliant examples ###

`SELECT name, surname, count from dbo.test where locationID in (select id from dbo.locations);`

`SELECT name, surname, count from dbo.test where locationID not in (select id from dbo.locations);`

## C017 - ORDER BY clause does not contain order (ASC/DESC) ##
<p>It is advisable to specidy order how rows should be ordered.</p>

### Compliant examples ###

`SELECT name, surname from dbo.test order by name desc, surname asc;`

### Non-compliant examples ###

`SELECT name, surname from dbo.test order by name, surname asc;`

