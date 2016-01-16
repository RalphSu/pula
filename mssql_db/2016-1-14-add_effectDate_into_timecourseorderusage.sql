
use pula_sys
go
alter table pula_sys.dbo.time_course_usage add 
	effectDate datetime default getdate()
go