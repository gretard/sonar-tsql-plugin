SET NOCOUNT ON;  

DECLARE @vendor_id int, @vendor_name nvarchar(50),  
    @message varchar(80), @product nvarchar(50);  

PRINT '-------- Vendor Products Report --------';  

DECLARE vendor_cursor CURSOR FOR   
SELECT VendorID, Name  
FROM Purchasing.Vendor  
WHERE PreferredVendorStatus = 1  
ORDER BY VendorID;  

OPEN vendor_cursor  

FETCH NEXT FROM vendor_cursor   
INTO @vendor_id, @vendor_name  

WHILE @@FETCH_STATUS = 0  
BEGIN  
    PRINT ' '  
   
        DECLARE product_cursor CURSOR FOR   
    SELECT v.Name  
    FROM Purchasing.ProductVendor pv, Production.Product v  
    WHERE pv.ProductID = v.ProductID AND  
    pv.VendorID = @vendor_id  -- Variable value from the outer cursor  


    OPEN product_cursor  
    FETCH NEXT FROM product_cursor INTO @product  

    IF @@FETCH_STATUS <> 0   
        PRINT '         <<None>>'       

    WHILE @@FETCH_STATUS = 0  
    BEGIN  

        SELECT @message = '         ' + @product  
        PRINT @message  
        FETCH NEXT FROM product_cursor INTO @product  
        END  

    CLOSE product_cursor  
    DEALLOCATE product_cursor  
   

END   

select * from test
CLOSE vendor_cursor; 
 
DEALLOCATE vendor_cursor; 

WAITFOR '10:00:00'
 