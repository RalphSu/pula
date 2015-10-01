
use pula_sys
go
alter table pula_sys.dbo.time_course_order add 
	specialCourseCount int NOT NULL default 0,
	usedSpecialCourseCount int NOT NULL default 0
go


alter table pula_sys.dbo.time_course_usage add 
	usedSpecialCourseCount int NOT NULL default 0
go