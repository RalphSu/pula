
use pula_sys
go
alter table pula_sys.dbo.time_course_order add 
	orderStatus int NOT NULL default 0
go