
use pula_sys
go
alter table pula_sys.dbo.time_course_order add 
	courseTime NVARCHAR(40) NOT NULL default '',
	effectTime NVARCHAR(40) NOT NULL default ''
go