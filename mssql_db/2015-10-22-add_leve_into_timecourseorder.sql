
use pula_sys
go
alter table pula_sys.dbo.time_course_order add 
	level NVARCHAR(40) NOT NULL default ''
go